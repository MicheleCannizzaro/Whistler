package it.aps.whistler.domain;

import java.util.ArrayList;

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

	public boolean signUp(String nickname, String name, String surname, String email, String passwordPlainText) {
		if(!nickname.matches("\\S+")) {
			System.out.println("<<Spaces are not allowed in \"@nickname\". Please choose another one.>>");
			return false;
		}
		if(this.searchAccount(nickname) != null) {
			System.out.println("\n<<Sorry! The Nickname you chose is already taken. Please retry and choose another one.>>\n");
			return false;
		}
		if (passwordPlainText.length()<8) {
			System.out.println("\n<<Password must be at least 8 characters. Please, take in mind and retry.>>\n");
			return false;
		}
			Account account = new Account(nickname, name, surname, email, passwordPlainText);
			whistlerAccounts.add(account);
			return true;
	}
	
	public boolean login(String nickname, String password) {
		
		if (this.searchAccount(nickname) == null) {
			System.out.println("\n<<The Nickname is incorrect or non-existent! Please Sign-up first or enter a correct one>>");
			return false;
		}
		
		if (password.length()>=8) {
			
			for (Account a : whistlerAccounts) {
				if (a.getNickname().equals(nickname) && a.getPassword().equals(password)) return true;
			}
			
		}else {
			System.out.println("\n<<The password length cannot be less than 8 characters>>");
		}
		
		return false;
	}
		
	public Account searchAccount(String nickname){
		Account account = null;
		for(Account a : whistlerAccounts) {
			if(a.getNickname().equals(nickname)){
				account=a;
			}
		}
		return account;
	}
	
	public ArrayList<Account> getWhistlerAccounts() {
		return whistlerAccounts;
	}

}
