package tweetscraper;

//Our parent tweet object contains objects which includes all of the data fields we requested when we built the URI from our stream
//We can add or remove fields as required
public class Tweet {
	private TweetData data;
	private TweetIncludes includes;
	private TweetRules rules;
	private TweetMetrics metrics;
	private TweetSentiment sentiment;
	
	public Tweet(TweetData data, TweetIncludes includes,TweetRules rules, TweetMetrics metrics, TweetSentiment sentiment){
		this.data = data;
		this.includes = includes;
		this.rules = rules;
		this.metrics = metrics;
		this.sentiment = sentiment;
	}
	public void printTweet() {
		System.out.print(data.toString()+includes.toString()+rules.toString()+metrics.toString()+sentiment.toString()+"\n");
	}
	public TweetData getData() {
		return data;
	}
	public TweetIncludes getIncludes() {
		return includes;
	}
	public TweetRules getRules() {
		return rules;
	}
	public TweetMetrics getMetrics() {
		return metrics;
	}
	public TweetSentiment getSentiment() {
		return sentiment;
	}
}
