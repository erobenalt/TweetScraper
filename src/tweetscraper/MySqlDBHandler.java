package tweetscraper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import com.google.cloud.language.v1.Entity;


import com.google.cloud.language.v1.Sentiment;

public class MySqlDBHandler {
	
	
	private static boolean dbConnected = false;
	private static Connection con = null;
	private static String table = "tweetTable";
	
	//Store sensitive information in our system environment variables
	private static String dbLocation = System.getenv("TS_DB_LOCATION");//jdbc:mysql://127.0.0.1:3306/tweet_db
	private static String dbUser = System.getenv("TS_DB_USER");
	private static String dbPass = System.getenv("TS_DB_PASS");
	
	//Attempts to connect to the DB and prints error if it fails
	public static void connectDb() {
		try{
			//Attempts to connect using our db location, user, and password set in our system environment variables 
			con=DriverManager.getConnection(  
					System.getenv("TS_DB_LOCATION"),System.getenv("TS_DB_USER"),System.getenv("TS_DB_PASS"));  
			dbConnected = true;
		}catch(Exception e){
			System.out.println("Error detected");
			e.printStackTrace();
		} 
	}
	
	//Attempts to disconnect from our DB
	public static void disconnectDb() {
		if(dbConnected) {
			try {
				con.close();
				dbConnected = false;
			} catch (SQLException e) {
				System.out.println("Failed to disconnect from database");
				e.printStackTrace();
			}
		} else {
			System.out.println("Database is not currently connected");
		}
	}
	
