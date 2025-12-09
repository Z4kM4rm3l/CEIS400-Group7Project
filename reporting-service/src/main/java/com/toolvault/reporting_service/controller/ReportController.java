package com.toolvault.reporting_service.controller;

import com.toolvault.reporting_service.dto.DepotActivityDto;
import com.toolvault.reporting_service.dto.SummaryReportDto;
import com.toolvault.reporting_service.dto.WarehouseStockDto;
import com.toolvault.reporting_service.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) { this.reportService = reportService; }

    @GetMapping("/warehouse")
    public ResponseEntity<List<WarehouseStockDto>> warehouse(
            @RequestHeader(value="Authorization", required=false) String auth) {
        String token = auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : null;
        return ResponseEntity.ok(reportService.getWarehouseStock(token));
    }

    @GetMapping("/depot")
    public ResponseEntity<List<DepotActivityDto>> depot(
            @RequestHeader(value="Authorization", required=false) String auth) {
        String token = auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : null;
        return ResponseEntity.ok(reportService.getDepotActivities(token));
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryReportDto> summary(
            @RequestHeader(value="Authorization", required=false) String auth) {
        String token = auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : null;
        return ResponseEntity.ok(reportService.getSummary(token));
    }

    @GetMapping("/warehouse/export")
    public ResponseEntity<byte[]> exportWarehouse(
            @RequestHeader(value="Authorization", required=false) String auth,
            @RequestParam(defaultValue="pdf") String format) {
        String token = auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : null;
        byte[] bytes = reportService.exportWarehouse(token, format);
        return buildDownload(bytes, "warehouse." + ("xlsx".equalsIgnoreCase(format) ? "xlsx" : "pdf"));
    }

    @GetMapping("/depot/export")
    public ResponseEntity<byte[]> exportDepot(
            @RequestHeader(value="Authorization", required=false) String auth,
            @RequestParam(defaultValue="pdf") String format) {
        String token = auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : null;
        byte[] bytes = reportService.exportDepot(token, format);
        return buildDownload(bytes, "depot." + ("xlsx".equalsIgnoreCase(format) ? "xlsx" : "pdf"));
    }

    @GetMapping("/summary/export")
    public ResponseEntity<byte[]> exportSummary(
            @RequestHeader(value="Authorization", required=false) String auth,
            @RequestParam(defaultValue="pdf") String format) {
        String token = auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : null;
        byte[] bytes = reportService.exportSummary(token, format);
        return buildDownload(bytes, "summary." + ("xlsx".equalsIgnoreCase(format) ? "xlsx" : "pdf"));
    }

    private ResponseEntity<byte[]> buildDownload(byte[] bytes, String filename) {
        String contentType = filename.endsWith("xlsx")
                ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                : MediaType.APPLICATION_PDF_VALUE;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType))
                .body(bytes);
    }
}
