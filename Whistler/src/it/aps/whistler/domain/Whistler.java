package it.aps.whistler.domain;

import java.util.ArrayList;

import it.aps.whistler.Visibility;
import it.aps.whistler.persistence.dao.AccountDao;

public class Whistler {
	
	private static Whistler instance;
	private ArrayList<Account> whistlerAccounts;
	
	private Whistler(){
		this.whistlerAccounts = new ArrayList<>();
		System.out.println("[Whistler] - Whistler was succesfully created.");
	}
	
	//Singleton pattern created by static method - Lazy Initialization
	public static synchronized Whistler getInstance() {
		if (instance == null) { 
			instance = new Whistler();
		}
		return instance;
	}
	
	//UC1
	public boolean signUp(String nickname, String name, String surname, String email, String passwordPlainText) {
		
		if(!nickname.matches("\\S+")) {
			System.out.println("<<Spaces are not allowed in \"@nickname\". Please choose another one.>>");
			return false;
		}
		
		if(this.getAccount(nickname) != null) {
			System.out.println("\n<<Sorry! The Nickname you chose is already taken. Please retry and choose another one.>>\n");
			return false;
		}
		
		if (passwordPlainText.length()<8) {
			System.out.println("\n<<Password must be at least 8 characters. Please, take in mind and retry.>>\n");
			return false;
		}
			//create account
			Account account = new Account(nickname, name, surname, email, passwordPlainText);
			
			//save it in database using persistence stratum
			AccountDao.getInstance().saveAccount(account);
			
			//load account in cache
			whistlerAccounts.add(account);
			return true;
	}
	
	public boolean login(String nickname, String password) {
		//get Account from cache or from database
		Account account = this.getAccount(nickname);
		
		if (account == null) {
			System.out.println("\n<<The Nickname is incorrect or non-existent! Please Sign-up first or enter a correct one>>");
			return false;
		}
		
		if (password.length()>=8) {
			
			if (account.getNickname().equals(nickname) && account.getPassword().equals(password)) return true;
			
		}else {
			System.out.println("\n<<The password length cannot be less than 8 characters>>");
		}
		
		return false;
	}
		
	public Account getAccount(String nickname){
		Account account = null;
		
		//check if account is in cache
		for(Account a : whistlerAccounts) {
			if(a.getNickname().equals(nickname)){
				account=a;
			}
		}
		
		//if account not present in cache search it in database and load it to cache
		if (account == null) {
			account = AccountDao.getInstance().getAccountByNickname(nickname);
			if (account != null) {
				whistlerAccounts.add(account);
			}
		}
		
		return account;
	}
	
	public boolean isPresent(String nickname) {           //checks if an account with the nickname provided exists on Whistler
		if (this.getAccount(nickname)!=null) return true;
		return false;
	}
	
	public ArrayList<Account> getWhistlerAccounts() {
		return whistlerAccounts;
	}
	
	public void setWhistlerAccounts(ArrayList<Account> whistlerAccounts) {
		this.whistlerAccounts = whistlerAccounts;
	}
	
	public void removeAccountFromCache(String nickname) {
		Account userAccount = AccountDao.getInstance().getAccountByNickname(nickname);
		this.whistlerAccounts.remove(userAccount);
	}

	//UC6 //UC8
	public ArrayList<Post> getAccountPublicPosts(String nickname){
		ArrayList<Post> publicPosts = new ArrayList<>();
		ArrayList<Post> posts = new ArrayList<>();;
		
		try {
			posts = this.getAccount(nickname).getPosts(); 
		}catch(Exception ex) {
			System.out.println("<<Something went wrong while gathering account posts: "+ex);
		}
		
		if(!posts.isEmpty()) {
			for (Post p: posts) {
				if (p.getPostVisibility()==Visibility.PUBLIC) {
					publicPosts.add(p);
				}
			}
		}
		
		return publicPosts;
	}

}
