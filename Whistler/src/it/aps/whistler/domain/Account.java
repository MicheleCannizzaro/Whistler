package it.aps.whistler.domain;

import it.aps.whistler.Visibility;
import it.aps.whistler.persistence.dao.AccountDao;
import it.aps.whistler.persistence.dao.CommentDao;
import it.aps.whistler.persistence.dao.NotificationDao;
import it.aps.whistler.persistence.dao.PostDao;
import it.aps.whistler.util.AESUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class Account implements java.io.Serializable, PropertyChangeListener {
	private final static Logger logger = Logger.getLogger(Account.class.getName());
	
	private String nickname;
	private String name;
	private String surname;
	private String email;
	private String encryptedPassword;	
	private HashMap<String,Visibility> visibility; 
	
	private String encodedKey;
	private String encodedIv;
	
	private ArrayList<String> followedAccounts;  //List of account nicknames that the account follow
	private ArrayList<String> followers;		 //List of account nicknames that follow the account
	
	private Post currentPost;
	private ArrayList<Post> posts;
	private Comment currentComment;
	
	private Set<Notification> notifications = new HashSet<>(0);	 //necessary for one-to-many Hibernate relation mapping
	private PropertyChangeSupport support = new PropertyChangeSupport(this);

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
		
		this.notifications  = new HashSet<>(0);
	}
	
	//UC2 
	public void followAccount(String nickname) {
		
		Account whistleblowerAccount = Whistler.getInstance().getAccount(nickname);  //the account of the whistleblower to follow
		
		if (Whistler.getInstance().isAccountPresent(nickname) && !this.nickname.equals(nickname)) {
		
			if(!followedAccounts.contains(nickname)) {
				
				followedAccounts.add(nickname);
				AccountDao.getInstance().updateAccount(this);
				
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
		
		if (Whistler.getInstance().isAccountPresent(nickname) && !this.nickname.equals(nickname)) {
			
			if(followedAccounts.contains(nickname)) {
				
				followedAccounts.remove(nickname);
				AccountDao.getInstance().updateAccount(this);
				
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
		AccountDao.getInstance().updateAccount(this);
		
		
	}
	
	private void removeFollower(String Nickname) {
		if (this.followers==null) {
			this.followers = AccountDao.getInstance().getAccountByNickname(this.nickname).getFollowers();
		}
		this.followers.remove(Nickname);
		AccountDao.getInstance().updateAccount(this);
		
	}

	//UC3
	public boolean enterNewPost(String title, String body) {
		if(title.length()<=70 && body.length()<=280) {
			this.currentPost = new Post(title,body);
			return true;
		}
		return false;
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
		System.out.println(" wait ");
		
		//UC9
		//Observer Pattern - fire notification of new post
		support.firePropertyChange("post", null, this.currentPost);
	}
	
	//UC3_1b
	public boolean removePost(String postPid) {
		Post p = Whistler.getInstance().getPost(postPid);
		
		if (p!=null) {
			if (this.nickname.equals(p.getOwner())) {
				
				Whistler.getInstance().keywordDiffusionRateReduction(p);
				PostDao.getInstance().deletePost(postPid);
				return true;
			}else {
				logger.logp(Level.SEVERE,Account.class.getSimpleName(),"removePost", this.nickname+" wants to remove post ("+postPid+") of an other user!");
			}
		}else {
			logger.logp(Level.SEVERE,Account.class.getSimpleName(),"removePost","post with pid:"+postPid+" does not exist on whistler.");
		}
		return false;
	}
	
	//UC5
	public boolean enterNewComment(String postPid, String body) {
		Post p = Whistler.getInstance().getPost(postPid);
		if(p!=null && body.length()<=280) {
			this.currentComment = new Comment(body);
			this.currentComment.setCommentVisibility(p.getPostVisibility());
			this.currentComment.setPost(p);
			return true;
		}
		return false;
	}
	
	//UC5
	public void setCommentOwner() {
		this.currentComment.setOwner(this.nickname);
	}
	
	//UC5
	public void confirmComment() {
		CommentDao.getInstance().saveComment(this.currentComment);
	}
	
	//UC5_1b
	public boolean removeComment(String commentCid) {
		Comment c = Whistler.getInstance().getComment(commentCid);
		
		if (c!=null) {
			if (this.nickname.equals(c.getOwner())) {
				
				CommentDao.getInstance().deleteComment(commentCid);
				return true;
			}else {
				logger.logp(Level.SEVERE,Account.class.getSimpleName(),"removeComment", this.nickname+" wants to remove comment ("+commentCid+") of an other user!");
			}
		}else {
			logger.logp(Level.SEVERE,Account.class.getSimpleName(),"removeComment","comment with cid:"+commentCid+" is not present in this post.");
		}
		return false;
	}
	
	//UC9 - Observer pattern implemented with PropertyChangeListener
	@Override
	public void propertyChange(PropertyChangeEvent evt) {	
		if(evt.getPropertyName().equals("post")) {
			Post p = (Post)evt.getNewValue();
			
			if(p.getPostVisibility().equals(Visibility.PUBLIC)) {
				
				if(this.followedAccounts.contains(p.getOwner())) {
					System.out.println(" ... ");
					
					//create a permanent notification
					Notification n = new Notification(p.getOwner(),p.getPid(),p.getTimestamp());
					n.setAccount(this);
					this.addNotification(n);
					
					//send notification mail if the address of the account provided it's reachable
					Whistler.getInstance().sendEmailNotification(this.nickname, n);
				}
			}
		}
    }
	
	//UC9
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

	//UC9
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
    
    //UC9
    public void updatePropertyListeners(String nickname) {
    	Account a = AccountDao.getInstance().getAccountByNickname(nickname);
    	
    	for (String followerNickname : a.followers) {
    		Account flwr = AccountDao.getInstance().getAccountByNickname(followerNickname);
    		support.addPropertyChangeListener(flwr); 
    	}
    }
	
	//UC9
	public void addNotification(Notification notification) {
		try {
			NotificationDao.getInstance().saveNotification(notification);
		}catch(javax.persistence.PersistenceException ex){
			logger.logp(Level.SEVERE, Account.class.getSimpleName(),"addNotification","Duplicate entry: "+ex);
			System.out.println("Sorry something went wrong in saving notification: post("+notification.getItemIdentifier()+")"); //itemIdentifier contains postPid
		}
	}
	
	//UC9_1a
	public boolean clearNotification(String nid) {
		if(NotificationDao.getInstance().deleteNotification(nid)) {
			return true;
		}
		return false;
	}
	
	//UC9_1b
	public boolean clearAllNotifications() {
		ArrayList<Boolean> results = new ArrayList<>();
		boolean result;
		
		for(Notification n : this.getAllAccountNotifications()) {
			result = this.clearNotification(n.getNid());
			results.add(result);
		}
		
		for(boolean b : results) if(!b) return false;
		return true;
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

	//UC1_1a
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
	
	public Comment getCurrentComment() {
		return currentComment;
	}

	public void setCurrentComment(Comment currentComment) {
		this.currentComment = currentComment;
	}
	
	public Set<Notification> getNotifications() { //necessary for one-to-many Hibernate relation mapping
		return this.notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}
	
	//UC9  -- useful for resolving Hibernate LazyInitializationException 
	public Set<Notification> getAllAccountNotifications(){
		this.notifications = NotificationDao.getInstance().getAllNotificationFromNickname(this.nickname);
		return this.notifications;
	}
	
	//----convenient methods----
	public boolean isNidPresent(String notificationNid) {    //checks if NID exists on Whistler and it's relative to the account
		Notification n = Whistler.getInstance().getNotification(notificationNid);
		if (n!=null && n.getAccount().getNickname().equals(this.nickname)) return true;
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;			//self check
		if (o == null) return false;		//null check 
		
		//type check and cast
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
