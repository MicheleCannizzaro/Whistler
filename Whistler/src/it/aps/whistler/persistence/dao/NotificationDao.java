package it.aps.whistler.persistence.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.TypedQuery;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.aps.whistler.domain.Notification;
import it.aps.whistler.util.HibernateUtil;

public class NotificationDao {
	private final static Logger logger = Logger.getLogger(NotificationDao.class.getName());
	
	private static NotificationDao instance;
	
	//Singleton pattern created by static method - Lazy Initialization
	public static synchronized NotificationDao getInstance() {
		if (instance == null) { 
			instance = new NotificationDao();
		}
		return instance;
	}
	
	public void saveNotification(Notification notification) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
		try {
			
			transaction = session.beginTransaction();
			
			session.save(notification);
			
			transaction.commit();
        
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, NotificationDao.class.getSimpleName(),"saveNotification","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, NotificationDao.class.getSimpleName(),"saveNotification","Transaction Rollback");
            }
        } finally {
     	   session.close();
        }
	}	
	
	public void updateNotification(Notification notification) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// update notification object
			session.saveOrUpdate(notification);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, NotificationDao.class.getSimpleName(),"updateNotification","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, NotificationDao.class.getSimpleName(),"updateNotification","Transaction Rollback");
            }
        } finally {
      	   session.close();
         }
	}
	
	public boolean deleteNotification(String nid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		Notification notification = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// retrieve notification object by nid
			notification = session.get(Notification.class, nid);
			
			// remove notification object
			session.delete(notification);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, NotificationDao.class.getSimpleName(),"deleteNotification","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, NotificationDao.class.getSimpleName(),"deleteNotification","Transaction Rollback");
            	
            }
            return false;
        } finally {
       	   session.close();
          }
		return true;
	}
	
	public Notification getNotificationByNid(String nid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		Notification notification = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// get notification object by nid
			notification = session.get(Notification.class, nid);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, NotificationDao.class.getSimpleName(),"getNotificationByNid","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, NotificationDao.class.getSimpleName(),"getNotificationByNid","Transaction Rollback");
            }
        } finally {
        	   session.close();
           }
		return notification;
	}
	
	public Set<Notification> getAllNotificationFromNickname(String nickname) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
	    
	    Set<Notification> notifications = new HashSet<>();
	    
	    try {
	      // start the transaction 
	      transaction = session.beginTransaction();
	      																		//it uses Hibernate SQL language, not native SQL
	      TypedQuery<Notification> query = session.createQuery("FROM Notification n LEFT JOIN FETCH n.account WHERE n.account.nickname = :nickname", Notification.class);
	      query.setParameter("nickname", nickname);
	      
	      List<Notification> notificationList = query.getResultList();
	      notifications.addAll(notificationList);
	      
	      // commit transaction
	      transaction.commit();
	      
	    }catch(HibernateException ex) {
	      logger.logp(Level.SEVERE, NotificationDao.class.getSimpleName(),"getAllNotificationFromNickname","HibernateException: "+ex);
	      
	            if (transaction!=null) {
	              transaction.rollback();
	              
	              logger.logp(Level.SEVERE, NotificationDao.class.getSimpleName(),"getAllNotificationFromNickname","Transaction Rollback");
	            }
	        } finally {
	          session.close();
	        }
	    return notifications;
	}
}