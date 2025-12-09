package com.toolvault.reporting_service.controller;

import com.toolvault.reporting_service.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private ReportService reportService;

    @Test
    void summary_returnsOk() throws Exception {
        Mockito.when(reportService.getSummary(Mockito.any()))
                .thenReturn(new com.toolvault.reporting_service.dto.SummaryReportDto());
        mockMvc.perform(get("/api/reports/summary"))
                .andExpect(status().isOk());
    }
}
