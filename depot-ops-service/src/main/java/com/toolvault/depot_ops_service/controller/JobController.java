package com.toolvault.depot_ops_service.controller;

import com.toolvault.depot_ops_service.domain.Job;
import com.toolvault.depot_ops_service.domain.JobAlert;
import com.toolvault.depot_ops_service.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Job>> getJobsByStatus(@PathVariable String status) {
        Job.Status jobStatus = Job.Status.valueOf(status.toUpperCase());
        return ResponseEntity.ok(jobService.getJobsByStatus(jobStatus));
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<List<Job>> getMyJobs() {
        return ResponseEntity.ok(jobService.getActiveJobsByTechnician(getCurrentUserEmail()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Job> createJob(@RequestBody Map<String, String> body) {
        Job job = jobService.createJob(
                body.get("name"),
                body.get("site"),
                body.get("technicianEmail"),
                body.get("technicianName")
        );
        return ResponseEntity.ok(job);
    }

    @PostMapping("/{id}/assign-asset")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Job> assignAsset(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(jobService.assignAssetToJob(id, body.get("assetTag")));
    }

    @PostMapping("/{id}/remove-asset")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Job> removeAsset(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(jobService.removeAssetFromJob(id, body.get("assetTag")));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Job> markComplete(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.markJobComplete(id, getCurrentUserEmail()));
    }

    @PostMapping("/{id}/override-deadline")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Job> overrideDeadline(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Instant newDeadline = Instant.parse(body.get("returnDeadline"));
        return ResponseEntity.ok(jobService.overrideReturnDeadline(id, newDeadline, getCurrentUserEmail()));
    }

    @GetMapping("/alerts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobAlert>> getAlerts() {
        return ResponseEntity.ok(jobService.getUnresolvedAlerts());
    }

    @GetMapping("/alerts/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getAlertCount() {
        return ResponseEntity.ok(Map.of("count", jobService.getUnresolvedAlertCount()));
    }

    @GetMapping("/{id}/alerts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobAlert>> getJobAlerts(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getAlertsForJob(id));
    }

    @PostMapping("/alerts/{alertId}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobAlert> resolveAlert(@PathVariable Long alertId) {
        return ResponseEntity.ok(jobService.resolveAlert(alertId, getCurrentUserEmail()));
    }
}