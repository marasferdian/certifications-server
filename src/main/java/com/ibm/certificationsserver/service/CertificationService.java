package com.ibm.certificationsserver.service;

import com.ibm.certificationsserver.exceptions.ExistentException;
import com.ibm.certificationsserver.model.Certification;
import com.ibm.certificationsserver.model.CertificationFilter;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.model.Status;
import com.ibm.certificationsserver.persistence.CertificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CertificationService {

    @Autowired
    private CertificationRepository certificationRepository;

    public Certification addCertification(Certification certification) throws ExistentException {
        try {
            return certificationRepository.addCertification(certification);
        }catch (DataIntegrityViolationException e){
            throw new ExistentException();
        }
    }
    public List<Certification> queryCertifications(){
        List<Certification> certifications=certificationRepository.queryCertifications();
        return certifications;
    }

    public Certification queryCertification(long id) {
        Certification certification = certificationRepository.queryCertification(id);
        return certification;
    }

    public List<Certification> queryCustomCertification(){
        return certificationRepository.queryCustomCertification();
    }

    public List<RequestDetails> queryCertificationsWithFilter(CertificationFilter certificationFilter,Long id){
        return certificationRepository.queryCertificationsWithFilter(certificationFilter,id);
    }

    public Certification updateCertification(Certification newCertification) {
        Certification certification = certificationRepository.updateCertification(newCertification);
        return certification;
    }

    public void deleteCertification(long id) {
        certificationRepository.deleteCertification(id);
    }

    public Certification addPendingCertification(Certification customCertification) throws ExistentException {
        try {
            return certificationRepository.addPendingCertification(customCertification);
        }catch (DataIntegrityViolationException e){
            throw new ExistentException();
        }
    }

    public Certification approveOrRejectCustomCertification(Certification certif, Status status) {
        return certificationRepository.approveOrRejectCustomCertification(certif,status);
    }
}
