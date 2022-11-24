package test;

import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Whistler;

class AccountTest {

	@Test
	void testFollowAccount_TwoAccountInWhistler() { //CF
		Whistler w = Whistler.getInstance();
		Account a1 = new Account("@elonmsk", "Elon", "Musk", "elonmusk@gmail.com", "ciao");
		Account a2 = new Account("@jeffbezs", "Jeff", "Bezos", "jeffbezos@gmail.com", "trattore");
		w.getWhistlerAccounts().add(a1);
		w.getWhistlerAccounts().add(a2);
		
		a1.followAccount("@jeffbezs");
		
		assertTrue(a1.getFollowedAccounts().contains(a2));
		
		
	}

}
