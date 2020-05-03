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


    private Request convertRequestDetailsToRequest(RequestDetails request, Session session) {
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

    @Override
    @Transactional
    public RequestDetails addRequest(RequestDetails request) {
        Session session=sessionFactory.getCurrentSession();
        Request req = convertRequestDetailsToRequest(request, session);
        session.save(req);
        return request;
    }

    @Override
    @Transactional
    public RequestDetails updateRequest(RequestDetails request) {
        Session session=sessionFactory.getCurrentSession();
        Request req=convertRequestDetailsToRequest(request,session);
        Query<Request> query=session.createQuery("FROM Request where idUser=:u AND idCertificate=:c");
        query.setParameter("u",req.getIdUser());
        query.setParameter("c",req.getIdCertificate());
        Request updated=query.getSingleResult();
        updated.updateRequest(req);
        session.update(updated);
        return request;
    }

    @Override
    @Transactional
    public void deleteRequest(long id) {
        Session session=sessionFactory.getCurrentSession();
        Request r=session.get(Request.class, id);
        session.delete(r);
    }

    @Override
    @Transactional
    public List<RequestDetails> approveRequestFilterList(String quarter, String name) {
        Session session=sessionFactory.getCurrentSession();
        Query<Request> query=session.createQuery("FROM Request WHERE quarter=:q");
        query.setParameter("q", Quarter.valueOf(quarter));
        List<Request> requests=query.list();
        List<RequestDetails> approve=new ArrayList<>();
        for(Request r:requests){
            User u=session.get(User.class,r.getIdUser());
            Certification c=session.get(Certification.class,r.getIdCertificate());
            if(u.getUsername().equals(name) && r.getStatus()==Status.PENDING) {
                r.setStatus(Status.APPROVED);
                populateList(approve,r,u,c);
            }
        }
        return approve;
    }

    private void populateList(List<RequestDetails> details,Request request, User user, Certification certification){
        RequestDetails detail=new RequestDetails(request.getQuarter(),user.getUsername(),certification.getTitle(),
                certification.getCategory(),request.getStatus(),certification.getCost(),request.getBusinessJustification());
        details.add(detail);
    }

    private void createRequestDetailsList(Session session, List<Request> requestList, List<RequestDetails> requestDetails) {
        for(Request r : requestList) {
            User u=session.get(User.class,r.getIdUser());
            Certification c=session.get(Certification.class,r.getIdCertificate());
            populateList(requestDetails,r,u,c);
        }
    }

    @Override
    @Transactional
    public List<RequestDetails> getRequestsAdmin() {
        Session session = sessionFactory.getCurrentSession();
        List<Request> requests = null;
        Query<Request> query = session.createQuery("FROM Request");
        requests = query.list();

        List<RequestDetails> requestDetails = new ArrayList<>();
        createRequestDetailsList(session, requests, requestDetails);

        return requestDetails;
    }

    @Override
    @Transactional
    public List<RequestDetails> getRequestsClient(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("FROM User WHERE username=:name");
        query.setParameter("name", name);
        User user = query.getSingleResult();
        long userId = user.getId();

        Query<Request> requestQuery = session.createQuery("FROM Request WHERE idUser=:userId");
        requestQuery.setParameter("userId", userId);
        List<Request> requestList = requestQuery.list();

        List<RequestDetails> requestDetails = new ArrayList<>();
        createRequestDetailsList(session, requestList, requestDetails);

        return requestDetails;
    }

    @Override
    @Transactional
    public RequestDetails getRequestById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Request request = session.get(Request.class, id);

        User user=session.get(User.class,request.getIdUser());
        Certification certification=session.get(Certification.class,request.getIdCertificate());

        RequestDetails detail=new RequestDetails(request.getQuarter(),user.getUsername(),certification.getTitle(),
                certification.getCategory(),request.getStatus(),certification.getCost(),request.getBusinessJustification());
        return detail;
    }
}
