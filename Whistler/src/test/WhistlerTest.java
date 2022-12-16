package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

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

class WhistlerTest {
	
	  @Nested
	  class StandaloneTests {
		  	
		  	//Sign-up Tests
		  	@Test
		  	void testSignUp_ValidNicknameAndPassword() { //CE //VE //CF
				Whistler w = Whistler.getInstance();
																					//length = 9
				assertTrue(w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2"));
			}
		  
			@Test
			void testSignUp_PasswordLengthEqualEight() {  //VE
				Whistler w = Whistler.getInstance();
																					//length = 8
				assertTrue(w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao"));
			}
			
			@Test
			void testSignUp_PasswordLengthLessThanEight() { //VE
				Whistler w = Whistler.getInstance();
																							//length = 7
				assertFalse(w.signUp("@alanturing", "Alan", "Turing", "alanturing@gmail.com", "ciaocie"));
			}
	  }
	
	  @Nested
	  class InitializedTests{
		  
		    @BeforeEach
			public void init() {
				Whistler w = Whistler.getInstance();
				w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
			}
		  
		    //Sign-up Tests
		    @Test
			void testSignUp_NicknameAlreadyExists() {  //CF
				Whistler w = Whistler.getInstance();
				
				//the user @elonmsk was previously added to the database 
				assertFalse(w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao22"));
			}
			
			@Test
			void testSignUp_NicknameWithSpaces() { //CE
				Whistler w = Whistler.getInstance();
					
				assertFalse(w.signUp("@alan turing", "Alan", "Turing", "alanturing@gmail.com", "ciaociao22"));
			}
			
			//Login Tests	
			@Test
			void testLogin_InvalidNicknameAndPassword() { //CF
				Whistler w = Whistler.getInstance();
				
				//correct Account details: ["@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2"]
				assertFalse(w.login("@elon","ciaociao9"));
			}
			
			@Test
			void testLogin_InvalidNickname() { //CF
				Whistler w = Whistler.getInstance();
				
				//correct Account details: ["@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2"]
				assertFalse(w.login("@elon","ciaociao2"));
			}
			
			@Test 
			void testLogin_InvalidPasswordLength() {  //CF //VE 
				Whistler w = Whistler.getInstance();
				
				//correct Account details: ["@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2"]
											  //length = 4
				assertFalse(w.login("@elonmsk","ciao"));
			}
			
			@Test
			void testLogin_ValidNicknameAndPasswordLenghtButNotValidPassword() { //CF //VE
				Whistler w = Whistler.getInstance();
				//correct Account details: ["@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2"]
												//length = 9
				assertFalse(w.login("@elonmsk","ciaociao3"));
			}
			
			@Test
			void testLogin_ValidNicknameAndPassword() { //CF //VE
				Whistler w = Whistler.getInstance();
				//correct Account details: ["@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2"]
											  //Length = 8
				assertTrue(w.login("@elonmsk","ciaociao2"));
			}
			 
			//GetAccount Tests
			@Test
			void testGetAccount_IsPresent() { //CE //CF
				Whistler w = Whistler.getInstance();
				Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
				
				assertEquals(a,w.getAccount("@elonmsk"));
			}
			
			@Test
			void testGetAccount_NotPresent() { //CE //CF
				Whistler w = Whistler.getInstance();
				
				assertNull(w.getAccount("@elonnotpresent"));
			}
			
			//GetPost Tests
			@Test
			void testGetPost_IsPresent() { //CE //CF
				Whistler w = Whistler.getInstance();
				Account fakeAccount = w.getAccount("@elonmsk");
				
				fakeAccount.enterNewPost("title", "body");
				String postPid = fakeAccount.getCurrePostPid();
				
				fakeAccount.addPostKeyword("#Keyword1");
				fakeAccount.addPostKeyword("#Keyword2");
				fakeAccount.setPostOwner();
				fakeAccount.setPostVisibility(Visibility.PUBLIC);
				fakeAccount.confirmPost();
				
				Post pExpected = new Post("title","body");
				pExpected.setPid(postPid);
				pExpected.addPostKeyword("#Keyword1");
				pExpected.addPostKeyword("#Keyword2");
				pExpected.setOwner(fakeAccount.getNickname());
				pExpected.setPostVisibility(Visibility.PUBLIC);
				
				
				assertEquals(pExpected, w.getPost(postPid));
			}
			
			@Test
			void testGetPost_NotPresent() { //CE //CF
				Whistler w = Whistler.getInstance();
				Account fakeAccount = w.getAccount("@elonmsk");
				
				fakeAccount.enterNewPost("title", "body");
				String postPid = fakeAccount.getCurrePostPid();
				
				fakeAccount.addPostKeyword("#Keyword1");
				fakeAccount.addPostKeyword("#Keyword2");
				fakeAccount.setPostOwner();
				fakeAccount.setPostVisibility(Visibility.PUBLIC);
				fakeAccount.confirmPost();
				
				fakeAccount.removePost(postPid);
				
				assertNull(w.getPost(postPid));
			}
			
			@Test
			void testGetAccountPublicPosts() { //CF
				Whistler w = Whistler.getInstance();
				Account fakeAccount = w.getAccount("@elonmsk");
				
				fakeAccount.enterNewPost("title", "body");
				fakeAccount.addPostKeyword("#Keyword1");
				fakeAccount.addPostKeyword("#Keyword2");
				fakeAccount.setPostOwner();
				fakeAccount.setPostVisibility(Visibility.PUBLIC);
				fakeAccount.confirmPost();
				
				fakeAccount.enterNewPost("title", "body");
				fakeAccount.addPostKeyword("#Keyword1");
				fakeAccount.addPostKeyword("#Keyword2");
				fakeAccount.setPostOwner();
				fakeAccount.setPostVisibility(Visibility.PRIVATE);
				fakeAccount.confirmPost();
				
				fakeAccount.enterNewPost("title", "body");
				fakeAccount.addPostKeyword("#Keyword1");
				fakeAccount.addPostKeyword("#Keyword2");
				fakeAccount.setPostOwner();
				fakeAccount.setPostVisibility(Visibility.PUBLIC);
				fakeAccount.confirmPost();
				
				int arrayListExpectedSize = 2;
				
				assertEquals(arrayListExpectedSize, w.getAccountPublicPosts("@elonmsk").size());
			}
			
			@Test
			void testGetAccountPublicInfo() { //CF
				Whistler w = Whistler.getInstance();
				Account fakeAccount = w.getAccount("@elonmsk");
				
				fakeAccount.getVisibility().put("Name", Visibility.PUBLIC);
				fakeAccount.getVisibility().put("Surname", Visibility.PRIVATE);
				fakeAccount.getVisibility().put("E-mail", Visibility.PUBLIC);
				
				w.updateAccount(fakeAccount);
				
				HashMap<String,String> accountPublicInfo = w.getAccountPublicInfo("@elonmsk");
				
				HashMap<String,String> accountPublicInfoExpected = new HashMap<>();
				accountPublicInfoExpected.put("Name","Elon");
				accountPublicInfoExpected.put("FollowedAccounts",String.valueOf(fakeAccount.getFollowedAccounts().size()));
				accountPublicInfoExpected.put("Followers",String.valueOf(fakeAccount.getFollowers().size()));
				accountPublicInfoExpected.put("E-mail", "elonmusk@gmail.com");
				
				
				assertEquals(accountPublicInfoExpected,accountPublicInfo);
				
			}
			
			@Test
			void testKeywordDiffusionRateReduction() { //CF
				Whistler w = Whistler.getInstance();
				Account fakeAccount = w.getAccount("@elonmsk");
				
				fakeAccount.enterNewPost("title", "body");
				String postPid = fakeAccount.getCurrePostPid();
				
				fakeAccount.addPostKeyword("#Key1"); 	 
				fakeAccount.addPostKeyword("#Key2");		
				fakeAccount.setPostOwner();
				fakeAccount.setPostVisibility(Visibility.PUBLIC);
				fakeAccount.confirmPost();
				
				fakeAccount.enterNewPost("title1", "body1");
				fakeAccount.addPostKeyword("#Key1"); 	
				fakeAccount.setPostOwner();
				fakeAccount.setPostVisibility(Visibility.PUBLIC);
				fakeAccount.confirmPost();
				
				//Key1 diffRate = 2  --- Key2 diffRate = 1
				
				Post p = w.getPost(postPid);
				w.keywordDiffusionRateReduction(p);
				
				//Expected
				//Key1 diffRate = 1  --- Key2 diffRate = 0
				
				Keyword k = KeywordDao.getInstance().getKeywordByWord("#Key1");
				Keyword k1 = KeywordDao.getInstance().getKeywordByWord("#Key2");
				
				assertTrue(k.getDiffusionRate()==1 && k1.getDiffusionRate() == 0);
				
			}
			
		}
	  
	  @Test
	  void testRemoveAccount() { //CF
		  Whistler w = Whistler.getInstance();
		  w.signUp("@johnneumann", "John", "von Neumann", "johnneumann@gmail.com", "ciaociao2");
		  
		  w.removeAccount("@johnneumann");
		  assertNull(w.getAccount("@johnneumann"));
	  }
	  
	  @AfterEach
	  public void cleanUp() {
		  Whistler w = Whistler.getInstance();
		  Account fakeAccount = w.getAccount("@elonmsk");
			
		  //removing fake account from whistler_db and cache
		  if (w.getWhistlerAccounts().contains(fakeAccount)) {
			  w.removeAccount("@elonmsk");
		  }
	  }
	  
	  @AfterAll
	  public static void cleanUpKeywords() {
		  KeywordDao.getInstance().deleteKeyword("#Keyword1");
		  KeywordDao.getInstance().deleteKeyword("#Keyword2");
		  KeywordDao.getInstance().deleteKeyword("#Key1");
		  KeywordDao.getInstance().deleteKeyword("#Key2");
	  }
}
