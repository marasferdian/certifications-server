package com.ibm.certificationsserver.controller;

import com.ibm.certificationsserver.model.Certification;
import com.ibm.certificationsserver.model.CertificationFilter;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.service.CertificationService;
import com.ibm.certificationsserver.service.UserService;
import com.ibm.certificationsserver.util.GenerateExcelUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
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
    @PostMapping(value = "/excel")
    public  ResponseEntity<byte[]> getExcel(@RequestBody CertificationFilter certificationFilter){
        List<RequestDetails> certifications=certificationService.queryCertificationsWithFilter(certificationFilter,null);
        byte[] excelContent = GenerateExcelUtils.createExcel(certifications);

        return ResponseEntity.status(HttpStatus.OK).header("Filename", "requests.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }
}
