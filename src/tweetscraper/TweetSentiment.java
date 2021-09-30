package tweetscraper;

//Our TweetSentiment object contains all of the information gotten from our Google Sentiment Analysis
public class TweetSentiment {
	private float magnitude;
	private float score;
	private float salience;
	
	public TweetSentiment() {
		this.magnitude = 0;
		this.score = 0;
		this.salience = 0;
	}
	public TweetSentiment(float magnitude, float score, float salience) {
		this.magnitude = magnitude;
		this.score = score;		
	}
	public float getMagnitude() {
		return magnitude;
	}
	public void setMagnitude(float f) {
		this.magnitude = f;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public float getSalience() {
		return this.salience;
		
	}
	public void setSalience(float salience) {
		this.salience = salience;
		
	}
	public String toString() {
		return ("Sentiment: "+magnitude+"\t"+score+"\t");
	}

}
