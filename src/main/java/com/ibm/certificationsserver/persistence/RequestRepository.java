package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.model.Request;

import java.util.List;

public interface RequestRepository {
    Request addRequest(Request request);
    Request updateRequest(Request request);
    void deleteRequest(long userId,long certificationId);
    List<Request> approveRequestFilterList(String quarter,String name);
}
