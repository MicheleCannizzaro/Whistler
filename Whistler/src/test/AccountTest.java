package test;

import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.persistence.dao.AccountDao;

class AccountTest {

	@BeforeEach
	public void init() {
		Whistler w = Whistler.getInstance();
		w.signUp("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciaociao2");
		w.signUp("@alanturing", "Alan", "Turing", "alanturing@gmail.com", "ciaociao22");
	}

	@Test
	void testFollowAccount() { //CF
		Whistler w = Whistler.getInstance();
		Account account = w.getAccount("@elonmsk");
		
		account.followAccount("@alanturing");
		Account accountAfterUpdate = w.getAccount("@elonmsk");
		
		assertTrue(accountAfterUpdate.getFollowedAccounts().contains("@alanturing"));
		
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
	}

}
