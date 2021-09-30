package tweetscraper;

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class JSONParser {
	
	//Parses our tweet and turns it into a Tweet Object
	public Tweet parseTweet(String tweetJSON) throws IOException {

		JsonReader jsonReader = Json.createReader(new StringReader(tweetJSON));
		JsonObject tweetObj = jsonReader.readObject();
		TweetData td;
		TweetIncludes ti;
		TweetRules tr;
		TweetMetrics tm;
		TweetSentiment ts = new TweetSentiment();
		
		//Gets our data fields out of our JSON String and sets the fields of our TweetData and TweetMetrics Object
		if (tweetObj.containsKey("data")) {		
			JsonObject dataObj = tweetObj.getJsonObject("data");

			String tweetText = dataObj.getString("text");
			String tweetLang = dataObj.getString("lang");
			String tweetAuthorId = dataObj.getString("author_id");
			String tweetId = dataObj.getString("id");;
			String tweetCreatedAt = dataObj.getString("created_at");

			td = new TweetData(tweetText,tweetLang,tweetAuthorId,tweetId,tweetCreatedAt);
			tm = null;

			JsonObject metricObj = dataObj.getJsonObject("public_metrics");
			int retweetCount;
			int replyCount;			
			int likeCount;
			int quoteCount;
				
			retweetCount = metricObj.getInt("retweet_count");
			replyCount = metricObj.getInt("reply_count");
			likeCount = metricObj.getInt("like_count");
			quoteCount = metricObj.getInt("quote_count");
			tm = new TweetMetrics(retweetCount,replyCount,likeCount,quoteCount);
			
			
		}else {
			td = null;
			tm = null;
		}
		//Gets our includes fields out of our JSON String and sets the fields of our TweetIncludes Object
		if (tweetObj.containsKey("includes")) {		
			JsonObject includesObj = tweetObj.getJsonObject("includes");
			//System.out.println(includesObj);			
			JsonArray userArray = includesObj.getJsonArray("users");
			String tweetId = userArray.get(0).asJsonObject().getString("id");
			String tweetCreatedAt =userArray.get(0).asJsonObject().getString("created_at");
			String tweetUsername = userArray.get(0).asJsonObject().getString("username");
			String tweetName = userArray.get(0).asJsonObject().getString("name");

				TweetUser[] users = new TweetUser[10];
				users[0] = new TweetUser(tweetId,tweetCreatedAt,tweetUsername,tweetName);
				ti = new TweetIncludes(users);
			}else {
				ti = null;
			}
		//Gets our rules fields out of our JSON String and sets the fields of our TweetRules Object
		if (tweetObj.containsKey("matching_rules")) {	
			JsonArray rulesObj = tweetObj.getJsonArray("matching_rules");
			String tweetTag = null;	
			tweetTag = rulesObj.get(0).asJsonObject().getString("tag");
			tr = new TweetRules(tweetTag);
			
		}else {
			tr = null;
		}

		//Create a new tweet by passing our above objects into our constructor
		return (new Tweet(td,ti,tr,tm,ts));
	}
}
