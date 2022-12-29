package it.aps.whistler.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import it.aps.whistler.Visibility;
import it.aps.whistler.util.Util;

@SuppressWarnings("serial")
public class Comment implements java.io.Serializable{
	
	private String cid;
	private String body;
	private Visibility commentVisibility;
	private LocalDateTime timestamp;
	private String owner;
	private Post post;				//necessary for many-to-one Hibernate relation mapping
	
	public Comment() {}
	
	public Comment(String body) {
		this.cid = Util.getRandomHexString();
		this.body = body;
		this.timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}
	
	//Getter and Setter
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Visibility getCommentVisibility() {
		return commentVisibility;
	}

	public void setCommentVisibility(Visibility commentVisibility) {
		this.commentVisibility = commentVisibility;
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

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;			//self check
		if (o == null) return false;		//null check 
		
		//type check and cast
		if (!(o instanceof Comment)) return false; 
		Comment comment = (Comment) o;
		
		//field comparison
		return Objects.equals(cid, comment.cid) && Objects.equals(body, comment.body)
				&& Objects.equals(owner, comment.owner);
	}

	@Override
	public String toString() {
		return "Comment [cid=" + cid + ", body=" + body + ", commentVisibility=" + commentVisibility + ", timestamp="
				+Util.getTimeString(timestamp) + ", owner=" + owner + ", postPid=" + post.getPid() + "]";
	}
	
	
	
}
