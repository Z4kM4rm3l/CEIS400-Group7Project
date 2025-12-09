package com.toolvault.procurement_service.service.impl;

import com.toolvault.procurement_service.dto.ProcurementRequestDto;
import com.toolvault.procurement_service.dto.ProcurementStatusDto;
import com.toolvault.procurement_service.dto.ProcurementUpdateDto;
import com.toolvault.procurement_service.entity.PurchaseRequest;
import com.toolvault.procurement_service.repository.PurchaseRequestRepository;
import com.toolvault.procurement_service.service.ProcurementService;
import org.springframework.stereotype.Service;

@Service
public class ProcurementServiceImpl implements ProcurementService {

    private final PurchaseRequestRepository repository;

    public ProcurementServiceImpl(PurchaseRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProcurementStatusDto createRequest(ProcurementRequestDto requestDto) {
        PurchaseRequest pr = new PurchaseRequest();
        pr.setSku(requestDto.getSku());
        pr.setQuantity(requestDto.getQuantity());
        pr.setStatus("PENDING");
        pr = repository.save(pr);
        return toDto(pr);
    }

    @Override
    public ProcurementStatusDto getStatus(Long id) {
        PurchaseRequest pr = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));
        return toDto(pr);
    }

    @Override
    public ProcurementStatusDto updateStatus(Long id, ProcurementUpdateDto updateDto) {
        PurchaseRequest pr = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));

        String current = pr.getStatus();
        if (!"PENDING".equalsIgnoreCase(current)) {
            throw new IllegalStateException("Only PENDING requests can be updated; current status=" + current);
        }

        String action = updateDto.getAction();
        if (action == null) {
            throw new IllegalArgumentException("Action must be provided");
        }

        switch (action.toUpperCase()) {
            case "APPROVE":
                pr.setStatus("APPROVED");
                break;
            case "REJECT":
                pr.setStatus("REJECTED");
                break;
            default:
                throw new IllegalArgumentException("Unsupported action: " + action);
        }

        pr = repository.save(pr);
        return toDto(pr);
    }

    private ProcurementStatusDto toDto(PurchaseRequest pr) {
        ProcurementStatusDto dto = new ProcurementStatusDto();
        dto.setId(pr.getId());
        dto.setSku(pr.getSku());
        dto.setQuantity(pr.getQuantity());
        dto.setStatus(pr.getStatus());
        return dto;
    }
}
