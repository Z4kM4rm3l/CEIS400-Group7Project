package com.toolvault.depot_ops_service.service;

import com.toolvault.depot_ops_service.domain.Asset;
import com.toolvault.depot_ops_service.domain.Job;
import com.toolvault.depot_ops_service.domain.JobAlert;
import com.toolvault.depot_ops_service.repository.AssetRepository;
import com.toolvault.depot_ops_service.repository.JobAlertRepository;
import com.toolvault.depot_ops_service.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

    private final JobRepository jobs;
    private final JobAlertRepository alerts;
    private final AssetRepository assets;

    @Transactional
    public List<Job> getAllJobs() {
        return jobs.findAll();
    }

    @Transactional
    public List<Job> getJobsByTechnician(String technicianEmail) {
        return jobs.findByTechnicianEmail(technicianEmail);
    }

    @Transactional
    public List<Job> getActiveJobsByTechnician(String technicianEmail) {
        return jobs.findByTechnicianEmailAndStatus(technicianEmail, Job.Status.ACTIVE);
    }

    @Transactional
    public List<Job> getJobsByStatus(Job.Status status) {
        return jobs.findByStatus(status);
    }

    @Transactional
    public Job getJobById(Long id) {
        return jobs.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found: " + id));
    }

    @Transactional
    public Job createJob(String name, String site, String technicianEmail, String technicianName) {
        Job job = new Job(name, site, technicianEmail, technicianName);
        Job saved = jobs.save(job);
        log.info("Job created: {} for technician: {}", name, technicianName);
        return saved;
    }

    @Transactional
    public Job assignAssetToJob(Long jobId, String assetTag) {
        Job job = getJobById(jobId);

        Asset asset = assets.findByTag(assetTag)
                .orElseThrow(() -> new RuntimeException("Asset not found: " + assetTag));

        if (asset.getStatus() != Asset.Status.IN_DEPOT) {
            throw new IllegalStateException("Asset " + assetTag + " is not available for assignment");
        }

        job.addAsset(assetTag);
        asset.setStatus(Asset.Status.OUT_WITH_TECH);
        assets.save(asset);

        log.info("Asset {} assigned to job {}", assetTag, jobId);
        return jobs.save(job);
    }

    @Transactional
    public Job removeAssetFromJob(Long jobId, String assetTag) {
        Job job = getJobById(jobId);
        job.removeAsset(assetTag);

        Asset asset = assets.findByTag(assetTag)
                .orElseThrow(() -> new RuntimeException("Asset not found: " + assetTag));
        asset.setStatus(Asset.Status.IN_DEPOT);
        assets.save(asset);

        log.info("Asset {} removed from job {}", assetTag, jobId);
        return jobs.save(job);
    }

    @Transactional
    public Job markJobComplete(Long jobId, String technicianEmail) {
        Job job = getJobById(jobId);

        if (!job.getTechnicianEmail().equals(technicianEmail)) {
            throw new IllegalStateException("Only the assigned technician can mark this job complete");
        }

        if (job.getStatus() != Job.Status.ACTIVE) {
            throw new IllegalStateException("Job is not active");
        }

        Instant completedAt = Instant.now();
        Instant returnDeadline = completedAt.plus(24, ChronoUnit.HOURS);

        job.setStatus(Job.Status.COMPLETED);
        job.setCompletedAt(completedAt);
        job.setReturnDeadline(returnDeadline);

        Job saved = jobs.save(job);

        JobAlert completionAlert = new JobAlert(
                saved,
                JobAlert.AlertType.JOB_COMPLETED,
                "Job '" + job.getName() + "' marked complete by " + job.getTechnicianName() +
                        " at " + job.getSite() + ". Tools must be returned by " +
                        returnDeadline.truncatedTo(ChronoUnit.MINUTES) + "."
        );
        alerts.save(completionAlert);

        log.info("Job {} marked complete by {}. Return deadline: {}", jobId, technicianEmail, returnDeadline);
        return saved;
    }

    @Transactional
    public JobAlert resolveAlert(Long alertId, String managerEmail) {
        JobAlert alert = alerts.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found: " + alertId));
        alert.resolve();
        log.info("Alert {} resolved by {}", alertId, managerEmail);
        return alerts.save(alert);
    }

    @Transactional
    public Job overrideReturnDeadline(Long jobId, Instant newDeadline, String managerEmail) {
        Job job = getJobById(jobId);
        job.setReturnDeadline(newDeadline);
        log.info("Return deadline for job {} overridden by {} to {}", jobId, managerEmail, newDeadline);
        return jobs.save(job);
    }

    @Transactional
    public List<JobAlert> getUnresolvedAlerts() {
        return alerts.findByResolvedFalseOrderByCreatedAtDesc();
    }

    public long getUnresolvedAlertCount() {
        return alerts.countByResolvedFalse();
    }

    @Transactional
    public List<JobAlert> getAlertsForJob(Long jobId) {
        return alerts.findByJob_IdAndResolvedFalse(jobId);
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void checkOverdueReturns() {
        log.info("Running overdue return check...");

        Instant now = Instant.now();
        List<Job> overdueJobs = jobs.findByStatusAndReturnDeadlineBefore(
                Job.Status.COMPLETED, now
        );

        for (Job job : overdueJobs) {
            boolean hasUnreturnedAssets = job.getAssetTags().stream()
                    .anyMatch(tag -> assets.findByTag(tag)
                            .map(a -> a.getStatus() == Asset.Status.OUT_WITH_TECH)
                            .orElse(false));

            if (hasUnreturnedAssets) {
                job.setStatus(Job.Status.OVERDUE);
                jobs.save(job);

                JobAlert overdueAlert = new JobAlert(
                        job,
                        JobAlert.AlertType.OVERDUE_RETURN,
                        "Tools not returned for job '" + job.getName() + "' assigned to " +
                                job.getTechnicianName() + " at " + job.getSite() +
                                ". Return deadline was " + job.getReturnDeadline()
                                .truncatedTo(ChronoUnit.MINUTES) + "."
                );
                alerts.save(overdueAlert);

                log.warn("Overdue return alert created for job {}: {}", job.getId(), job.getName());
            }
        }

        log.info("Overdue check complete. {} jobs flagged.", overdueJobs.size());
    }
}