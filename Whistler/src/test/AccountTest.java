package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Comment;
import it.aps.whistler.domain.Keyword;
import it.aps.whistler.domain.Notification;
import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.persistence.dao.CommentDao;
import it.aps.whistler.persistence.dao.KeywordDao;

class AccountTest {

	@Nested class InitializedTests{
		@BeforeEach
		public void init() {
			Whistler w = Whistler.getInstance();
			w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
			w.signUp("@alanturing", "Alan", "Turing", "alanturing@gmail.com", "ciaociao22");
		}
	
		@Test
		void testFollowAccount() { //CF
			Whistler w = Whistler.getInstance();
			
			Account account1 = w.getAccount("@elonmsk");
			Account account2 = w.getAccount("@alanturing");
			
			account1.followAccount("@alanturing");
			
			assertTrue(account1.getFollowedAccounts().contains("@alanturing"));
			assertTrue(account2.getFollowers().contains("@elonmsk"));
			
		}
		
		@Test
		void testUnFollowAccount() { //CF
			Whistler w = Whistler.getInstance();
			
			Account account1 = w.getAccount("@elonmsk");
			Account account2 = w.getAccount("@alanturing");
			
			int beforeSize = account1.getFollowedAccounts().size();
			account1.followAccount("@alanturing");
			
			int sizeAfterFollow = account1.getFollowedAccounts().size();
			
			account1.unFollowAccount("@alanturing");
			
			int sizeAfterFollowAndUnFollow = account1.getFollowedAccounts().size();
			
			assertFalse(account1.getFollowedAccounts().contains("@alanturing"));
			assertFalse(account2.getFollowers().contains("@elonmsk"));
			assertEquals(1,sizeAfterFollow);
			assertEquals(beforeSize,sizeAfterFollowAndUnFollow);
		}
		
		@Test
		void testEnterNewPost_TitleLenghtMoreThanSeventy() { //CF //VE
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
												      //titlelength = 99
			boolean actual = fakeAccount.enterNewPost("Lorem ipsum dolor sit amet, consectetur adipiscing elit."
												     +" Quisque ultricies mi nec vulputate tempus.", "body");
			
			assertFalse(actual);
		}
		
		@Test
		void testEnterNewPost_TitleLenghtEqualSeventy() { //CF //VE
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
												      //titlelength = 70
			boolean actual = fakeAccount.enterNewPost("Lorem ipsum dolor sit amet, consectetur adipiscing elit."
												     +" Quisque ultri", "body");
			
			assertTrue(actual);
		}
		
		@Test
		void testEnterNewPost_TitleLenghtLessThanSeventy() { //CF //VE
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
												      //titlelength = 20
			boolean actual = fakeAccount.enterNewPost("Lorem ipsum dolor si", "body");
			
			assertTrue(actual);
		}
		
		@Test
		void testEnterNewPost_BodyLenghtMoreThanTwoHundredAndEighty() { //CF //VE
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
												                //bodylength = 320
			boolean actual = fakeAccount.enterNewPost("title", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
											                  +"Quisque ultricies mi nec vulputate tempus. Mauris lectus nisl,"
											                  +" interdum a cursus eu, interdum nec orci. Curabitur id arcu lacus."
											                  +" Donec orci leo, convallis semper tempus non, consectetur et leo. D"
											                  +"onec congue in magna ut ultricies. Aenean cursus suscipit molestie.");
			
			assertFalse(actual);
		}
		
		@Test
		void testEnterNewPost_BodyLenghtEqualTwoHundredAndEighty() { //CF //VE
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
												               //bodylength = 280
			boolean actual = fakeAccount.enterNewPost("title", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
											                  +"Quisque ultricies mi nec vulputate tempus. Mauris lectus nisl,"
											                  +" interdum a cursus eu, interdum nec orci. Curabitur id arcu lacus."
											                  +" Donec orci leo, convallis semper tempus non, consectetur et leo. D"
											                  +"onec congue in magna ut ultr");
			
			assertTrue(actual);
		}
		
		@Test
		void testEnterNewPost_BodyLessThanTwoHundredAndEighty() { //CF //VE
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
												                 //bodylength = 99
			boolean actual = fakeAccount.enterNewPost("title", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
											 				  +"Quisque ultricies mi nec vulputate tempus.");
			
			assertTrue(actual);
		}
		
