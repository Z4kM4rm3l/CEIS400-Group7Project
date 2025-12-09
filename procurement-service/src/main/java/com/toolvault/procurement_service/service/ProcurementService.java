package com.toolvault.procurement_service.service;

import com.toolvault.procurement_service.dto.ProcurementRequestDto;
import com.toolvault.procurement_service.dto.ProcurementStatusDto;
import com.toolvault.procurement_service.dto.ProcurementUpdateDto;

public interface ProcurementService {
    ProcurementStatusDto createRequest(ProcurementRequestDto requestDto);
    ProcurementStatusDto getStatus(Long id);
    ProcurementStatusDto updateStatus(Long id, ProcurementUpdateDto updateDto);
}
