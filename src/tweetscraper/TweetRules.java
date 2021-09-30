package tweetscraper;

//Our TweetRules object contains all of the information gotten from our Tweet's matching_rules field
public class TweetRules {
	private String tag;
	
	public TweetRules(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}
	public String toString() {
		return ("Rules: "+tag+"\t");
	}
}
