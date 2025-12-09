package com.toolvault.reporting_service.service;

import com.toolvault.reporting_service.dto.DepotActivityDto;
import com.toolvault.reporting_service.dto.SummaryReportDto;
import com.toolvault.reporting_service.dto.WarehouseStockDto;

import java.util.List;

public interface ReportService {
    List<DepotActivityDto> getDepotActivities(String token);
    List<WarehouseStockDto> getWarehouseStock(String token);
    SummaryReportDto getSummary(String token);

    byte[] exportWarehouse(String token, String format);
    byte[] exportDepot(String token, String format);
    byte[] exportSummary(String token, String format);
}
