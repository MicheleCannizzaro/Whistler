package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.persistence.dao.AccountDao;

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
		
			@AfterEach
			public void cleanUpWhistlerAccounts() {
				Whistler w = Whistler.getInstance();
				Account fakeAccount = w.getAccount("@elonmsk");
				
				//removing fake account from whistler_db and cache
				if (AccountDao.getInstance().getAllWhistlerAccounts().contains(fakeAccount)) {
					AccountDao.getInstance().deleteAccount("@elonmsk");
					w.getWhistlerAccounts().clear();
				}
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
			 
			//SearchAccount Tests
			@Test
			void testSearchAccount_IsPresent() { //CE //CF
				Whistler w = Whistler.getInstance();
				Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
				
				assertEquals(a,w.getAccount("@elonmsk"));
			}
			
			@Test
			void testSearchAccount_NotPresent() { //CE //CF
				Whistler w = Whistler.getInstance();
				
				assertNull(w.getAccount("@elonnotpresent"));
			}
			
			
			@AfterEach
			public void cleanUpWhistlerAccounts() {
				Whistler w = Whistler.getInstance();
				Account fakeAccount = w.getAccount("@elonmsk");
				
				//removing fake account from whistler_db and cache
				if (AccountDao.getInstance().getAllWhistlerAccounts().contains(fakeAccount)) {
					AccountDao.getInstance().deleteAccount("@elonmsk");
					w.getWhistlerAccounts().clear();
				}
			}
	  }
	  
	  @AfterEach
		public void cleanUpWhistlerAccounts() {
			Whistler w = Whistler.getInstance();
			Account fakeAccount = w.getAccount("@elonmsk");
			
			//removing fake account from whistler_db and cache
			if (AccountDao.getInstance().getAllWhistlerAccounts().contains(fakeAccount)) {
				AccountDao.getInstance().deleteAccount("@elonmsk");
				w.getWhistlerAccounts().clear();
			}
		}
	
}
