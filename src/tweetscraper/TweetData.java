package tweetscraper;

//Our TweetData object contains all of the information gotten from our Tweet's Data field
public class TweetData {
	private String text;
	private String lang;
	private String authorId;
	private String id;
	private String createdAt; // TODO: convert to a date later
	
	public TweetData(String text, String lang, String authorId, String id, String createdAt) {
		this.text = text;
		this.lang = lang;
		this.authorId = authorId;
		this.id = id;
		this.createdAt = createdAt;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
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

	public String toString() {
		return ("Data: "+text+"\t"+lang+"\t"+authorId+"\t"+id+"\t"+createdAt+"\t");
	}

	public String getLang() {
		return lang;
	}

}