		@Test
		void testEnterNewComment_BodyLenghtMoreThanTwoHundredAndEighty() { //CF //VE
			Whistler w = Whistler.getInstance();
			 
			Account fakeAccount = w.getAccount("@elonmsk"); 
			 
			fakeAccount.enterNewPost("title", "body"); 
			String postPid = fakeAccount.getCurrePostPid();
			 
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2"); 
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC);
			fakeAccount.confirmPost();
																  //bodylength = 320
			boolean actual = fakeAccount.enterNewComment(postPid,"Lorem ipsum dolor sit amet, consectetur adipiscing elit."
											                    +"Quisque ultricies mi nec vulputate tempus. Mauris lectus nisl,"
											                    +" interdum a cursus eu, interdum nec orci. Curabitur id arcu lacus."
											                    +" Donec orci leo, convallis semper tempus non, consectetur et leo. D"
											                    +"onec congue in magna ut ultricies. Aenean cursus suscipit molestie.");
			
			assertFalse(actual);
		}
		
		@Test
		void testEnterNewComment_BodyLenghtEqualTwoHundredAndEighty() { //CF //VE
			Whistler w = Whistler.getInstance();
			 
			Account fakeAccount = w.getAccount("@elonmsk"); 
			 
			fakeAccount.enterNewPost("title", "body"); 
			String postPid = fakeAccount.getCurrePostPid();
			 
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2"); 
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC); 
			fakeAccount.confirmPost();
												                  //bodylength = 280
			boolean actual = fakeAccount.enterNewComment(postPid,"Lorem ipsum dolor sit amet, consectetur adipiscing elit."
	                  											+"Quisque ultricies mi nec vulputate tempus. Mauris lectus nisl,"
	                  											+" interdum a cursus eu, interdum nec orci. Curabitur id arcu lacus."
	                  											+" Donec orci leo, convallis semper tempus non, consectetur et leo. D"
	                  											+"onec congue in magna ut ultr");
			
