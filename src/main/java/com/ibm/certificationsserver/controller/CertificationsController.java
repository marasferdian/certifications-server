package com.ibm.certificationsserver.controller;

import com.ibm.certificationsserver.model.Certification;
import com.ibm.certificationsserver.model.CertificationFilter;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.service.CertificationService;
import com.ibm.certificationsserver.service.UserService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/certifications")
public class CertificationsController {

    @Autowired
    private CertificationService certificationService;
    @Autowired
    private UserService userService;

    // ---------------------------------------CREATE OPERATIONS---------------------------------------

    @PostMapping("/custom")
    public ResponseEntity<Certification> addCustomCertification(@RequestBody Certification customCertification){

        Certification certification = certificationService.addPendingCertification(customCertification);
        return new ResponseEntity<>(certification,HttpStatus.OK);
    }

    //ADMIN (OK)
    @PostMapping("")
    public ResponseEntity<Certification> addCertification(@RequestBody Certification certification) {
        certification.setId(null);
        certificationService.addCertification(certification);
        return new ResponseEntity<>(certification, HttpStatus.OK);
    }

    // ---------------------------------------READ OPERATIONS---------------------------------------

    //CLIENT-ADMIN (OK)
    @GetMapping("")
    public ResponseEntity<List<Certification>> queryCertifications() {
        List<Certification> certifications=certificationService.queryCertifications();
        if(certifications.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(certifications, HttpStatus.OK);
    }

    //CLIENT-ADMIN (OK)
    @GetMapping("/{id}")
    public ResponseEntity<Certification> queryCertification(@PathVariable("id") long id) {
        Certification certification = certificationService.queryCertification(id);
        if(certification == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(certification, HttpStatus.OK);
    }

    //ADMIN (OK)
    @PostMapping("/filters")
    public ResponseEntity<List<RequestDetails>> queryCertificationsWithFilter(@RequestBody CertificationFilter certificationFilter, Authentication auth){
        List<RequestDetails> certifications = null;
        if(UserController.hasAuthority(auth,"ADMIN")) {
            certifications = certificationService.queryCertificationsWithFilter(certificationFilter,null);
        } else {
            String userName = auth.getName();
            Long id = userService.getIdByUsername(userName);
            certifications = certificationService.queryCertificationsWithFilter(certificationFilter,id);
        }
        if(certifications.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
        return new ResponseEntity<>(certifications,HttpStatus.OK);
    }

    // ---------------------------------------UPDATE OPERATIONS---------------------------------------

    //ADMIN (OK)
    @PutMapping("")
    public ResponseEntity updateCertification(@RequestBody Certification newCertification) {
        Certification certification = certificationService.updateCertification(newCertification);
        return new ResponseEntity<>(certification, HttpStatus.OK);
    }

    // ---------------------------------------DELETE OPERATIONS---------------------------------------

    //ADMIN (OK)
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCertification(@PathVariable("id") long id) {
        certificationService.deleteCertification(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    // --------------------------------------------EXCEL---------------------------------------------

    //CLIENT-ADMIN
    @PostMapping("/excel")
    public ResponseEntity getExcel(@RequestBody CertificationFilter certificationFilter){
        List<RequestDetails> certifications=certificationService.queryCertificationsWithFilter(certificationFilter,null);
        createExcel(certifications);
        return ResponseEntity.status(HttpStatus.OK).header("Filename", "requests.xls")
                .body(null);
    }

    private void createExcel(List<RequestDetails> requestDetails) {
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
        String fileLocation = path.substring(0, path.length() - 1) + "Certifications.xlsx";

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillCells(CellStyle style, Row row, String s, int i) {
        Cell cell = row.createCell(i);
        cell.setCellValue(s);
        cell.setCellStyle(style);
    }
}
