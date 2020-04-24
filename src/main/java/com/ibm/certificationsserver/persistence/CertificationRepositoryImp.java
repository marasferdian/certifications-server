package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.model.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CertificationRepositoryImp implements CertificationRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Certification addCertification(Certification certification) {
        Session session=sessionFactory.getCurrentSession();
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

    private void addRestrictionIfNotNull(Criteria criteria, String propertyName, Object value) {
        if (value != null) {
            criteria.add(Restrictions.eq(propertyName, value));
        }
    }

    private void populateList(List<RequestDetails> details,Request request, User user, Certification certification){
        RequestDetails detail=new RequestDetails(request.getQuarter(),user.getName(),certification.getTitle(),
                certification.getCategory(),request.getStatus(),certification.getCost(),request.getBusinessJustification());
        details.add(detail);
    }

    @Override
    @Transactional
    public List<RequestDetails> queryCertificationsWithFilter(CertificationFilter certificationFilter) {
        Session session=sessionFactory.getCurrentSession();
        Criteria criteria=session.createCriteria(Request.class);
        addRestrictionIfNotNull(criteria,"status",certificationFilter.getStatus());
        addRestrictionIfNotNull(criteria,"quarter",certificationFilter.getQuarter());
        List<Request> requests=criteria.list();
        List<RequestDetails> details=new ArrayList<>();
        for(Request r:requests) {
            User u=session.get(User.class,r.getIdUser());
            Certification c=session.get(Certification.class,r.getIdCertificate());
            populateList(details,r,u,c);
        }
        return details;
    }
}
