package it.aps.whistler.persistence.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.aps.whistler.domain.Keyword;
import it.aps.whistler.util.HibernateUtil;

public class KeywordDao {
	private final static Logger logger = Logger.getLogger(KeywordDao.class.getName());
	
	private static KeywordDao instance;
	
	//Singleton pattern created by static method - Lazy Initialization
	public static synchronized KeywordDao getInstance() {
		if (instance == null) { 
			instance = new KeywordDao();
		}
		return instance;
	}
	
	public void saveKeyword(Keyword keyword) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
		try {
			
			transaction = session.beginTransaction();
			
			session.save(keyword);
			
			transaction.commit();
        
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, KeywordDao.class.getSimpleName(),"saveKeyword","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, KeywordDao.class.getSimpleName(),"saveKeyword","Transaction Rollback");
            }
        } finally {
     	   session.close();
        }
	}	
	
	public void updateKeyword(Keyword keyword) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// update keyword object
			session.saveOrUpdate(keyword);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, KeywordDao.class.getSimpleName(),"updateKeyword","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, KeywordDao.class.getSimpleName(),"updateKeyword","Transaction Rollback");
            }
        } finally {
      	   session.close();
         }
	}
	
	public void deleteKeyword(String word) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		Keyword keyword = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// retrieve keyword object by word
			keyword = session.get(Keyword.class, word);
			// remove keyword object
			session.delete(keyword);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, KeywordDao.class.getSimpleName(),"deleteKeyword","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, KeywordDao.class.getSimpleName(),"deleteKeyword","Transaction Rollback");
            }
        } finally {
       	   session.close();
          }
	}
	
	public Keyword getKeywordByWord(String word) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		Keyword keyword = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// get keyword object by word
			keyword = session.get(Keyword.class, word);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, KeywordDao.class.getSimpleName(),"getKeywordByWord","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, KeywordDao.class.getSimpleName(),"getKeywordByWord","Transaction Rollback");
            }
        } finally {
        	   session.close();
           }
		return keyword;
	}
	
	@SuppressWarnings("unchecked")
	public Set<Keyword> getAllWhistlerKeywords() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		
		List<Keyword> keywordsList = null;
		Set<Keyword> keywords = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// get all keyword in a list and put it in an HashSet
			keywordsList = session.createQuery("from Keyword").list();  //it uses Hibernate SQL language, not native SQL
			keywords = new HashSet<>(keywordsList);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, KeywordDao.class.getSimpleName(),"getAllWhistlerKeywords","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, KeywordDao.class.getSimpleName(),"getAllWhistlerKeywords","Transaction Rollback");
            }
        } finally {
     	   session.close();
        }
		return keywords;
	}
}