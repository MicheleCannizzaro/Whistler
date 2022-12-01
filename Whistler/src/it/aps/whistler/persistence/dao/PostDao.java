package it.aps.whistler.persistence.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Post;
import it.aps.whistler.util.HibernateUtil;

public class PostDao {
	
	private static PostDao instance;
	
	//Singleton pattern created by static method - Lazy Initialization
	public static synchronized PostDao getInstance() {
		if (instance == null) { 
			instance = new PostDao();
		}
		return instance;
	}
	
	public boolean savePost(Post post) {
        Transaction transaction = null;
        
        // try with resource statement auto-close session and shutdown HibernateUtil - in this way we can avoid finally
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			
			transaction = session.beginTransaction();
			
			session.save(post);
			
			transaction.commit();
			return true;
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
            return false;
        }
	}	
	
	public void updatePost(Post post) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			// start the transaction 
			transaction = session.beginTransaction();
			
			// update post object
			session.merge(post);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
        }
	}
	
	public void deletePost(String pid) {
		Transaction transaction = null;
		Post post = null;
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			// start the transaction 
			transaction = session.beginTransaction();
			
			// retrieve post object by pid
			post = session.get(Post.class, pid);
			// remove post object
			session.delete(post);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
        }
	}
	
	public Post getPostByPid(String pid) {
		Transaction transaction = null;
		Post post = null;
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			// start the transaction 
			transaction = session.beginTransaction();
			
			// get post object by pid
			post = session.get(Post.class, pid);
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
        }
		return post;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<Post> getAllPostsFromOwner(String owner) {
		Transaction transaction = null;
		
		List<Object[]> postFieldList = null;
		ArrayList<Post> posts = new ArrayList();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			 //start the transaction 
			 transaction = session.beginTransaction();
			
			// get all post in a list and put it in an HashSet
			postFieldList = session.createNativeQuery("select * from post where owner =\'"+owner+"\'").list();  //it uses native SQL language for query
			Set<Object[]> fields = new HashSet<>(postFieldList); 
			
			
			for(Object[] field: fields) {
				Post p=new Post();
				p.setPid(field[0].toString());
				p.setTitle(field[1].toString());
				p.setBody(field[2].toString());
				p.setPostVisibility(Visibility.values()[(int)field[3]]);
				LocalDateTime t = LocalDateTime.parse(field[4].toString(), formatter);
				p.setTimestamp(t);
				p.setOwner(field[5].toString());
		
				posts.add(p);	
			}
			
			// commit transaction
			transaction.commit();
			
		}catch(HibernateException e) {
            if (transaction!=null) {
            	transaction.rollback();
            }
        }
		return posts;
	}

}
