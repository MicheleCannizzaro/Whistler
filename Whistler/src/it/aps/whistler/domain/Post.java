package it.aps.whistler.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import it.aps.whistler.Visibility;
import it.aps.whistler.persistence.dao.CommentDao;
import it.aps.whistler.persistence.dao.KeywordDao;
import it.aps.whistler.persistence.dao.PostDao;
import it.aps.whistler.util.Util;

@SuppressWarnings("serial")
public class Post implements java.io.Serializable {
	
	private String pid;
	private String title;
	private String body;
	private Visibility postVisibility;
	private LocalDateTime timestamp;
	private String owner;
	private Set<Keyword> postKeywords = new HashSet<>(0); //also necessary for many-to-many Hibernate relation mapping
	
	private Set<Comment> comments  = new HashSet<>(0);
	
	public Post() {}
	
	public Post(String title, String body) {
		this.pid = Util.getRandomHexString();
		this.title = title;
		this.body = body;
		this.timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		this.comments = new HashSet<Comment>(0);
	}
	
	//UC3
	public void addPostKeyword(String word) {
		Keyword keyword = null;
		Keyword result = KeywordDao.getInstance().getKeywordByWord(word);
		
		if(result!=null) {
			keyword = result;
			int keywordDiffusionRate = keyword.getDiffusionRate();
			keyword.setDiffusionRate(keywordDiffusionRate+1);
			KeywordDao.getInstance().updateKeyword(keyword);
		}else {
			keyword = new Keyword(word);
			keyword.setDiffusionRate(1);
		
			KeywordDao.getInstance().saveKeyword(keyword);
		}
		
		this.postKeywords.add(keyword);
		
	}
	
	//UC3_1a
	public void clearKeyword() {
		this.postKeywords.clear();
	}
		 
	// Getter and Setter
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Visibility getPostVisibility() {
		return postVisibility;
	}

	public void setPostVisibility(Visibility postVisibility) {
		this.postVisibility = postVisibility;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public void setPostKeywords(Set<Keyword> postKeywords) {
		this.postKeywords = postKeywords;
	}

	public Set<Keyword> getPostKeywords() {
		if (this.postKeywords.isEmpty()) {
			Post p = PostDao.getInstance().getPostByPid(pid);
			p.getPostKeywords();
			this.postKeywords = p.getPostKeywords();
		}
		return postKeywords;
	}

	public Set<Comment> getComments() { //necessary for one-to-many Hibernate relation mapping
		return this.comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
	public ArrayList<Comment> getAllPostComments(){
		ArrayList<Comment> postComments = CommentDao.getInstance().getAllCommentsFromPost(this.pid);
		return postComments;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;			//self check
		if (o == null) return false;		//null check 
		
		//type check and cast
		if (!(o instanceof Post)) return false; 
		Post post = (Post) o;
		
		//field comparison
		return Objects.equals(pid, post.pid) && Objects.equals(title, post.title) && Objects.equals(body, post.body)
				&& Objects.equals(owner, post.owner);
	}
	
	@Override
	public String toString() {
		return "Post [title=" + title + ", body=" + body + ", postVisibility=" + postVisibility + ", timestamp="
				+ Util.getTimeString(timestamp) + ", owner=" + owner + ", pid=" + pid + "]";
	}
	
}