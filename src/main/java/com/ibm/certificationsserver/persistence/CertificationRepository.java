package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.exceptions.ExistentException;
import com.ibm.certificationsserver.model.Certification;
import com.ibm.certificationsserver.model.CertificationFilter;
import com.ibm.certificationsserver.model.RequestDetails;
import com.ibm.certificationsserver.model.Status;

import java.util.List;

public interface CertificationRepository {
    Certification addCertification(Certification certification);
    List<Certification> queryCertifications();
    List<RequestDetails> queryCertificationsWithFilter(CertificationFilter certificationFilter,Long id);
    Certification queryCertification(long id);
    Certification updateCertification(Certification newCertification);
    void deleteCertification(long id);
    Certification addPendingCertification(Certification customCertification);
    List<Certification> queryCustomCertification();
    Certification approveOrRejectCustomCertification(Certification certification, Status status);
}
