package com.ibm.certificationsserver.service;

import com.ibm.certificationsserver.model.Certification;
import com.ibm.certificationsserver.model.CertificationFilter;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.persistence.CertificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CertificationService {

    @Autowired
    private CertificationRepository certificationRepository;

    public Certification addCertification(Certification certification){
        return certificationRepository.addCertification(certification);
    }

    public List<Certification> queryCertifications(){
        List<Certification> certifications=certificationRepository.queryCertifications();
        return certifications;
    }

    public Certification queryCertification(long id) {
        Certification certification = certificationRepository.queryCertification(id);
        return certification;
    }

    public List<RequestDetails> queryCertificationsWithFilter(CertificationFilter certificationFilter){
        return certificationRepository.queryCertificationsWithFilter(certificationFilter);
    }

    public Certification updateCertification(Certification newCertification) {
        Certification certification = certificationRepository.updateCertification(newCertification);
        return certification;
    }

    public void deleteCertification(long id) {
        certificationRepository.deleteCertification(id);
    }
}
