package it.aps.whistler.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.Visibility;
import it.aps.whistler.persistence.dao.AccountDao;
import it.aps.whistler.persistence.dao.CommentDao;
import it.aps.whistler.persistence.dao.KeywordDao;
import it.aps.whistler.persistence.dao.PostDao;
import it.aps.whistler.util.Util;

public class Whistler {
	private final static Logger logger = Logger.getLogger(Whistler.class.getName());
	
	private static Whistler instance;
	private ArrayList<Account> whistlerAccounts;
	
	private Whistler(){
		this.whistlerAccounts = new ArrayList<>();
		logger.logp(Level.INFO, Whistler.class.getSimpleName(), "Whistler constructor", "[Whistler] - Whistler was succesfully created.");
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
		
		if(!Util.isNicknameCorrect(nickname)) {
			System.out.println("<<Blank nicknames, special characters as !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ and spaces are not allowed in \"@nickname\". Please choose another one.>>");
			logger.logp(Level.WARNING, Whistler.class.getSimpleName(),"signUp", "nickname with spaces or special characters: "+nickname);
			return false;
		}
		
		if(this.getAccount(nickname) != null) {
			System.out.println("\n<<Sorry! The Nickname you chose is already taken. Please retry and choose another one.>>\n");
			logger.logp(Level.WARNING, Whistler.class.getSimpleName(),"signUp", "nickname already taken: "+nickname);
			return false;
		}
		
		if (passwordPlainText.length()<8) {
			System.out.println("\n<<Password must be at least 8 characters. Please, take in mind and retry.>>\n");
			logger.logp(Level.WARNING, Whistler.class.getSimpleName(),"signUp", nickname+" entered password with lenght<8.");
			return false;
		}
			//create account
			Account account = new Account(nickname, name, surname, email, passwordPlainText);
			
			//load account in cache
			whistlerAccounts.add(account);
			
			//save it in database using persistence stratum
			AccountDao.getInstance().saveAccount(account);
			
			return true;
	}
	
	public boolean login(String nickname, String password) {
		//get Account from cache or from database
		Account account = this.getAccount(nickname);
		
		if (account == null) {
			System.out.println("\n<<The Nickname is incorrect or non-existent! Please Sign-up first or enter a correct one>>");
			logger.logp(Level.WARNING, Whistler.class.getSimpleName(),"login","Incorrect nickname was entered: "+nickname);
			return false;
		}
		
		if (password.length()>=8) {
			
			if (account.getNickname().equals(nickname) && account.getPassword().equals(password)) return true;
			
		}else {
			System.out.println("\n<<The password length cannot be less than 8 characters>>");
			logger.logp(Level.WARNING, Whistler.class.getSimpleName(),"login","Incorrect password lenght was entered by: "+nickname);
		}
		
		return false;
	}
	
	//UC1_1b
	public void removeAccount(String nickname) {
		Account userAccount = this.getAccount(nickname);
		ArrayList<Post> posts = userAccount.getPosts();
		
		//remove userAccount's nickname from follower and followedAccount lists in all whistler accounts
		this.removeAccountAssociations(nickname);
		
		//Delete Posts
		for (Post p : posts) {
			//Keyword diffusionRate reduction
			keywordDiffusionRateReduction(p);
			
			//Delete Posts
			PostDao.getInstance().deletePost(p.getPid());
		}
		
		//Delete Account
			//from cache
		this.whistlerAccounts.remove(userAccount);
			//from db
		AccountDao.getInstance().deleteAccount(nickname);
	}
	
	private void removeAccountAssociations(String userNickname) {
		for (Account a : this.getWhistlerAccounts()) {
			ArrayList<String> followers = a.getFollowers();
			ArrayList<String> followedAccounts = a.getFollowedAccounts();
			
			//remove userNickname from a's followers
			if(followers.contains(userNickname)) {
				followers.remove(userNickname);
			}
			
			//remove userNickname from a's followedAccounts
			if (followedAccounts.contains(userNickname)) {
				followedAccounts.remove(userNickname);
			}
			
			//update
			a.setFollowers(followers);
			a.setFollowedAccounts(followedAccounts);
			
			this.updateAccount(a);
		}
	}
	
	public void keywordDiffusionRateReduction(Post p) {
		Set<Keyword> postKeywords = p.getPostKeywords();
		for (Keyword key : postKeywords) {
			key.setDiffusionRate(key.getDiffusionRate()-1);
			KeywordDao.getInstance().updateKeyword(key);
		}
	}
	
	//UC4 //UC7
	public ArrayList<Post> searchPosts(String keyword) {
		ArrayList<Post> matches = new ArrayList<>();
		Keyword key = getKeyword(keyword);
		
		for (Post p : getWhistlerPublicPosts()) {
			for (Keyword k : p.getPostKeywords()) {
				if (k.equals(key)) {
					matches.add(p);
				}
			}
		}
		
		return matches;
	}
	
