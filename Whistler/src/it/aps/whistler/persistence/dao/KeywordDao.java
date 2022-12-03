package it.aps.whistler.persistence.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.aps.whistler.domain.Keyword;
import it.aps.whistler.util.HibernateUtil;

public class KeywordDao {
	
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
        
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
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
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
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
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
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
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
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
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
        } finally {
     	   session.close();
        }
		return keywords;
	}
}