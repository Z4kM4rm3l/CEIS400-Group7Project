package com.toolvault.procurement_service.controller;

import com.toolvault.procurement_service.dto.ProcurementRequestDto;
import com.toolvault.procurement_service.dto.ProcurementStatusDto;
import com.toolvault.procurement_service.dto.ProcurementUpdateDto;
import com.toolvault.procurement_service.service.ProcurementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/procurement")
public class ProcurementController {

    private final ProcurementService service;

    public ProcurementController(ProcurementService service) {
        this.service = service;
    }

    @PostMapping("/request")
    public ResponseEntity<ProcurementStatusDto> create(@Valid @RequestBody ProcurementRequestDto dto) {
        return ResponseEntity.ok(service.createRequest(dto));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<ProcurementStatusDto> status(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStatus(id));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ProcurementStatusDto> approve(
            @PathVariable Long id,
            @RequestBody(required = false) ProcurementUpdateDto dto) {
        if (dto == null) dto = new ProcurementUpdateDto();
        dto.setAction("APPROVE");
        return ResponseEntity.ok(service.updateStatus(id, dto));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ProcurementStatusDto> reject(
            @PathVariable Long id,
            @RequestBody(required = false) ProcurementUpdateDto dto) {
        if (dto == null) dto = new ProcurementUpdateDto();
        dto.setAction("REJECT");
        return ResponseEntity.ok(service.updateStatus(id, dto));
    }
}
