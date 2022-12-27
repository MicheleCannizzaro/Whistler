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
import it.aps.whistler.domain.Keyword;
import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;
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
		void testRemovePost() { //CF
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
			
			fakeAccount.removePost(postPid);
			
			assertEquals(isPostPresent==true,w.getPost(postPid)==null);
		}
	}
	
	@AfterEach
	public void cleanUpWhistlerAccounts() {
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
		  String[] keywords = {"#Keyword1","#Keyword2","#Keyword3"};
		  for (String key : keywords) {
			  KeywordDao.getInstance().deleteKeyword(key);
		  }
	  }

}
