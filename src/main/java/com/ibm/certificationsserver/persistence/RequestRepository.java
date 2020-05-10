package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.model.RequestDetails;

import java.util.List;

public interface RequestRepository {
    RequestDetails addRequest(RequestDetails request);
    RequestDetails updateRequest(RequestDetails request);
    void deleteRequest(RequestDetails requestDetails);
    List<RequestDetails> approveRequestFilterList(String quarter,String name);
    RequestDetails getRequestById(long id);
    List<RequestDetails> getRequestsAdmin();
    List<RequestDetails> getRequestsClient(String name);
}
