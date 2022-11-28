package it.aps.whistler.domain;

import it.aps.whistler.persistence.dao.AccountDao;
import it.aps.whistler.util.AESUtil;

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
	
	private String encodedKey;
	private String encodedIv;
	
	private ArrayList<String> followedAccounts;
	private ArrayList<String> followers;

	public Account() {}
	
	public Account(String nickname, String name, String surname, String email, String plainTextPassword) {
		this.nickname = nickname;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.encodedKey = AESUtil.generateEncodedKey();
		this.encodedIv = AESUtil.generateEncodedIv();
		this.encryptedPassword = AESUtil.encryptPassword(plainTextPassword, AESUtil.getSecretKeyFromEncodedKey(this.encodedKey), AESUtil.getIvParameterSpec(this.encodedIv));
		this.visibility = Visibility.PRIVATE;
		
		this.followedAccounts = new ArrayList<>();
		this.followers = new ArrayList<>();
	}
	
	public void followAccount(String nickname) {
		
		if(!followedAccounts.contains(nickname)) {
			
			followedAccounts.add(nickname);
			AccountDao.getInstance().updateAccount(Whistler.getInstance().getAccount(this.nickname));
			
		}else{
			
			System.out.println("You already follow: "+nickname);
			
			System.out.println("\nThat's your cyrcle of interests:");
			for (String accountNickname: followedAccounts) {
				System.out.println(accountNickname);
			}
		}
	}

	//Getter and Setter
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

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
	
	public String getEncodedIv() {
		return encodedIv;
	}

	public void setEncodedIv(String encodedIv) {
		this.encodedIv = encodedIv;
	}

	public String getEncodedKey() {
		return encodedKey;
	}

	public void setEncodedKey(String encodedKey) {
		this.encodedKey = encodedKey;
	}
	
	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public void setPassword(String plainTextPassword) {
		this.encryptedPassword = AESUtil.encryptPassword(plainTextPassword, AESUtil.getSecretKeyFromEncodedKey(this.encodedKey), AESUtil.getIvParameterSpec(this.encodedIv));
	}
		
	public String getPassword() {
		String plainTextPassword = AESUtil.decryptPassword(this.encryptedPassword, AESUtil.getSecretKeyFromEncodedKey(this.encodedKey), AESUtil.getIvParameterSpec(this.encodedIv));
		return plainTextPassword;
	}
	
	public ArrayList<String> getFollowedAccounts() {
		return followedAccounts;
	}

	public ArrayList<String> getFollowers() {
		return followers;
	}
	
	public void setFollowedAccounts(ArrayList<String> followedAccounts) {
		this.followedAccounts = followedAccounts;
	}

	public void setFollowers(ArrayList<String> followers) {
		this.followers = followers;
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
