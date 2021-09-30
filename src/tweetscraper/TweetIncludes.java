package tweetscraper;

//Our TweetIncludes object contains all of the information gotten from our Tweet's Includes field
public class TweetIncludes {
	private TweetUser[] tweetUsers;

	public TweetIncludes(TweetUser[] tweetUsers) {
		this.tweetUsers = tweetUsers;
	}

	public TweetUser[] getTweetUsers() {
		return tweetUsers;
	}
	public String toString() {
		String temp = null;
		int i = 0;
		while(tweetUsers[i]!=null) {
			if(temp == null)
				temp = tweetUsers[i].toString();
			else	
				temp += tweetUsers[i].toString();
			i++;
		}
		return temp;
	}
	public String firstElement() {
		return tweetUsers[0].toString();
	}
	public String getUsername() {
		return tweetUsers[0].getUsername();
	}
	public String getTwittername() {
		return tweetUsers[0].getName();
	}
}
