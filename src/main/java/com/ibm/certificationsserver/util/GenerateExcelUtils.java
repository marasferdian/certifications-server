package com.ibm.certificationsserver.util;

import com.ibm.certificationsserver.model.RequestDetails;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class GenerateExcelUtils {
    public static byte[] createExcel(List<RequestDetails> requestDetails) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Certifications");

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        fillCells(headerStyle, header, "Participant's Name", 0);
        fillCells(headerStyle, header, "Certification's Title", 1);
        fillCells(headerStyle, header, "Category", 2);
        fillCells(headerStyle, header, "Cost", 3);
        fillCells(headerStyle, header, "Business Justification", 4);
        fillCells(headerStyle, header, "Quarter", 5);
        fillCells(headerStyle, header, "Status", 6);

        CellStyle style = workbook.createCellStyle();

        for(int i = 0; i < requestDetails.size(); ++i) {
            Row row = sheet.createRow(i + 2);

            RequestDetails details = requestDetails.get(i);

            fillCells(style, row, details.getParticipantName(), 0);
            fillCells(style, row, details.getCertificationTitle(), 1);
            fillCells(style, row, details.getCategory().toString(), 2);
            fillCells(style, row, details.getCost() + "", 3);
            fillCells(style, row, details.getBusinessJustification(), 4);
            fillCells(style, row, details.getQuarter().toString(), 5);
            fillCells(style, row, details.getStatus().toString(), 6);
        }

        for(int i = 0; i < 7; ++i) {
            sheet.autoSizeColumn(i);
        }

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "src/main/resources/Certifications.xlsx";

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            outputStream.close();

            File file = new File(fileLocation);
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void fillCells(CellStyle style, Row row, String s, int i) {
        Cell cell = row.createCell(i);
        cell.setCellValue(s);
        cell.setCellStyle(style);
    }
}
