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
        Transaction transaction = null;
        // try with resource statement auto-close session and shutdown HibernateUtil - in this way we can avoid finally
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			
			transaction = session.beginTransaction();
			
			session.save(account);
			
			transaction.commit();
        
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
        }
	}	
	
	public void updateAccount(Account account) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
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
        }
	}
	
	public void deleteAccount(String nickname) {
		Transaction transaction = null;
		Account account = null;
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
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
        }
	}
	
	public Account getAccountByNickname(String nickname) {
		Transaction transaction = null;
		Account account = null;
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
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
        }
		return account;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Account> getAllWhistlerAccounts() {
		Transaction transaction = null;
		List<Account> accountsList = null;
		ArrayList<Account> accounts = null;
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			// start the transaction 
			transaction = session.beginTransaction();
			
			// get all account in a list and and put it in an ArrayList
			accountsList = session.createQuery("from Account").list();  //it uses hibernate sql languages, not sql
			accounts = new ArrayList<>(accountsList);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
        }
		return accounts;
	}
}