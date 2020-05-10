package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.model.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ibm.certificationsserver.util.ConversionUtility;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CertificationRepositoryImp implements CertificationRepository {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private ConversionUtility conversionUtility;

    @Override
    @Transactional
    public Certification addCertification(Certification certification) {
          Session session = sessionFactory.getCurrentSession();
          session.save(certification);
          return certification;
    }

    @Override
    @Transactional
    public List<Certification> queryCertifications() {
        Session session=sessionFactory.getCurrentSession();
        Query<Certification> query=session.createQuery("from Certification");
        List<Certification> certifications=query.list();
        return certifications;
    }

    private void addRestrictionIfNotNull(List<Predicate> predicateList, Root root, CriteriaBuilder criteriaBuilder, CriteriaQuery<Request> criteria, String propertyName, Object value) {
        if (value != null) {
            predicateList.add(criteriaBuilder.equal(root.get(propertyName), value));
        }
    }
    @Override
    @Transactional
    public List<RequestDetails> queryCertificationsWithFilter(CertificationFilter certificationFilter,Long id) {
        final CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        Session session = sessionFactory.getCurrentSession();
        CriteriaQuery<Request> crit = criteriaBuilder.createQuery(Request.class);
        Root<Request> root = crit.from(Request.class);
        crit.select(root);
        List<Predicate> predicateList = new ArrayList<>();
        addRestrictionIfNotNull(predicateList,root,criteriaBuilder, crit, "status", certificationFilter.getStatus());
        addRestrictionIfNotNull(predicateList,root,criteriaBuilder, crit, "quarter", certificationFilter.getQuarter());
        addRestrictionIfNotNull(predicateList,root,criteriaBuilder,crit,"idUser",id);
        if(predicateList.size()!=0){
            Predicate pred = predicateList.get(0);
            for(int i=1 ; i < predicateList.size() ; i++){

                pred = criteriaBuilder.and(predicateList.get(i),pred);
            }
            crit.where(pred);
        }
        List<Request> requests = sessionFactory.createEntityManager().createQuery(crit).getResultList();
        List<RequestDetails> details = new ArrayList<>();
        for (Request request : requests) {
            User user = session.get(User.class, request.getIdUser());
            Certification certification = session.get(Certification.class, request.getIdCertificate());
            conversionUtility.populateList(details, request, user, certification);
        }
        return details;
    }

    @Override
    @Transactional
    public Certification queryCertification(long id) {
        Session session = sessionFactory.getCurrentSession();
        Certification certification = session.get(Certification.class, id);
        return certification;
    }

    @Override
    @Transactional
    public Certification updateCertification(Certification newCertification) {
        Session session = sessionFactory.getCurrentSession();
        session.update(newCertification);
        return newCertification;
    }

    @Override
    @Transactional
    public void deleteCertification(long id) {
        Session session = sessionFactory.getCurrentSession();
        Certification certification = session.get(Certification.class, id);
        Query<Request> requestQuery = session.createQuery("DELETE FROM Request WHERE idCertificate=:id");
        requestQuery.setParameter("id", id);
        requestQuery.executeUpdate();
        session.delete(certification);
    }

    @Override
    @Transactional
    public Certification addPendingCertification(Certification customCertification) {
        Session session = sessionFactory.getCurrentSession();
        Query<Certification> query = session.createQuery("FROM Certification WHERE title=:customTitle AND category=:customCategory");
        query.setParameter("customTitle",customCertification.getTitle());
        query.setParameter("customCategory",customCertification.getCategory());
        List<Certification> certifications = query.list();
        if(certifications.isEmpty()) {
            PendingCertifications pendingCertifications = conversionUtility.convertCertificationToPendingCertification(customCertification);
            pendingCertifications.setId(null);
            double cost = pendingCertifications.getCost();
            cost = Double.parseDouble(new DecimalFormat("#.##").format(cost));
            pendingCertifications.setCost(cost);
            session.save(pendingCertifications);
            customCertification.setId(pendingCertifications.getId());
            customCertification.setCost(cost);
        }
        return customCertification;
    }

    @Override
    @Transactional
    public List<Certification> queryCustomCertification() {
        Session session=sessionFactory.getCurrentSession();
        List<PendingCertifications> pending=session.createQuery("FROM PendingCertifications").list();
        List<Certification> certifications=new ArrayList<>();
        pending.forEach((pend -> {
            Certification certifi=conversionUtility.convertPendingCertificationToCertification(pend);
            certifications.add(certifi);
        }));
        return certifications;
    }

    @Override
    @Transactional
    public Certification approveOrRejectCustomCertification(Certification certification, Status status) {
        Session session=sessionFactory.getCurrentSession();
        PendingCertifications pending=conversionUtility.convertCertificationToPendingCertification(certification);
        session.delete(pending);
        if(status==Status.APPROVED){
            session.save(certification);
        }
        return certification;
    }
}
