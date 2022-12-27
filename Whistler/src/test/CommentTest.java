package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Comment;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.persistence.dao.CommentDao;
import it.aps.whistler.persistence.dao.KeywordDao;

class CommentTest {

	@Nested class InitializedTests{
		@BeforeEach
		public void init() {
			Whistler w = Whistler.getInstance();
			w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
			w.signUp("@alanturing", "Alan", "Turing", "alanturing@gmail.com", "ciaociao22");
		}
	
		
		@Test
		void testConfirmCommand() { //CF
			 Whistler w = Whistler.getInstance();
			 
			 Account fakeAccount = w.getAccount("@elonmsk"); 
			 
			 fakeAccount.enterNewPost("title", "body"); 
			 String postPid = fakeAccount.getCurrePostPid();
			 
			 fakeAccount.addPostKeyword("#Keyword1");
			 fakeAccount.addPostKeyword("#Keyword2"); fakeAccount.setPostOwner();
			 fakeAccount.setPostVisibility(Visibility.PUBLIC); fakeAccount.confirmPost();
			 
			 fakeAccount.enterNewComment(postPid,"body");
			 fakeAccount.setCommentOwner();
			 fakeAccount.confirmComment();
			 
			 
			 String expectedBody = "body";
				
			 boolean isCidPresent=false;
			 ArrayList<Comment> postComments = CommentDao.getInstance().getAllCommentsFromPost(postPid);
			
			 for (Comment comment : postComments) {
			 	 if (comment.getBody().equals(expectedBody) && comment.getOwner().equals("@elonmsk")) {
					 if (comment.getCid()!=null) {
						 isCidPresent = true;
					 }
				 } 
			 }
			 
			 assertTrue(isCidPresent);
		}
		
		@Test
		void testRemoveComment_owner() { //CE //CF
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
			Account fakeAccount1 = w.getAccount("@alanturing");
			
			fakeAccount.enterNewPost("title", "body"); 
			String postPid = fakeAccount.getCurrePostPid();
			 
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2"); fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC); fakeAccount.confirmPost();
			 
			fakeAccount1.enterNewComment(postPid,"body");
			String commentCid = fakeAccount1.getCurrentComment().getCid();
			fakeAccount1.setCommentOwner();
			fakeAccount1.confirmComment();	
			
			boolean isCommentPresent = false;
			if (w.getComment(commentCid)!=null) isCommentPresent=true;
			
			fakeAccount1.removeComment(commentCid); //comment's owner is fakeAccount1
			
			assertEquals(isCommentPresent==true,w.getComment(commentCid)==null); //fakeAccount1 can delete the comment
			
		}
		
		@Test
		void testRemoveComment_notOwner() { //CE //CF
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
			Account fakeAccount1 = w.getAccount("@alanturing");
			
			fakeAccount.enterNewPost("title", "body"); 
			String postPid = fakeAccount.getCurrePostPid();
			 
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2");
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC); 
			fakeAccount.confirmPost();
			 
			fakeAccount1.enterNewComment(postPid,"body");
			String commentCid = fakeAccount1.getCurrentComment().getCid();
			fakeAccount1.setCommentOwner();
			fakeAccount1.confirmComment();	
			
			boolean isCommentPresent = false;
			if (w.getComment(commentCid)!=null) isCommentPresent=true;
			
			fakeAccount.removeComment(commentCid); //comment's owner is fakeAccount1 not fakeAccount
			
			assertEquals(isCommentPresent==true,w.getComment(commentCid)!=null); //fakeAccount can't delete comment
			
		}
		
	}

	@AfterEach
	public void cleanUp() {
		Whistler w = Whistler.getInstance();
		Account fakeAccount1 = w.getAccount("@elonmsk");
		Account fakeAccount2 = w.getAccount("@alanturing");
		
		//removing fake account from whistler_db and cache
		if (w.getWhistlerAccounts().contains(fakeAccount1)) {
			w.removeAccount("@elonmsk");
		}
		if (w.getWhistlerAccounts().contains(fakeAccount2)) {
			w.removeAccount("@alanturing");
		}
	}
	
	@AfterAll
	  public static void cleanUpKeywords() {
		String[] keywords = {"#Keyword1","#Keyword2"};
		  for (String key : keywords) {
			  KeywordDao.getInstance().deleteKeyword(key);
		  }
	  }

}
