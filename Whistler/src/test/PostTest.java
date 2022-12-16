package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
		void testAddPostKeyword() {
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
			
			ArrayList<String> keywords  = new ArrayList<>();
			
			for (Keyword k : p.getPostKeywords()) {
				keywords.add(k.getWord());
			}
			
			assertTrue(keywords.contains("#Keyword1"));
			assertTrue(keywords.contains("#Keyword2"));
			assertTrue(keywords.contains("#Keyword3"));
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
		
		KeywordDao.getInstance().deleteKeyword("#Keyword1");
		KeywordDao.getInstance().deleteKeyword("#Keyword2");
		KeywordDao.getInstance().deleteKeyword("#Keyword3");
	}

}
