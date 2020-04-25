package com.ibm.certificationsserver.service;

import com.ibm.certificationsserver.model.Request;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.persistence.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;

    public Request addRequest(Request request){
        return requestRepository.addRequest(request);
    }

    public Request updateRequest(Request request){
        return requestRepository.updateRequest(request);
    }

    public void deleteRequest(long id){
        requestRepository.deleteRequest(id);
    }

    public List<Request> approveRequestFilterList(String quarter, String name){
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
