package com.toolvault.procurement_service.service;

import com.toolvault.procurement_service.dto.ProcurementRequestDto;
import com.toolvault.procurement_service.dto.ProcurementStatusDto;
import com.toolvault.procurement_service.dto.ProcurementUpdateDto;
import com.toolvault.procurement_service.repository.PurchaseRequestRepository;
import com.toolvault.procurement_service.service.impl.ProcurementServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ProcurementServiceImpl.class)
class ProcurementServiceTest {

    @Autowired private ProcurementServiceImpl service;
    @Autowired private PurchaseRequestRepository repository;

    @Test
    void approve_pending_becomes_approved() {
        var dto = new ProcurementRequestDto();
        dto.setSku("GB-ITEM-020"); dto.setQuantity(4);
        var created = service.createRequest(dto);

        var upd = new ProcurementUpdateDto();
        upd.setAction("APPROVE");
        var updated = service.updateStatus(created.getId(), upd);
        assertEquals("APPROVED", updated.getStatus());
    }
}
