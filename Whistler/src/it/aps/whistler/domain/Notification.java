package it.aps.whistler.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import it.aps.whistler.util.Util;

@SuppressWarnings("serial")
public class Notification implements java.io.Serializable{

	private String nid;
	private String whistleblowerNickname;
	private String itemIdentifier;
	private LocalDateTime timestamp;
	private Account account;				//necessary for many-to-one Hibernate relation mapping
	
	public Notification() {}
	
	public Notification(String whistleblowerNickname, String itemIdentifier, LocalDateTime timestamp) {
		this.nid = Util.getRandomHexString();
		this.whistleblowerNickname = whistleblowerNickname;
		this.itemIdentifier = itemIdentifier;
		this.timestamp = timestamp;
	}
	
	//Getter and Setter
	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}
	
	public String getWhistleblowerNickname() {
		return whistleblowerNickname;
	}

	public void setWhistleblowerNickname(String whistleblowerNickname) {
		this.whistleblowerNickname = whistleblowerNickname;
	}

	public String getItemIdentifier() {
		return itemIdentifier;
	}

	public void setItemIdentifier(String itemIdentifier) {
		this.itemIdentifier = itemIdentifier;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;			//self check
		if (o == null) return false;		//null check 
		
		//type check and cast
		if (!(o instanceof Notification)) return false; 
		Notification notification = (Notification) o;
		
		//field comparison
		return Objects.equals(whistleblowerNickname, notification.whistleblowerNickname) &&
				Objects.equals(itemIdentifier, notification.itemIdentifier) &&
				Objects.equals(timestamp, notification.timestamp);
		
	}

	@Override
	public String toString() {
		return "Notification [whistleblowerNickname=" + whistleblowerNickname + ", itemIdentifier=" + itemIdentifier + ", timestamp="
				+ Util.getTimeString(timestamp) + "]";
	}

}
