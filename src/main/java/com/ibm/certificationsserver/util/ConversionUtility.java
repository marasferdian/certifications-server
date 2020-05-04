package com.ibm.certificationsserver.util;

import com.ibm.certificationsserver.model.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConversionUtility {

    public Request convertRequestDetailsToRequest(RequestDetails request, Session session) {
        Request req=new Request();
        req.setQuarter(request.getQuarter());
        req.setStatus(request.getStatus());
        req.setBusinessJustification(request.getBusinessJustification());
        Query<User> q1=session.createQuery("FROM User where username=:u");
        q1.setParameter("u",request.getParticipantName());
        User user=q1.getSingleResult();
        req.setIdUser(user.getId());
        Query<Certification> q2=session.createQuery("FROM Certification where title=:t");
        q2.setParameter("t",request.getCertificationTitle());
        Certification certification=q2.getSingleResult();
        req.setIdCertificate(certification.getId());
        return req;
    }

    public PendingCertifications convertCertificationToPendingCertification(Certification certification){

        PendingCertifications pendingCertifications = new PendingCertifications();
        pendingCertifications.setId(certification.getId());
        pendingCertifications.setTitle(certification.getTitle());
        pendingCertifications.setCategory(certification.getCategory().toString());
        pendingCertifications.setCost(certification.getCost());
        return pendingCertifications;
    }


    public void populateList(List<RequestDetails> details,Request request, User user, Certification certification){
        RequestDetails detail=new RequestDetails(request.getQuarter(),user.getUsername(),certification.getTitle(),
                certification.getCategory(),request.getStatus(),certification.getCost(),request.getBusinessJustification());
        details.add(detail);
    }
}