	//Function used in the past to sort through DB tweets and change some of the sentiment information
	public static void fixSentiment() throws IOException {
		try {
			Statement stmt=con.createStatement();
			for (int i=24648; i<35791;i++) {
				ResultSet rs=stmt.executeQuery("SELECT tweetText,entryNo FROM sql_testdb."+table+" WHERE entryno = "+i+";");
				if (rs.next()) {
					rs.getInt(2);
					try {
						//Sentiment s = SentimentAnalyzer.analyzeEntitySetRule(rs.getString(1));
						//stmt.executeUpdate("UPDATE tweetTable SET magnitude = '"+String.valueOf(s.getMagnitude())+"',score = '"+String.valueOf(s.getScore())+"' WHERE entryNo = "+rs.getInt(2)+";");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch(SQLException e) {
			System.out.println("Error printing contents of table");
			e.printStackTrace();
		}
	}
	
	//Function used in the past to turn our datetime from tweets into date in milliseconds
	public static void fixTime() throws IOException {
		try {
			Statement stmt=con.createStatement();
			for (int i=1; i<60576;i++) {
				ResultSet rs=stmt.executeQuery("SELECT entryNo,createdAt FROM sql_testdb."+table+" WHERE entryno = "+i+";");
				if (rs.next()) {
					rs.getInt(1);
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						Long dateInMilli = (long) 0;
						try {
							dateInMilli = sdf.parse(rs.getString(2)).getTime();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						stmt.executeUpdate("UPDATE tweetTable SET dateInMilli="+dateInMilli+" WHERE entryNo = "+rs.getInt(1)+";");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch(SQLException e) {
			System.out.println("Error printing contents of table");
			e.printStackTrace();
		}
	}
	
	//Function which prints everything in the table
	public static void printAll() {
		try {
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("SELECT * FROM sql_testdb."+table);  
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"  "+rs.getString(2)); 
		}
		} catch(SQLException e) {
			System.out.println("Error printing contents of table");
			e.printStackTrace();
		}
	}
	
	//inserts tweets into the database if they fit our criteria
	public static void insertTweet(Tweet tw, TweetStream ts) throws Exception {
		String copyPasteTweet = "Participate in the April #Webull wheel event";
		//exclude tweets that are not english
		if(!tw.getData().getLang().equals("en")) {
			System.out.println("Non-english tweets are not added to the database");
			return;
		}
		//exclude tweets that are not originals
		if(tw.getMetrics().getRetweetCount() > 0) {
			System.out.println("Retweets are not added to the database");
			return;
		}
		
		//Used to exclude any tweet which is a common copy and paste tweet
		if(tw.getData().getText().contains(copyPasteTweet)) {
			System.out.println("Copy and paste tweets are not added to the database");
			return;
		}
		
		//Replace any ' with a '' to avoid an SQL injection from Twitter
		String text= tw.getData().getText().replace("'", "''");
		
		//get our tweet fields from our tweet object tw
		String id = tw.getData().getId();
		String authorId = tw.getData().getAuthorId();
		String createdAt = tw.getData().getCreatedAt();
		
		//get our datetime format and convert it into ms for future convenience
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Long dateInMilli = (long) 0;
		try {
			dateInMilli = sdf.parse(createdAt).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//get more tweet fields from our tweet object tw
		String username = tw.getIncludes().getUsername().replace("'", "''");
		String twittername = tw.getIncludes().getTwittername().replace("'", "''");
		String tag = tw.getRules().getTag();
		int retweetCount = tw.getMetrics().getRetweetCount();
		int replyCount = tw.getMetrics().getReplyCount();
		int likeCount = tw.getMetrics().getLikeCount();
		int quoteCount = tw.getMetrics().getQuoteCount();
		
		//anaylyze the sentiment using
		//Entity e = SentimentAnalyzer.analyzeEntitySetRule(tw.getData().getText(),tag,0.1);
		
		//salience is used to determine the relevance of the tweet. No Salience implies no relevance and we ignore the tweet
		/*if (e == null) {
			System.out.println("Salience was not high enough");
			return;
		}*/
		
		//Set our sentiment fields in our tweet object for later use, and put them into a variable
		//Sentiment s = e.getSentiment();
		//tw.getSentiment().setMagnitude(s.getMagnitude());
		//tw.getSentiment().setScore(s.getScore());
		//tw.getSentiment().setSalience(e.getSalience());
		
		float salience = 0.00f;//tw.getSentiment().getSalience();
		float magnitude = 0.00f;//tw.getSentiment().getMagnitude();
		float score = 0.00f;//tw.getSentiment().getScore();
		


		//Now that we have all of our variables, we can finally craft our SQL insert
		String finalInsert = "INSERT INTO "+table+"(tweettext,id,authorId,createdAt,dateinmilli,username,twittername,matchingrule,retweetcount,replycount,likecount,quotecount,magnitude,score,salience) VALUES ('"
				+text+"','"
				+id+"','"
				+authorId+"','"
				+createdAt+"','"
				+dateInMilli+"','"
				+username+"','"
				+twittername+"','"
				+tag+"','"
				+retweetCount+"','"
				+replyCount+"','"
				+likeCount+"','"
				+quoteCount+"','"
				+magnitude+"','"
				+score+"','"
				+salience+"'"
				+")";
		try {
			//attempt to execute our above insert
			Statement stmt=con.createStatement();  
			stmt.executeUpdate(finalInsert); 
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	//Finds our average sentiment over a given period of time depending on our lowerBound and upperBound Instant objects
	public static float avgSentiment(String rule, Instant lowerBound, Instant upperBound) {
		float total = 0;
		float average = 0;
		int n=0;
		
		try {
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("SELECT magnitude, score FROM sql_testdb."+table+" WHERE dateInMilli>"+lowerBound.toEpochMilli()+" AND dateInMilli<"+upperBound.toEpochMilli()+" AND matchingrule='"+rule+"'");  
			while(rs.next()) {
				//System.out.println(rs.getFloat(1)+" "+rs.getFloat(2));
				total += rs.getFloat(1)*rs.getFloat(2);
				n++;
		}
		} catch(SQLException e) {
			System.out.println("Error printing contents of table");
			e.printStackTrace();
		}
		average = total/n;
		return average;
	}
}  

