package tweetscraper;


//Our TweetMetrics object contains all of the information gotten from our Tweet's Metrics field
public class TweetMetrics {
	private int retweetCount;
	private int replyCount;
	private int likeCount;
	private int quoteCount;
	
	public TweetMetrics(int retweet, int reply, int like, int quote) {
		this.retweetCount = retweet;
		this.replyCount = reply;
		this.likeCount = like;
		this.quoteCount = quote;
	}
	
	public String toString() {
		return ("Metrics: "+retweetCount+"\t"+replyCount+"\t"+likeCount+"\t"+quoteCount+"\t");
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public int getQuoteCount() {
		return quoteCount;
	}
}