			assertTrue(actual);
		}
		
		@Test
		void testEnterNewComment_BodyLenghtLessThanTwoHundredAndEighty() { //CF //VE
			Whistler w = Whistler.getInstance();
			 
			Account fakeAccount = w.getAccount("@elonmsk"); 
			 
			fakeAccount.enterNewPost("title", "body"); 
			String postPid = fakeAccount.getCurrePostPid();
			 
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2"); 
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC); 
			fakeAccount.confirmPost();
												                  //bodylength = 99
			boolean actual = fakeAccount.enterNewComment(postPid,"Lorem ipsum dolor sit amet, consectetur adipiscing elit."
																+"Quisque ultricies mi nec vulputate tempus.");
			
			assertTrue(actual);
		}
		
		
		
		@Test
		void testConfirmPost() { //CF
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
				
			fakeAccount.enterNewPost("title", "body");
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2");
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC);
			fakeAccount.confirmPost();	
			
			String expectedTitle = "title";
			String expectedBody = "body";
			ArrayList<Keyword> expecetdKeywords = new ArrayList<>();
			expecetdKeywords.add(new Keyword("#Keyword1"));
			expecetdKeywords.add(new Keyword("#Keyword2"));
			
			boolean isPidPresent=false;
			
			ArrayList<Post> fakeAccountPosts=fakeAccount.getPosts();
			
			for (Post post : fakeAccountPosts) {
				if (post.getTitle().equals(expectedTitle) && post.getBody().equals(expectedBody) && post.getOwner().equals("@elonmsk")) {
					if (post.getPid()!=null) {
						isPidPresent = true;
					}
				}
			}
			
			//Keywords Set in ArrayList
			ArrayList<Keyword> keywords = new ArrayList<>(fakeAccountPosts.get(0).getPostKeywords());
			
			//Sorting Keywords Alphabetical Order
			Collections.sort(keywords, Comparator.comparing(Keyword::getWord));
			
			assertEquals(expectedTitle,fakeAccountPosts.get(0).getTitle());
			assertEquals(expectedBody,fakeAccountPosts.get(0).getBody());
			assertEquals(expecetdKeywords,keywords);
			assertTrue(isPidPresent);
			
		}
		
		@Test
		void testConfirmComment() { //CF
			 Whistler w = Whistler.getInstance();
			 
			 Account fakeAccount = w.getAccount("@elonmsk"); 
			 
			 fakeAccount.enterNewPost("title", "body"); 
			 String postPid = fakeAccount.getCurrePostPid();
			 
			 fakeAccount.addPostKeyword("#Keyword1");
			 fakeAccount.addPostKeyword("#Keyword2"); 
			 fakeAccount.setPostOwner();
			 fakeAccount.setPostVisibility(Visibility.PUBLIC); 
			 fakeAccount.confirmPost();
			 
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
		void testRemovePost_owner() { //CE //CF
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
			
			fakeAccount.enterNewPost("title", "body");
			String postPid = fakeAccount.getCurrePostPid();
			
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2");
			fakeAccount.addPostKeyword("#Keyword3");
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC);
			fakeAccount.confirmPost();	
			
			boolean isPostPresent = false;
			if (w.getPost(postPid)!=null) isPostPresent=true;
			
			fakeAccount.removePost(postPid);	//post's owner is fakeAccount
			
			assertEquals(isPostPresent==true,w.getPost(postPid)==null); //fakeAccount can delete post
		}
		
		@Test
		void testRemovePost_notOwner() { //CE //CF
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
			Account fakeAccount1 = w.getAccount("@alanturing");
			
			fakeAccount.enterNewPost("title", "body");
			String postPid = fakeAccount.getCurrePostPid();
			
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2");
			fakeAccount.addPostKeyword("#Keyword3");
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC);
			fakeAccount.confirmPost();	
			
			boolean isPostPresent = false;
			if (w.getPost(postPid)!=null) isPostPresent=true;
			
			fakeAccount1.removePost(postPid);	//post's owner is fakeAccount not fakeAccount1
			
			assertEquals(isPostPresent==true,w.getPost(postPid)!=null); //fakeAccount1 can't delete post
		}
		
		@Test
		void testRemoveComment_owner() { //CE //CF
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
		
		@Test  //this test is about tests: propertyChange, addPropertyChangeListener, updatePropertyListeners, addNotification methods.
		void testNotification() { //CF
			Whistler w = Whistler.getInstance();
			
			w.signUp("@kirchhoff", "Gustav Robert", "Kirchhoff", "Kirchhoff@gmail.com", "ciaociao22");
			
			Account fakeAccount = w.getAccount("@elonmsk");
			Account fakeAccount3 = w.getAccount("@kirchhoff");
			
			fakeAccount3.followAccount("@elonmsk");
			fakeAccount.updatePropertyListeners(fakeAccount.getNickname()); //fakeAccount updates its own PropertyListeners
			
			fakeAccount.enterNewPost("title", "body"); 
			String postPid = fakeAccount.getCurrePostPid();
			
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2");
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC); 
			fakeAccount.confirmPost();
			
			Post p = Whistler.getInstance().getPost(postPid);
																			//itemIdentifier
			Notification expectedNotification = new Notification(p.getOwner(),p.getPid(),p.getTimestamp());		
			expectedNotification.setAccount(fakeAccount3);
			
			ArrayList<Notification> actual = new ArrayList<>(fakeAccount3.getAllAccountNotifications());
			
			assertTrue(actual.contains(expectedNotification));
		}
		
		@Test 
		void testClearNotification() { //CF
			Whistler w = Whistler.getInstance();
			w.signUp("@kirchhoff", "Gustav Robert", "Kirchhoff", "Kirchhoff@gmail.com", "ciaociao22");
			
			Account fakeAccount = w.getAccount("@elonmsk");
			Account fakeAccount1 = w.getAccount("@alanturing");
			Account fakeAccount2 = w.getAccount("@kirchhoff");
			
			fakeAccount1.followAccount("@elonmsk");
			fakeAccount1.followAccount("@kirchhoff");
			fakeAccount.updatePropertyListeners(fakeAccount.getNickname());  //fakeAccount updates its own PropertyListeners
			fakeAccount2.updatePropertyListeners(fakeAccount2.getNickname());  //fakeAccount2 updates its own PropertyListeners
			
			fakeAccount.enterNewPost("title", "body");
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2");
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC); 
			fakeAccount.confirmPost();						//fakeAccount1 notifications n°=1
			
			fakeAccount2.enterNewPost("title", "body");
			fakeAccount2.addPostKeyword("#Keyword1");
			fakeAccount2.addPostKeyword("#Keyword2");
			fakeAccount2.setPostOwner();
			fakeAccount2.setPostVisibility(Visibility.PUBLIC); 
			fakeAccount2.confirmPost();						//fakeAccount1 notifications n°=2
			
			ArrayList<Notification> notifications = new ArrayList<>(fakeAccount1.getAllAccountNotifications());
			int oldNotificationsSize = notifications.size();
			
			fakeAccount1.clearAllNotifications(); //clearing All notifications
			
			int expectedNotificationsSize = 0;
			int actualNotificationsSize = fakeAccount1.getAllAccountNotifications().size();
			
			assertEquals(oldNotificationsSize,2);
			assertEquals(expectedNotificationsSize,actualNotificationsSize);
		}
		
		@Test 
		void testClearAllNotifications() { //CF
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
			Account fakeAccount1 = w.getAccount("@alanturing");
			
			fakeAccount1.followAccount("@elonmsk");
			fakeAccount.updatePropertyListeners(fakeAccount.getNickname());  //fakeAccount updates its own PropertyListeners
			
			fakeAccount.enterNewPost("title", "body");
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2");
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC); 
			fakeAccount.confirmPost();						//fakeAccount1 notifications n°=1
			
			ArrayList<Notification> notifications = new ArrayList<>(fakeAccount1.getAllAccountNotifications());
			int oldNotificationsSize = notifications.size();
			Notification n = notifications.get(0);  //there is only one notification
			
			fakeAccount1.clearNotification(n.getNid()); //clearing the specific notification
			
			int expectedNotificationsSize = 0;
			int actualNotificationsSize = fakeAccount1.getAllAccountNotifications().size();
			
			assertEquals(oldNotificationsSize,1);
			assertEquals(expectedNotificationsSize,actualNotificationsSize);
		}
		
		@Test
		void testLike_likeNotPresent() { //CE //CF
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
			
			fakeAccount1.like(postPid);
			
			Post p = Whistler.getInstance().getPost(postPid);
			
			assertTrue(p.getLikes().contains(fakeAccount1.getNickname()));
		}
		
		@Test
		void testLike_likeAlreadyPresent() { //CE //CF
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
			
			fakeAccount1.like(postPid); //the like is present on post
			
			assertFalse(fakeAccount1.like(postPid)); //fakeAccount1 tries to like the post twice
		}
		
		@Test
		void testRemoveLike_likePresent() {  //CE //CF
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
			
			fakeAccount1.like(postPid);
			fakeAccount.like(postPid);
			
			fakeAccount1.removeLike(postPid);
			
			Post p = Whistler.getInstance().getPost(postPid);
			
			int expectedLikes = 1;
			int actualLikes = p.getLikes().size();
			
			assertFalse(p.getLikes().contains(fakeAccount1.getNickname()));
			assertEquals(actualLikes,expectedLikes);
		}
		
		@Test
		void testRemoveLike_likeNotPresent() { //CE //CF
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
			
			//no previous fakeAccount's like on post with pid=postPid
			assertFalse(fakeAccount1.removeLike(postPid));
		}
	}
	
	@AfterEach
	public void cleanUpWhistlerAccounts() {
		Whistler w = Whistler.getInstance();
		Account fakeAccount1 = w.getAccount("@elonmsk");
		Account fakeAccount2 = w.getAccount("@alanturing");
		Account fakeAccount3 = w.getAccount("@kirchhoff");
		
		//removing fake account from whistler_db and cache
		if (w.getWhistlerAccounts().contains(fakeAccount1)) {
			w.removeAccount("@elonmsk");
		}
		if (w.getWhistlerAccounts().contains(fakeAccount2)) {
			w.removeAccount("@alanturing");
		}
		if (w.getWhistlerAccounts().contains(fakeAccount3)) {
			fakeAccount3.clearAllNotifications();
			w.removeAccount("@kirchhoff");
		}
	}
	
	@AfterAll
	public static void cleanUpKeywords() {
		String[] keywords = {"#Keyword1","#Keyword2","#Keyword3"};
		for (String key : keywords) {
			KeywordDao.getInstance().deleteKeyword(key);
		}
	}

}
