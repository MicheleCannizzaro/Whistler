package it.aps.whistler.domain;

import it.aps.whistler.util.AESUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import java.util.ArrayList;
import java.util.Objects;

enum Visibility{PUBLIC,PRIVATE}

public class Account {
	
	private String nickname;
	private String name;
	private String surname;
	private String email;
	private String encryptedPassword;
	private Visibility visibility;
	
	private SecretKey secretKey;
	private IvParameterSpec iv;
	
	private ArrayList<Account> followedAccounts;
	private ArrayList<Account> followers;

	
	public Account(String nickname, String name, String surname, String email, String plainTextPassword) {
		this.nickname = nickname;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.secretKey = AESUtil.generateKey(128);
		this.iv = AESUtil.generateIv();
		this.encryptedPassword = AESUtil.encryptPassword(plainTextPassword, this.secretKey, this.iv );
		this.visibility = Visibility.PRIVATE;
		
		this.followedAccounts = new ArrayList<>();
		this.followers = new ArrayList<>();
	}
	
	public void followAccount(Account accountToFollow) {
		followedAccounts.add(accountToFollow);
	}

	//Getter and Setter

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public String getNickname() {
		return nickname;
	}

	public ArrayList<Account> getFollowedAccounts() {
		return followedAccounts;
	}

	public ArrayList<Account> getFollowers() {
		return followers;
	}
	
	public void setPassword(String plainTextPassword) {
		this.encryptedPassword = AESUtil.encryptPassword(plainTextPassword, this.secretKey, this.iv );
	}
	
	
	public String getPassword() {
		String plainTextPassword = AESUtil.decryptPassword(this.encryptedPassword, this.secretKey, this.iv);
		return plainTextPassword;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;			//self check
		if (o == null) return false;		//null check 
		
		// type check and cast
		if (!(o instanceof Account)) return false; 
		Account account = (Account) o;
		
		//field comparison
		return Objects.equals(nickname, account.nickname);
		
	}
	
	@Override
	public String toString() {
		return "Account [nickname=" + nickname + ", name=" + name + ", surname=" + surname + ", email=" + email + "]";
	}	
}
