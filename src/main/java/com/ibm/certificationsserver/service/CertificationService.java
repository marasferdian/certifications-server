package com.ibm.certificationsserver.service;

import com.ibm.certificationsserver.exceptions.NotFoundException;
import com.ibm.certificationsserver.model.Certification;
import com.ibm.certificationsserver.model.CertificationFilter;
import com.ibm.certificationsserver.model.Request;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.persistence.CertificationRepository;
import com.ibm.certificationsserver.persistence.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificationService {

    @Autowired
    private CertificationRepository certificationRepository;
    @Autowired
    private RequestRepository requestRepository;

    public Certification addCertification(Certification certification){
        return certificationRepository.addCertification(certification);
    }

    public List<Certification> queryCertifications(){
        List<Certification> certifications=certificationRepository.queryCertifications();
        return certifications;
    }

    public List<RequestDetails> queryCertificationsWithFilter(CertificationFilter certificationFilter){
        return certificationRepository.queryCertificationsWithFilter(certificationFilter);
    }

    public Request addRequest(Request request){
        return requestRepository.addRequest(request);
    }

    public Request updateRequest(Request request){
        return requestRepository.updateRequest(request);
    }

    public void deleteRequest(long userId,long certificationId){
        requestRepository.deleteRequest(userId,certificationId);
    }

    public List<Request> approveRequestFilterList(String quarter,String name){
        return requestRepository.approveRequestFilterList(quarter,name);
    }
}
