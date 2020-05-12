package com.ibm.certificationsserver.controller;

import com.ibm.certificationsserver.exceptions.ExistentException;
import com.ibm.certificationsserver.model.Certification;
import com.ibm.certificationsserver.model.CertificationFilter;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.model.Status;
import com.ibm.certificationsserver.service.CertificationService;
import com.ibm.certificationsserver.service.UserService;
import com.ibm.certificationsserver.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/certifications")
public class CertificationsController {

    @Autowired
    private CertificationService certificationService;
    @Autowired
    private UserService userService;

    // ---------------------------------------CREATE OPERATIONS---------------------------------------

    //CLIENT-ADMIN (OK)
    @PostMapping("")
    public ResponseEntity<Certification> addCertification(@RequestBody Certification certification,Authentication auth) throws ExistentException {
        if(UserController.hasAuthority(auth,"ADMIN")) {
            certification.setId(null);
            certificationService.addCertification(certification);
            return new ResponseEntity<>(certification, HttpStatus.OK);
        }else{
            Certification certif = certificationService.addPendingCertification(certification);
            return new ResponseEntity<>(certif,HttpStatus.OK);
        }
    }

    //CLIENT-ADMIN (OK)
    @PostMapping("/excel")
    public  ResponseEntity<byte[]> getExcel(@RequestBody CertificationFilter certificationFilter){
        List<RequestDetails> certifications = null;
        byte[] excelContent = null;

        if(ExcelUtils.isCached()) {
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "src/main/resources/Certifications.xlsx";

            File file = new File(fileLocation);
            try {
                excelContent = Files.readAllBytes(file.toPath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            certifications = certificationService.queryCertificationsWithFilter(certificationFilter,null);
            excelContent = ExcelUtils.createExcel(certifications);
            ExcelUtils.setCached(true);
        }



        return ResponseEntity.status(HttpStatus.OK).header("Filename", "requests.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }

    //CLIENT-ADMIN (OK)
    @PostMapping("/filters")
    public ResponseEntity<List<RequestDetails>> queryCertificationsWithFilter(@RequestBody CertificationFilter certificationFilter, Authentication auth){
        List<RequestDetails> certifications = null;
        ExcelUtils.setCached(false);
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
    @GetMapping("/custom")
    public ResponseEntity<List<Certification>> queryCustomCertification(){
        List<Certification> certifications=certificationService.queryCustomCertification();
        return new ResponseEntity<>(certifications,HttpStatus.OK);
    }

    // ---------------------------------------UPDATE OPERATIONS---------------------------------------

    //ADMIN (OK)
    @PutMapping("")
    public ResponseEntity updateCertification(@RequestBody Certification newCertification) {
        ExcelUtils.setCached(false);
        Certification certification = certificationService.updateCertification(newCertification);
        return new ResponseEntity<>(certification, HttpStatus.OK);
    }

    //ADMIN
    @PutMapping("/custom/{status}")
    public ResponseEntity<Certification> approveOrRejectCustomCertification(@RequestBody Certification certification, @PathVariable("status") Status status){
        Certification certif=certificationService.approveOrRejectCustomCertification(certification,status);
        return new ResponseEntity<>(certification,HttpStatus.OK);
    }

    // ---------------------------------------DELETE OPERATIONS---------------------------------------

    //ADMIN (OK)
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCertification(@PathVariable("id") long id) {
        certificationService.deleteCertification(id);
        ExcelUtils.setCached(false);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
