package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.persistence.dao.AccountDao;
import it.aps.whistler.persistence.dao.PostDao;

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
	}
	
	@Test
	void testConfirmPost() {
		Whistler w = Whistler.getInstance();
		w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
		Account fakeAccount = w.getAccount("@elonmsk");
			
		fakeAccount.enterNewPost("titoloDelPost", "rosso di sera bel tempo si spera");
		fakeAccount.addPostKeyword("ciao");
		fakeAccount.addPostKeyword("mare");
		fakeAccount.setPostOwner();
		fakeAccount.setPostVisibility(Visibility.PUBLIC);
		fakeAccount.confirmPost();	
		
		String expectedTitle = "titoloDelPost";
		String expectedBody = "rosso di sera bel tempo si spera";
		
		boolean isPidPresent=false;
		ArrayList<Post> fakeAccountPosts= PostDao.getInstance().getAllPostsFromOwner(fakeAccount.getNickname());
		
		for (Post post : fakeAccountPosts) {
			if (post.getTitle().equals(expectedTitle) && post.getBody().equals(expectedBody) && post.getOwner().equals("@elonmsk")) {
				if (post.getPid()!=null) {
					isPidPresent = true;
				}
			}
		}
		
		assertEquals(expectedTitle,fakeAccountPosts.get(0).getTitle());
		assertEquals(expectedBody,fakeAccountPosts.get(0).getBody());
		assertTrue(isPidPresent);
		
	}
	
	@AfterEach
	public void cleanUpWhistlerAccounts() {
		Whistler w = Whistler.getInstance();
		Account fakeAccount1 = w.getAccount("@elonmsk");
		Account fakeAccount2 = w.getAccount("@alanturing");
		
		//removing fake account from whistler_db and cache
		if (AccountDao.getInstance().getAllWhistlerAccounts().contains(fakeAccount1)) {
			AccountDao.getInstance().deleteAccount("@elonmsk");
		}
		if (AccountDao.getInstance().getAllWhistlerAccounts().contains(fakeAccount2)) {
			AccountDao.getInstance().deleteAccount("@alanturing");
		}
		w.getWhistlerAccounts().clear();
		
		ArrayList<Post> posts = PostDao.getInstance().getAllPostsFromOwner("@elonmsk");
		if (posts.size()>0) {
			for (Post p : posts ) {
				PostDao.getInstance().deletePost(p.getPid());
			}
		}
	}

}
