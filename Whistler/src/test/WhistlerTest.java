package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Whistler;

class WhistlerTest {
	
	//SignUp Tests
	@Test
	void testSignUp_ValidNicknameAndPassword() { //CE //VE //CF
		//Nickname without spaces and password length>8 
		Whistler w = Whistler.getInstance();									//length=9
		Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
	
		w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
		
		assertTrue(w.getWhistlerAccounts().contains(a));
	}
	
	@Test
	void testSignUp_NicknameAlreadyExists() {  //CF
		Whistler w = Whistler.getInstance();
		Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao22");
		w.getWhistlerAccounts().add(a);
		
		assertFalse(w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao22"));
	}
	
	@Test
	void testSignUp_NicknameWithSpaces() { //CE
		Whistler w = Whistler.getInstance();
			
		assertFalse(w.signUp("@elo nmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao22"));
	}
	
	@Test
	void testSignUp_PasswordLengthEqualEight() {  //VE
		Whistler w = Whistler.getInstance();
		
		assertTrue(w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao"));
	}
	
	@Test
	void testSignUp_PasswordLengthLessThanEight() { //VE
		Whistler w = Whistler.getInstance();
																			//length = 7
		assertFalse(w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaocie"));
	}

	//Login Tests	
	@Test
	void testLogin_InvalidNicknameAndPassword() { //CF
		Whistler w = Whistler.getInstance();
		Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao");
		w.getWhistlerAccounts().add(a);
		
		assertFalse(w.login("@elon","ciaociao9"));
	}
	
	@Test
	void testLogin_InvalidNickname() { //CF
		Whistler w = Whistler.getInstance();
		Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao");
		w.getWhistlerAccounts().add(a);
		
		assertFalse(w.login("@elon","ciaociao"));
	}
	
	@Test 
	void testLogin_InvalidPasswordLength() {  //CF //VE 
		Whistler w = Whistler.getInstance();
		Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao");
		w.getWhistlerAccounts().add(a);
									  //length = 4
		assertFalse(w.login("@elonmsk","ciao"));
	}
	
	@Test
	void testLogin_ValidNicknameAndPasswordLenghtButNotValidPassword() { //CF //VE
		Whistler w = Whistler.getInstance();
		Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
		w.getWhistlerAccounts().add(a);
										//length = 9
		assertFalse(w.login("@elonmsk","ciaociao3"));
	}
	
	@Test
	void testLogin_ValidNicknameAndPassword() { //CF //VE
		Whistler w = Whistler.getInstance();
		Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao");
		w.getWhistlerAccounts().add(a);
									  //Length = 8
		assertTrue(w.login("@elonmsk","ciaociao"));
	}
	 
	//SearchAccount Tests
	@Test
	void testSearchAccount_IsPresent() { //CE //CF
		Whistler w = Whistler.getInstance();
		Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciao");
		w.getWhistlerAccounts().add(a);
		
		assertEquals(a,w.getAccount("@elonmsk"));
	}
	
	@Test
	void testSearchAccount_NotPresent() { //CE //CF
		Whistler w = Whistler.getInstance();
		Account a = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciao");
		w.getWhistlerAccounts().add(a);
		
		assertNull(w.getAccount("@elon"));
	}
	
	
	@AfterEach
	public void cleanUpWhistlerAccounts() {
		Whistler w = Whistler.getInstance();
		w.getWhistlerAccounts().clear();
	}

}
