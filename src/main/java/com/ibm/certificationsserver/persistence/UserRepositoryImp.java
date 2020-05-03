package com.ibm.certificationsserver.persistence;

import com.ibm.certificationsserver.exceptions.NotFoundException;
import com.ibm.certificationsserver.model.User;
import com.ibm.certificationsserver.util.CustomPasswordEncoder;
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
        if(user.size()==0) {
            return null;
        }
        return user.get(0);
    }

    @Override
    @Transactional
    public User getUser(Long id){
        Session session=sessionFactory.getCurrentSession();
        User user=session.get(User.class,id);
        if(user==null) {
            throw new NotFoundException();
        }
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

    @Override
    @Transactional
    public void deleteUser(long id) {

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("DELETE FROM User WHERE id=:id");
        query.setParameter("id",id);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void updateUserPassword(long id, String pass) {
        Session session = sessionFactory.getCurrentSession();
        CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
        String cryptedPassword = customPasswordEncoder.encode(pass);
        Query query = session.createQuery("UPDATE User SET password =: cryptedPassword WHERE id=:id");
        query.setParameter("cryptedPassword",cryptedPassword);
        query.setParameter("id",id);
        query.executeUpdate();
    }
}
