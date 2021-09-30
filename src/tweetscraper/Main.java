package tweetscraper;

import java.io.IOException;
import java.net.URISyntaxException;

/*
 * Sample code to demonstrate the use of the Filtered Stream endpoint
 * */
public class Main {
  // To set your enviornment variables in your terminal run the following line:
  // export 'BEARER_TOKEN'='<your_bearer_token>'

  public static void main(String args[]) throws IOException, URISyntaxException {
	  
	//Connect to our DB
	MySqlDBHandler.connectDb();
    
	//Create a new Tweet Stream and initialize it
	TweetStream ts = new TweetStream();
    ts.initialize();

  }
}
  /*
   * This method calls the filtered stream endpoint and streams Tweets from it
   * */
 