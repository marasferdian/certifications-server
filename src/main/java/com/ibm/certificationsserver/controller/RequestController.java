package com.ibm.certificationsserver.controller;

import com.ibm.certificationsserver.exceptions.ExistentException;
import com.ibm.certificationsserver.model.Request;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {
    @Autowired
    private RequestService requestService;

    // ---------------------------------------CREATE OPERATIONS---------------------------------------

    //CLIENT-ADMIN (OK)
    @PostMapping("")
    public ResponseEntity<RequestDetails> addRequest(@RequestBody RequestDetails request) throws ExistentException {
        requestService.addRequest(request);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    // ---------------------------------------READ OPERATIONS---------------------------------------

    //ADMIN - CLIENT (OK)
    @GetMapping("")
    public ResponseEntity<List<RequestDetails>> getRequests(Authentication auth) {
        if(UserController.hasAuthority(auth, "ADMIN")) {
            return new ResponseEntity<>(requestService.getRequestsAdmin(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(requestService.getRequestsClient(auth.getName()), HttpStatus.OK);
        }
    }

    //ADMIN - CLIENT (OK)
    @GetMapping("/{id}")
    public ResponseEntity<RequestDetails> getRequest(@PathVariable("id") long id) {
        return new ResponseEntity<>(requestService.getRequestById(id), HttpStatus.OK);
    }

    // ---------------------------------------UPDATE OPERATIONS---------------------------------------

    //CLIENT - Modify BusinessJustification and/or Quarter (OK)
    //ADMIN can modify all
    @PutMapping("")
    public ResponseEntity<RequestDetails> updateRequest(@RequestBody RequestDetails request){
        RequestDetails req=requestService.updateRequest(request);
        return new ResponseEntity<>(req,HttpStatus.OK);
    }

    //ADMIN (OK)
    @PutMapping("/{quarter}/{name}")
    public ResponseEntity<List<RequestDetails>> approveRequestFilterList(@PathVariable("quarter") String quarter,
                                                   @PathVariable("name") String participantName) {
        List<RequestDetails> requests=requestService.approveRequestFilterList(quarter,participantName);
        if(requests.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    // ---------------------------------------DELETE OPERATIONS---------------------------------------

    //CLIENT-ADMIN (OK)
    @PutMapping("/delete")
    public ResponseEntity<Request> findAndDeleteRequest(@RequestBody  RequestDetails request){
        requestService.deleteRequest(request);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}
