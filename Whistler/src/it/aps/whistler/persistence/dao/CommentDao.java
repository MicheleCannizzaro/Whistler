package it.aps.whistler.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.TypedQuery;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.aps.whistler.domain.Comment;
import it.aps.whistler.util.HibernateUtil;

public class CommentDao {
	private final static Logger logger = Logger.getLogger(CommentDao.class.getName());
	
	private static CommentDao instance;
	
	//Singleton pattern created by static method - Lazy Initialization
	public static synchronized CommentDao getInstance() {
		if (instance == null) { 
			instance = new CommentDao();
		}
		return instance;
	}
	
	public void saveComment(Comment comment) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
		try {
			
			transaction = session.beginTransaction();
			
			session.save(comment);
			
			transaction.commit();
        
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, CommentDao.class.getSimpleName(),"saveComment","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, CommentDao.class.getSimpleName(),"saveComment","Transaction Rollback");
            }
        } finally {
     	   session.close();
        }
	}	
	
	public void updateComment(Comment comment) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// update comment object
			session.saveOrUpdate(comment);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, CommentDao.class.getSimpleName(),"updateComment","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, CommentDao.class.getSimpleName(),"updateComment","Transaction Rollback");
            }
        } finally {
      	   session.close();
         }
	}
	
	public void deleteComment(String cid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		Comment comment = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// retrieve comment object by cid
			comment = session.get(Comment.class, cid);
			
			// remove comment object
			session.delete(comment);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, CommentDao.class.getSimpleName(),"deleteComment","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, CommentDao.class.getSimpleName(),"deleteComment","Transaction Rollback");
            }
        } finally {
       	   session.close();
          }
	}
	
	public Comment getCommentByCid(String cid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		Comment comment = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// get comment object by cid
			comment = session.get(Comment.class, cid);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, CommentDao.class.getSimpleName(),"getCommentByCid","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, CommentDao.class.getSimpleName(),"getCommentByCid","Transaction Rollback");
            }
        } finally {
        	   session.close();
           }
		return comment;
	}
	
	public ArrayList<Comment> getAllCommentsFromPost(String pid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		
		ArrayList<Comment> comments = null;
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
																		//it uses Hibernate SQL language, not native SQL
			TypedQuery<Comment> query = session.createQuery("FROM Comment c LEFT JOIN FETCH c.post WHERE c.post.pid = :pid", Comment.class);
		    query.setParameter("pid", pid);
		      
		    List<Comment> commentList = query.getResultList(); 
			comments = new ArrayList<>(commentList);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException ex) {
			logger.logp(Level.SEVERE, CommentDao.class.getSimpleName(),"getAllCommentsFromPost","HibernateException: "+ex);
			
            if (transaction!=null) {
            	transaction.rollback();
            	
            	logger.logp(Level.SEVERE, CommentDao.class.getSimpleName(),"getAllCommentsFromPost","Transaction Rollback");
            }
        } finally {
     	   session.close();
        }
		return comments;
	}
}