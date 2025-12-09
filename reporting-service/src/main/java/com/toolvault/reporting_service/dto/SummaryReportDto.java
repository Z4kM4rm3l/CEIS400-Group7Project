package com.toolvault.reporting_service.dto;

import java.util.List;

public class SummaryReportDto {
    private int totalItems;
    private int lowStockCount;
    private int depotTransactionsCount;
    private List<WarehouseStockDto> lowStockItems;

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public int getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(int lowStockCount) { this.lowStockCount = lowStockCount; }

    public int getDepotTransactionsCount() { return depotTransactionsCount; }
    public void setDepotTransactionsCount(int depotTransactionsCount) { this.depotTransactionsCount = depotTransactionsCount; }

    public List<WarehouseStockDto> getLowStockItems() { return lowStockItems; }
    public void setLowStockItems(List<WarehouseStockDto> lowStockItems) { this.lowStockItems = lowStockItems; }
}
