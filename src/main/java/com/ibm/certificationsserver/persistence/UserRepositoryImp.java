package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.exceptions.NotFoundException;
import com.ibm.certificationsserver.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserRepositoryImp implements UserRepository{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public User findByUsername(String username) {
        Session session=sessionFactory.getCurrentSession();
        Query<User> query=session.createQuery("from User WHERE username=:user");
        query.setParameter("user",username);
        List<User> user=query.list();
        if(user.size()==0)
            return null;
        return user.get(0);
    }

    @Override
    @Transactional
    public User getUser(Long id){
        Session session=sessionFactory.getCurrentSession();
        User user=session.get(User.class,id);
        if(user==null)
            throw new NotFoundException();
        return user;
    }

    @Override
    @Transactional
    public List<User> getUsers(){
        Session session=sessionFactory.getCurrentSession();
        Query<User> query=session.createQuery("from User");
        return query.list();
    }

    @Override
    @Transactional
    public User saveUser(User user){
        Session session=sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);

        return user;
    }
}
