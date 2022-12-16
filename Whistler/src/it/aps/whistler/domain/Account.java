package it.aps.whistler.domain;

import it.aps.whistler.Visibility;
import it.aps.whistler.persistence.dao.AccountDao;
import it.aps.whistler.persistence.dao.PostDao;
import it.aps.whistler.util.AESUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Account {
	private final static Logger logger = Logger.getLogger(Account.class.getName());
	
	private String nickname;
	private String name;
	private String surname;
	private String email;
	private String encryptedPassword;	
	private HashMap<String,Visibility> visibility; 
	
	private String encodedKey;
	private String encodedIv;
	
	private ArrayList<String> followedAccounts;
	private ArrayList<String> followers;
	
	private Post currentPost;
	private ArrayList<Post> posts;

	public Account() {}
	
	public Account(String nickname, String name, String surname, String email, String plainTextPassword) {
		this.nickname = nickname;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.encodedKey = AESUtil.generateEncodedKey();
		this.encodedIv = AESUtil.generateEncodedIv();
		this.encryptedPassword = AESUtil.encryptPassword(plainTextPassword, AESUtil.getSecretKeyFromEncodedKey(this.encodedKey), AESUtil.getIvParameterSpec(this.encodedIv));
		
		this.visibility = new HashMap<String,Visibility>();
		this.visibility.put("Name", Visibility.PRIVATE);
		this.visibility.put("Surname", Visibility.PRIVATE);
		this.visibility.put("E-mail", Visibility.PUBLIC);
		
		this.followedAccounts = new ArrayList<>();
		this.followers = new ArrayList<>();
		this.posts = new ArrayList<>();
	}
	
	//UC2 
	public void followAccount(String nickname) {
		
		Account whistleblowerAccount = Whistler.getInstance().getAccount(nickname);  //the account of the whistleblower to follow
		
		if (Whistler.getInstance().isPresent(nickname) && !this.nickname.equals(nickname)) {
		
			if(!followedAccounts.contains(nickname)) {
				
				followedAccounts.add(nickname);
				AccountDao.getInstance().updateAccount(Whistler.getInstance().getAccount(this.nickname));
				
				whistleblowerAccount.addFollower(this.nickname);       //adding the user to the whistleblower's followers
				
			}else{
				System.out.println("<<You already follow the Account with nickname: \""+nickname+"\">>");
				logger.logp(Level.INFO, Account.class.getSimpleName(),"followAccount","The user with account "+this.nickname+" wants to follow "+nickname+" but he already follows it.");
			}
			
		}else{
			System.out.println("\n<<Sorry the Account with nickname: \""+nickname+"\" does not exist on Whistler or it's your account>>");
			logger.logp(Level.INFO, Account.class.getSimpleName(),"followAccount","The user with account "+this.nickname+" entered "+nickname+", that does not exist on Whistler or entered his own account nickname.");
		}
	}
	
	//UC2_1a
	public void unFollowAccount(String nickname) {
		Account whistleblowerAccount = Whistler.getInstance().getAccount(nickname);  //the account of the whistleblower to unfollow
		
		if (Whistler.getInstance().isPresent(nickname) && !this.nickname.equals(nickname)) {
			
			if(followedAccounts.contains(nickname)) {
				
				followedAccounts.remove(nickname);
				AccountDao.getInstance().updateAccount(Whistler.getInstance().getAccount(this.nickname));
				
				whistleblowerAccount.removeFollower(this.nickname);		//removing the user to the whistleblower's followers
			}else{
				System.out.println("\n<<You can't unfollow the Account with nickname: \""+nickname+"\" because you already don't follow it.>>");
				logger.logp(Level.INFO, Account.class.getSimpleName(),"unFollowAccount","The user with account "+this.nickname+" entered "+nickname+" to unfollow it, but he already don't follows it.");
			}
			
		}else {
			System.out.println("\n<<Sorry you can't unfollow the Account with nickname: \""+nickname+"\" because it does not exist on Whistler or because it's your account>>");
			logger.logp(Level.INFO, Account.class.getSimpleName(),"unFollowAccount","The user with account "+this.nickname+" entered "+nickname+", that does not exist on Whistler or entered his own account nickname.");
		}

	}

	private void addFollower(String Nickname) {
		if (this.followers==null) {
			this.followers = AccountDao.getInstance().getAccountByNickname(this.nickname).getFollowers();
		}
		this.followers.add(Nickname);
		AccountDao.getInstance().updateAccount(Whistler.getInstance().getAccount(this.nickname));
		
	}
	
	private void removeFollower(String Nickname) {
		if (this.followers==null) {
			this.followers = AccountDao.getInstance().getAccountByNickname(this.nickname).getFollowers();
		}
		this.followers.remove(Nickname);
		AccountDao.getInstance().updateAccount(Whistler.getInstance().getAccount(this.nickname));
		
	}

	//UC3
	public void enterNewPost(String title, String body) {
		this.currentPost = new Post(title,body);
	}
	
	//UC3
	public void addPostKeyword(String word) {
		this.currentPost.addPostKeyword(word);
	}
	
	//UC3
	public void setPostOwner() {
		this.currentPost.setOwner(this.nickname);
	}
	
	//UC3
	public void setPostVisibility(Visibility vis) {
		this.currentPost.setPostVisibility(vis);
	}
	
	//UC3
	public void confirmPost() {
		PostDao.getInstance().savePost(this.currentPost);
	}
	
	//UC3
	public void removePost(String postPid) {
		Post p = Whistler.getInstance().getPost(postPid);
		
		if (p!=null) {
			if (this.nickname.equals(p.getOwner())) {
				
				Whistler.getInstance().keywordDiffusionRateReduction(p);
				PostDao.getInstance().deletePost(postPid);
				
			}else {
				logger.logp(Level.SEVERE,Account.class.getSimpleName(),"removePost", this.nickname+" wants to remove post ("+postPid+") of an other user!");
			}
		}else {
			logger.logp(Level.SEVERE,Account.class.getSimpleName(),"removePost","post with pid:"+postPid+" does not exist on whistler.");
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

	//UC1_1a
	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}
	//UC1_1a
	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}
	//UC1_1a
	public void setEmail(String email) {
		this.email = email;
	}
	
	public HashMap<String, Visibility> getVisibility() {
		return visibility;
	}

	//UC1_1a
	public void setVisibility(HashMap<String, Visibility> visibility) {
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

	//UC1 - edit account
	public boolean setPassword(String plainTextPassword) {
		if (plainTextPassword.length()<8) {
			System.out.println("\n<<Password must be at least 8 characters. Please, take in mind and retry.>>\n");
			logger.logp(Level.WARNING, Account.class.getSimpleName(),"setPassword","The user with account "+this.nickname+" entered a password with lenght < 8 characters.");
			return false;
		}
		
		this.encryptedPassword = AESUtil.encryptPassword(plainTextPassword, AESUtil.getSecretKeyFromEncodedKey(this.encodedKey), AESUtil.getIvParameterSpec(this.encodedIv));
		return true;
	}
		
	public String getPassword() {
		String plainTextPassword = AESUtil.decryptPassword(this.encryptedPassword, AESUtil.getSecretKeyFromEncodedKey(this.encodedKey), AESUtil.getIvParameterSpec(this.encodedIv));
		return plainTextPassword;
	}
	
	public ArrayList<String> getFollowedAccounts() {
		return followedAccounts;
	}
	
	public void setFollowedAccounts(ArrayList<String> followedAccounts) {
		this.followedAccounts = followedAccounts;
	}

	public ArrayList<String> getFollowers() {
		return followers;
	}

	public void setFollowers(ArrayList<String> followers) {
		this.followers = followers;
	}

	//UC8
	public ArrayList<Post> getPosts() {
		this.posts = PostDao.getInstance().getAllPostsFromOwner(this.nickname);
		return this.posts;
	}

	public void setPosts(ArrayList<Post> posts) {
		this.posts = posts;
	}
	
	public String getCurrePostPid() {
		return this.currentPost.getPid();
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
