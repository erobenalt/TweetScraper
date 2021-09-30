package tweetscraper;

//Our TweetUser object contains all of the information gotten from our Tweet's user field
public class TweetUser {
	private String id;
	private String createdAt;
	private String username;
	private String name;
	
	public TweetUser(String id, String createdAt, String username, String name) {
		this.id = id;
		this.createdAt = createdAt;
		this.username = username;
		this.name = name;
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return ("User: "+id+"\t"+createdAt+"\t"+username+"\t"+name+"\t");
	}
	
}
