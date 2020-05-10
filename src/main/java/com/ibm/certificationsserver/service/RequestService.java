package com.ibm.certificationsserver.service;

import com.ibm.certificationsserver.exceptions.ExistentException;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.persistence.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;

    public RequestDetails addRequest(RequestDetails request) throws ExistentException {
        try {
            return requestRepository.addRequest(request);
        }catch (DataIntegrityViolationException e){
            throw new ExistentException();
        }

    }

    public RequestDetails updateRequest(RequestDetails request){
        return requestRepository.updateRequest(request);
    }

    public void deleteRequest(RequestDetails requestDetails){
        requestRepository.deleteRequest(requestDetails);
    }

    public List<RequestDetails> approveRequestFilterList(String quarter, String name){
        return requestRepository.approveRequestFilterList(quarter,name);
    }

    public List<RequestDetails> getRequestsAdmin() {
        return requestRepository.getRequestsAdmin();
    }

    public List<RequestDetails> getRequestsClient(String name) {
        return requestRepository.getRequestsClient(name);
    }

    public RequestDetails getRequestById(long id) {
        return requestRepository.getRequestById(id);
    }
}
