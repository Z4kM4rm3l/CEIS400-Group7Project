package com.toolvault.procurement_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toolvault.procurement_service.dto.ProcurementRequestDto;
import com.toolvault.procurement_service.dto.ProcurementStatusDto;
import com.toolvault.procurement_service.service.ProcurementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProcurementController.class)
@AutoConfigureMockMvc(addFilters = false)  // <— disables Spring Security filters for this test
class ProcurementControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ProcurementService service;

    @Test
    void createRequest_returnsStatus() throws Exception {
        ProcurementRequestDto dto = new ProcurementRequestDto();
        dto.setSku("GB-ITEM-001"); dto.setQuantity(5); dto.setReason("Auto-reorder");

        ProcurementStatusDto out = new ProcurementStatusDto();
        out.setId(1L); out.setSku("GB-ITEM-001"); out.setQuantity(5); out.setStatus("PENDING");
        Mockito.when(service.createRequest(Mockito.any())).thenReturn(out);

        mockMvc.perform(post("/api/procurement/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void approve_returnsApproved() throws Exception {
        ProcurementStatusDto out = new ProcurementStatusDto();
        out.setId(10L); out.setSku("GB-ITEM-010"); out.setQuantity(2); out.setStatus("APPROVED");
        Mockito.when(service.updateStatus(Mockito.eq(10L), Mockito.any())).thenReturn(out);

        mockMvc.perform(patch("/api/procurement/10/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}
