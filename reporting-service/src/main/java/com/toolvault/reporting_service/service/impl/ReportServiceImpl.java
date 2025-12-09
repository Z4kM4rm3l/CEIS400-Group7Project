package com.toolvault.reporting_service.service.impl;

import com.toolvault.reporting_service.client.DepotClient;
import com.toolvault.reporting_service.client.WarehouseClient;
import com.toolvault.reporting_service.dto.DepotActivityDto;
import com.toolvault.reporting_service.dto.SummaryReportDto;
import com.toolvault.reporting_service.dto.WarehouseStockDto;
import com.toolvault.reporting_service.service.ReportService;

// ----- Apache POI (Excel) -----
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// ----- OpenPDF (PDF) -----
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final WarehouseClient warehouseClient;
    private final DepotClient depotClient;

    public ReportServiceImpl(WarehouseClient warehouseClient, DepotClient depotClient) {
        this.warehouseClient = warehouseClient;
        this.depotClient = depotClient;
    }

    // ----------- Query methods -----------

    @Override
    public List<DepotActivityDto> getDepotActivities(String token) {
        return depotClient.fetchActivities(token);
    }

    @Override
    public List<WarehouseStockDto> getWarehouseStock(String token) {
        return warehouseClient.fetchStock(token);
    }

    @Override
    public SummaryReportDto getSummary(String token) {
        List<WarehouseStockDto> stock = getWarehouseStock(token);
        List<DepotActivityDto> acts = getDepotActivities(token);
        SummaryReportDto dto = new SummaryReportDto();

        dto.setTotalItems(stock.size());

        List<WarehouseStockDto> low = stock.stream()
                .filter(s -> s.getQuantity() < s.getThreshold())
                .collect(Collectors.toList());
        dto.setLowStockItems(low);
        dto.setLowStockCount(low.size());

        dto.setDepotTransactionsCount(acts.size());
        return dto;
    }

    // ----------- Export endpoints -----------

    @Override
    public byte[] exportWarehouse(String token, String format) {
        List<WarehouseStockDto> stock = getWarehouseStock(token);
        if ("xlsx".equalsIgnoreCase(format)) {
            return toExcelWarehouse(stock);
        }
        return toPdfWarehouse(stock);
    }

    @Override
    public byte[] exportDepot(String token, String format) {
        List<DepotActivityDto> acts = getDepotActivities(token);
        if ("xlsx".equalsIgnoreCase(format)) {
            return toExcelDepot(acts);
        }
        return toPdfDepot(acts);
    }

    @Override
    public byte[] exportSummary(String token, String format) {
        SummaryReportDto summary = getSummary(token);
        if ("xlsx".equalsIgnoreCase(format)) {
            return toExcelSummary(summary);
        }
        return toPdfSummary(summary);
    }

    // ----------- Excel helpers -----------

    private byte[] toExcelWarehouse(List<WarehouseStockDto> stock) {
        try (XSSFWorkbook wb = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = wb.createSheet("Warehouse");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("SKU");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Quantity");
            header.createCell(3).setCellValue("Threshold");

            int r = 1;
            for (var s : stock) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(s.getSku());
                row.createCell(1).setCellValue(s.getName());
                row.createCell(2).setCellValue(s.getQuantity());
                row.createCell(3).setCellValue(s.getThreshold());
            }

            wb.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Excel generation (warehouse) failed", e);
        }
    }

    private byte[] toExcelDepot(List<DepotActivityDto> acts) {
        try (XSSFWorkbook wb = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = wb.createSheet("Depot");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("EquipmentId");
            header.createCell(1).setCellValue("User");
            header.createCell(2).setCellValue("Action");
            header.createCell(3).setCellValue("Timestamp");

            int r = 1;
            for (var a : acts) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(a.getEquipmentId());
                row.createCell(1).setCellValue(a.getUser());
                row.createCell(2).setCellValue(a.getAction());
                row.createCell(3).setCellValue(a.getTimestamp() == null ? "" : a.getTimestamp().toString());
            }

            wb.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Excel generation (depot) failed", e);
        }
    }

    private byte[] toExcelSummary(SummaryReportDto s) {
        try (XSSFWorkbook wb = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = wb.createSheet("Summary");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Metric");
            header.createCell(1).setCellValue("Value");

            Row r1 = sheet.createRow(1);
            r1.createCell(0).setCellValue("Total Items");
            r1.createCell(1).setCellValue(s.getTotalItems());

            Row r2 = sheet.createRow(2);
            r2.createCell(0).setCellValue("Low Stock Count");
            r2.createCell(1).setCellValue(s.getLowStockCount());

            Row r3 = sheet.createRow(3);
            r3.createCell(0).setCellValue("Depot Transactions");
            r3.createCell(1).setCellValue(s.getDepotTransactionsCount());

            wb.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Excel generation (summary) failed", e);
        }
    }

    // ----------- PDF helpers -----------

    private byte[] toPdfWarehouse(List<WarehouseStockDto> stock) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, out);
            doc.open();

            doc.add(new Paragraph("Warehouse Stock Report"));
            PdfPTable table = new PdfPTable(4);
            table.addCell("SKU");
            table.addCell("Name");
            table.addCell("Quantity");
            table.addCell("Threshold");

            for (var s : stock) {
                table.addCell(s.getSku());
                table.addCell(s.getName());
                table.addCell(String.valueOf(s.getQuantity()));
                table.addCell(String.valueOf(s.getThreshold()));
            }

            doc.add(table);
            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation (warehouse) failed", e);
        }
    }

    private byte[] toPdfDepot(List<DepotActivityDto> acts) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, out);
            doc.open();

            doc.add(new Paragraph("Depot Activity Report"));
            PdfPTable table = new PdfPTable(4);
            table.addCell("EquipmentId");
            table.addCell("User");
            table.addCell("Action");
            table.addCell("Timestamp");

            for (var a : acts) {
                table.addCell(a.getEquipmentId());
                table.addCell(a.getUser());
                table.addCell(a.getAction());
                table.addCell(a.getTimestamp() == null ? "" : a.getTimestamp().toString());
            }

            doc.add(table);
            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation (depot) failed", e);
        }
    }

    private byte[] toPdfSummary(SummaryReportDto s) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, out);
            doc.open();

            doc.add(new Paragraph("Summary Report"));
            PdfPTable table = new PdfPTable(2);
            table.addCell("Total Items");
            table.addCell(String.valueOf(s.getTotalItems()));
            table.addCell("Low Stock Count");
            table.addCell(String.valueOf(s.getLowStockCount()));
            table.addCell("Depot Transactions");
            table.addCell(String.valueOf(s.getDepotTransactionsCount()));

            doc.add(table);
            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation (summary) failed", e);
        }
    }
}
