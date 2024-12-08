package com.example.AreaChecker.model.dao;

import com.example.AreaChecker.model.entity.Shot;
import com.example.AreaChecker.model.entity.User;
import com.example.AreaChecker.util.HibernateUtil;
import jakarta.ejb.TransactionManagement;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.ejb.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
@TransactionManagement(jakarta.ejb.TransactionManagementType.BEAN)
public class ShotDao {
    public void addShot(Shot shot) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer shotId = null;

        try {
            transaction = session.beginTransaction();
            shotId = (Integer) session.save(shot);  // Save and get the generated ID
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();  // Rollback the transaction in case of error
            }
            // You can log the error or throw an application-specific exception
        } finally {
            session.close();  // Ensure session is closed in a finally block
        }

        shot.setId(shotId);  // Return the ID of the saved shot
    }

    public List<Shot> getShots(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Shot> list = session
                .createQuery("select x from Shot x where x.user = :user", Shot.class)
                .setParameter("user", user)
                .getResultList();
        return (list != null) ? list : new ArrayList<>();
    }

    public void deleteShots(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("delete from Shot x where x.user = :user")
                .setParameter("user", user)
                .executeUpdate();
        transaction.commit();
        session.close();
    }


}
