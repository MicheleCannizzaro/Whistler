package it.aps.whistler.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import it.aps.whistler.persistence.dao.KeywordDao;
import it.aps.whistler.persistence.dao.PostDao;

@SuppressWarnings("serial")
public class Keyword implements java.io.Serializable {
	
	private String word;
	private int diffusionRate;
	private Set<Post> posts = new HashSet<>(0);  //necessary for many-to-many Hibernate relation mapping
	
	public Keyword() {}
	
	public Keyword(String word) {
		this.word = word;
		this.diffusionRate = 0;
	}

	// Getter and Setter
	public void setWord(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}
	
	public int getDiffusionRate() {
		return diffusionRate;
	}
	
	public void setDiffusionRate(int diffusionRate) {
		this.diffusionRate = diffusionRate;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}
	
	public void addPosts(String pid) {						//necessary for persistence
		Post p = PostDao.getInstance().getPostByPid(pid);
		this.posts.add(p);
		KeywordDao.getInstance().updateKeyword(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;			//self check
		if (o == null) return false;		//null check 
		
		// type check and cast
		if (!(o instanceof Keyword)) return false; 
		Keyword keyword = (Keyword) o;
		
		//field comparison
		return Objects.equals(this.word, keyword.word);
		
	}

	@Override
	public String toString() {
		return this.word;
	}
	
}
