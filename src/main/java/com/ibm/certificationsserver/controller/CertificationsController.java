package com.ibm.certificationsserver.controller;

import com.ibm.certificationsserver.model.Certification;
import com.ibm.certificationsserver.model.CertificationFilter;
import com.ibm.certificationsserver.model.Request;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.service.CertificationService;
import org.h2.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certifications")
public class CertificationsController {

    @Autowired
    private CertificationService certificationService;

    // ---------------------------------------CREATE OPERATIONS---------------------------------------

    //ADMIN STUFF
    @PostMapping("")
    public ResponseEntity<Certification> addCertification(@RequestBody Certification certification) {
        certification.setId(null);
        certificationService.addCertification(certification);
        return new ResponseEntity<>(certification, HttpStatus.OK);
    }

    //CLIENT
    @PostMapping("/request")
    public ResponseEntity<Request> addRequest(@RequestBody Request request){
        certificationService.addRequest(request);
        return new ResponseEntity<>(request,HttpStatus.OK);
    }
    // ---------------------------------------READ OPERATIONS---------------------------------------

    //CLIENT
    @GetMapping("")
    public ResponseEntity<List<Certification>> queryCertifications() {
        List<Certification> certifications=certificationService.queryCertifications();
        if(certifications.isEmpty())
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(certifications, HttpStatus.OK);
    }

    //ADMIN
    @PostMapping("/filters")
    public ResponseEntity<List<RequestDetails>> queryCertificationsWithFilter(@RequestBody CertificationFilter certificationFilter){
        List<RequestDetails> certifications=certificationService.queryCertificationsWithFilter(certificationFilter);
        if(certifications.isEmpty())
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(certifications,HttpStatus.OK);
    }

    /* TODO Excel stuff */

    // ---------------------------------------UPDATE OPERATIONS---------------------------------------

    //CLIENT - Modify BusinessJustification and/or Quarter
    @PutMapping("/request")
    public ResponseEntity<Request> updateRequest(@RequestBody Request request){
        Request req=certificationService.updateRequest(request);
        return new ResponseEntity<>(req,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCertification(@PathVariable("id") int id /* TODO */) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity approveCertificationsList(/* TODO*/) {
        /* TODO */
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //ADMIN
    @PutMapping("/{quarter}/{name}")
    public ResponseEntity approveRequestFilterList(@PathVariable("quarter") String quarter,
                                                            @PathVariable("name") String participantName) {
        List<Request> requests=certificationService.approveRequestFilterList(quarter,participantName);
        if(requests.isEmpty())
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    // ---------------------------------------DELETE OPERATIONS---------------------------------------

    //CLIENT
    @DeleteMapping("/request")
    public ResponseEntity<Request> deleteRequest(@RequestParam long userId,@RequestParam long certificationId){
        certificationService.deleteRequest(userId,certificationId);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity deleteCertification(@RequestParam int id) {
        /* TODO */
        System.out.println("The id: " + id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
