package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.model.Request;
import com.ibm.certificationsserver.model.RequestDetails;

import java.util.List;

public interface RequestRepository {
    Request addRequest(Request request);
    Request updateRequest(Request request);
    void deleteRequest(long id);
    List<Request> approveRequestFilterList(String quarter,String name);
    RequestDetails getRequestById(long id);
    List<RequestDetails> getRequestsAdmin();
    List<RequestDetails> getRequestsClient(String name);
}