	//UC6 
	public ArrayList<Post> getAccountPublicPosts(String nickname){
		ArrayList<Post> publicPosts = new ArrayList<>();
		ArrayList<Post> posts = new ArrayList<>();;
		
		try {
			posts = this.getAccount(nickname).getPosts(); 
		}catch(Exception ex) {
			System.out.println("<<Something went wrong while gathering account posts: "+ex);
			logger.logp(Level.SEVERE, Whistler.class.getSimpleName(),"getAccountPublicPosts","Something went wrong while gathering "+nickname+" account's posts: "+ex);
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
	
	//UC7
	public ArrayList<Keyword> getTrendingKeywords(){
		ArrayList<Keyword> trendingKeywords = new ArrayList<>();
		
		//getting keywords and sorting them in descending order of diffusionRate
		ArrayList<Keyword> allWhistlerKeywords = new ArrayList<>(KeywordDao.getInstance().getAllWhistlerKeywords());
		
		if(allWhistlerKeywords.isEmpty()) return trendingKeywords;
		
		Collections.sort(allWhistlerKeywords, Comparator.comparingInt(Keyword::getDiffusionRate).reversed());
		
		for (int i=0; i<3;i++) {		//getting the 3 most trending keywords
			trendingKeywords.add(allWhistlerKeywords.get(i));
		}
		return trendingKeywords;
	}
	
	public ArrayList<Post> getWhistlerPublicPosts() {
		ArrayList<Post> publicPosts = new ArrayList<>(); 
		for (Post p : PostDao.getInstance().getAllPosts()) {
			if (p.getPostVisibility()==Visibility.PUBLIC) {
				publicPosts.add(p);
			}
		}
		return publicPosts;
	}
	
	//UC8
	public HashMap<String,String> getAccountPublicInfo(String nickname){
		Account wbAccount =  this.getAccount(nickname);
		HashMap<String,String> accountPublicInfo = new HashMap<>();
		
		if(wbAccount.getVisibility().get("Name").toString().equals("PUBLIC")) {
			accountPublicInfo.put("Name",wbAccount.getName());
		}
		
		if(wbAccount.getVisibility().get("Surname").toString().equals("PUBLIC")) {
			accountPublicInfo.put("Surname",wbAccount.getSurname());
		}
		
		if(wbAccount.getVisibility().get("E-mail").toString().equals("PUBLIC")) {
			accountPublicInfo.put("E-mail",wbAccount.getEmail());
		}
		
		accountPublicInfo.put("Followers", String.valueOf(wbAccount.getFollowers().size()));
		accountPublicInfo.put("FollowedAccounts", String.valueOf(wbAccount.getFollowedAccounts().size()));
		
		return accountPublicInfo;
	}
	
	//Getter and Setter
	public ArrayList<Account> getWhistlerAccounts() {
		this.whistlerAccounts = AccountDao.getInstance().getAllWhistlerAccounts();
		return this.whistlerAccounts;
	}
	
	public void setWhistlerAccounts(ArrayList<Account> whistlerAccounts) {
		this.whistlerAccounts = whistlerAccounts;
	}
	
	//----convenient methods----
	public boolean isAccountPresent(String nickname) {           //checks if an account with the nickname provided exists on Whistler
		if (this.getAccount(nickname)!=null) return true;
		return false;
	}
	
	public boolean isKeywordPresent(String keyword) {           //checks if keyword with word==keyword exists on Whistler
		if (this.getKeyword(keyword)!=null) return true;
		return false;
	}
	
	public boolean isPidPresentAndRelativeToPublicPost(String postPid) {    //checks if pid exists on Whistler and it's relative to a public post
		Post p = this.getPost(postPid);
		if (p!=null && p.getPostVisibility().equals(Visibility.PUBLIC)) return true;
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
	
	public Post getPost(String postPid) {
		Post post = PostDao.getInstance().getPostByPid(postPid);
		return post;
	}
	
	public Comment getComment(String commentCid) {
		Comment comment = CommentDao.getInstance().getCommentByCid(commentCid);
		return comment;
	}
	
	public Keyword getKeyword(String word) {
		Keyword keyword = KeywordDao.getInstance().getKeywordByWord(word);
		return keyword;
	}
	
	public void updatePost(Post p) {
		PostDao.getInstance().updatePost(p);
	}
	
	public void updateAccount(Account a) {
		AccountDao.getInstance().updateAccount(a);
	}
	
	public void updateComment(Comment c) {
		CommentDao.getInstance().updateComment(c);
	}
}
