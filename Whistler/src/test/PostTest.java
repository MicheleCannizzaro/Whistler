package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

class PostTest {

	@Nested class InitializedTests{
		@BeforeEach
		public void init() {
			Whistler w = Whistler.getInstance();
			w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
		}
	
		
		@Test
		void testAddPostKeyword() {	//CF
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
				
			fakeAccount.enterNewPost("title", "body");
			String postPid = fakeAccount.getCurrePostPid();
			
			fakeAccount.addPostKeyword("#Keyword1");
			fakeAccount.addPostKeyword("#Keyword2");
			fakeAccount.setPostOwner();
			fakeAccount.setPostVisibility(Visibility.PUBLIC);
			fakeAccount.confirmPost();	
			
			Post p = w.getPost(postPid);
			
			p.addPostKeyword("#Keyword3");
			
			ArrayList<Keyword> expecetdKeywords = new ArrayList<>();
			expecetdKeywords.add(new Keyword("#Keyword1"));
			expecetdKeywords.add(new Keyword("#Keyword2"));
			expecetdKeywords.add(new Keyword("#Keyword3"));
			
			//Keywords Set in ArrayList
			ArrayList<Keyword> keywords = new ArrayList<>(p.getPostKeywords());
			
			//Sorting Keywords Alphabetical Order
			Collections.sort(keywords, Comparator.comparing(Keyword::getWord));
			
			assertEquals(expecetdKeywords,keywords);
		}
		
		@Test
		void testAddLike_likeNotPresent() {	//CE //CF
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
			
			Post p = Whistler.getInstance().getPost(postPid);
			p.addLike(fakeAccount.getNickname());
			
			int expectedLikes = 1;
			int actualLikes = p.getLikes().size();
			
			assertEquals(expectedLikes,actualLikes);
		}
		
		@Test
		void testAddLike_likePresent() {	//CE //CF
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
			
			Post p = Whistler.getInstance().getPost(postPid);
			p.addLike(fakeAccount.getNickname());
			
			p.addLike(fakeAccount.getNickname()); //secont time post add the like of the same account
			
			int expectedLikes = 1;
			int actualLikes = p.getLikes().size();
			
			assertEquals(expectedLikes,actualLikes);
			
		}
		
		@Test
		void testRemoveLike_likePresent() {	//CE //CF
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
			
			Post p = Whistler.getInstance().getPost(postPid);
			p.addLike(fakeAccount.getNickname());   //Likes = 1
			
			p.removeLike(fakeAccount.getNickname()); //Likes = 0
			
			int expectedLikes = 0;
			int actualLikes = p.getLikes().size();
			
			assertEquals(expectedLikes,actualLikes);
		}
		
		@Test
		void testRemoveLike_likeNotPresent() {	//CE //CF
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
			
			Post p = Whistler.getInstance().getPost(postPid);
			
			assertFalse(p.removeLike(fakeAccount.getNickname()));
		}
		
	}

	@AfterEach
	public void cleanUpWhistlerAccounts() {
		Whistler w = Whistler.getInstance();
		Account fakeAccount1 = w.getAccount("@elonmsk");

		//removing fake account from whistler_db and cache
		if (w.getWhistlerAccounts().contains(fakeAccount1)) {
			w.removeAccount("@elonmsk");
		}
		
		String[] keywords = {"#Keyword1","#Keyword2","#Keyword3"};
		  for (String key : keywords) {
			  KeywordDao.getInstance().deleteKeyword(key);
		  }
	}

}
