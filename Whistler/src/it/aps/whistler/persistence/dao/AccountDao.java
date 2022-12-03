package it.aps.whistler.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.aps.whistler.domain.Account;
import it.aps.whistler.util.HibernateUtil;

public class AccountDao {
	
	private static AccountDao instance;
	
	//Singleton pattern created by static method - Lazy Initialization
	public static synchronized AccountDao getInstance() {
		if (instance == null) { 
			instance = new AccountDao();
		}
		return instance;
	}
	
	public void saveAccount(Account account) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
		try {
			
			transaction = session.beginTransaction();
			
			session.save(account);
			
			transaction.commit();
        
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
        } finally {
        	   session.close();
        }
	}	
	
	public void updateAccount(Account account) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// update account object
			session.saveOrUpdate(account);
			
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
	
	public void deleteAccount(String nickname) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		Account account = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// retrieve account object by nickname
			account = session.get(Account.class, nickname);
			// remove account object
			session.delete(account);
			
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
	
	public Account getAccountByNickname(String nickname) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		Account account = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// get account object by nickname
			account = session.get(Account.class, nickname);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
        } finally {
       	   session.close();
          }
		return account;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Account> getAllWhistlerAccounts() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		
		List<Account> accountsList = null;
		ArrayList<Account> accounts = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// get all account in a list and put it in an ArrayList
			accountsList = session.createQuery("from Account").list();  //it uses Hibernate SQL language, not native SQL
			accounts = new ArrayList<>(accountsList);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
        } finally {
        	   session.close();
           }
		return accounts;
	}
}