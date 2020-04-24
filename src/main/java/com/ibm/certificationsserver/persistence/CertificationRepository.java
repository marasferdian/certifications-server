package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.model.Certification;
import com.ibm.certificationsserver.model.CertificationFilter;
import com.ibm.certificationsserver.model.RequestDetails;

import java.util.List;

public interface CertificationRepository {
    Certification addCertification(Certification certification);
    List<Certification> queryCertifications();
    List<RequestDetails> queryCertificationsWithFilter(CertificationFilter certificationFilter);
}
