package it.aps.whistler.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import it.aps.whistler.Visibility;
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
	private Set<Keyword> postKeywords = new HashSet<>(); //also necessary for many-to-many Hibernate relation mapping
	
	public Post() {}
	
	public Post(String title, String body) {
		this.pid = Util.getRandomHexString();
		this.title = title;
		this.body = body;
		this.timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		this.postKeywords = new HashSet<Keyword>(0);	
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

	@Override
	public String toString() {
		//DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = this.timestamp.format(formatter);
        
		return "Post [title=" + title + ", body=" + body + ", postVisibility=" + postVisibility + ", timestamp="
				+ formattedDateTime + ", owner=" + owner + ", pid=" + pid + "]";
	}
	
}