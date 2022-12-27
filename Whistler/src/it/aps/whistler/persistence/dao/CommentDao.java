package it.aps.whistler.persistence.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Comment;
import it.aps.whistler.domain.Post;
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
			
			// get comment object by word
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
	
	@SuppressWarnings("unchecked")
	public ArrayList<Comment> getAllCommentsFromPost(String pid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		
		List<Object[]> commentFieldList = null;
		ArrayList<Comment> comments = new ArrayList<>();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		
		try {
			// start the transaction 
			transaction = session.beginTransaction();
			
			// get all comments in a list and put it in an HashSet
			commentFieldList = session.createNativeQuery("select * from comment where pid=\'"+pid+"\'").list();  //it uses native SQL language for query
			Set<Object[]> fields = new HashSet<>(commentFieldList); 
			
			Post p = PostDao.getInstance().getPostByPid(pid);
			
			for(Object[] field: fields) {
				Comment c=new Comment();
				c.setCid(field[0].toString());
				c.setBody(field[1].toString());
				c.setCommentVisibility(Visibility.values()[(int)field[2]]);
				LocalDateTime t = LocalDateTime.parse(field[3].toString(), formatter);
				c.setTimestamp(t);
				c.setOwner(field[4].toString());
				c.setPost(p);
		
				comments.add(c);	
			}
			
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