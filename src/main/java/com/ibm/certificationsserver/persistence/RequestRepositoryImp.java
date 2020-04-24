package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
public class RequestRepositoryImp implements RequestRepository{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Request addRequest(Request request) {
        Session session=sessionFactory.getCurrentSession();
        session.save(request);
        return request;
    }

    @Override
    @Transactional
    public Request updateRequest(Request request) {
        Session session=sessionFactory.getCurrentSession();
        session.saveOrUpdate(request);
        return request;
    }

    @Override
    @Transactional
    public void deleteRequest(long userId, long certificationId) {
        Session session=sessionFactory.getCurrentSession();
        Query<Request> query=session.createQuery("FROM Request WHERE idUser=:usr AND idCertificate=:cer");
        query.setParameter("usr",userId);
        query.setParameter("cer",certificationId);
        Request r=query.getSingleResult();
        session.delete(r);
    }

    @Override
    @Transactional
    public List<Request> approveRequestFilterList(String quarter, String name) {
        Session session=sessionFactory.getCurrentSession();
        Query<Request> query=session.createQuery("FROM Request WHERE quarter=:q");
        query.setParameter("q", Quarter.valueOf(quarter));
        List<Request> requests=query.list();
        List<Request> approve=new ArrayList<>();
        for(Request r:requests){
            User u=session.get(User.class,r.getIdUser());
            if(u.getUsername().equals(name)) {
                r.setStatus(Status.APPROVED);
                approve.add(r);
            }
        }
        return approve;
    }
}
