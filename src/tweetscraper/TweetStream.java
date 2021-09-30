package tweetscraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class TweetStream {
	
	public TweetStream() {
		
	}
	
	/*The majority of this function was taken from a Twitter API example use here: 
	 * https://github.com/twitterdev/Twitter-API-v2-sample-code/blob/main/Filtered-Stream/FilteredStreamDemo.java*/
	
	//Set all of our rules and connect using our token provided by our Twitter Dev Account 
	public void initialize() throws IOException, URISyntaxException {
	    String bearerToken = System.getenv("BEARER_TOKEN");
	    if (null != bearerToken) {
	        Map<String, String> rules = new HashMap<>();
	        
	        //These rules will only include tweets that contain at least one of these keywords/sets of keywords.
	        //We are unable to use $ in our tweets because Twitter does not allow it with our current permissions, 
	        //so this is what I came up with
	        
	        //DASH is an actual word and was giving a lot of false positives. We added stock and it appeared to give us more relevant results
	        rules.put("DASH stock", "DASH");
	        rules.put("NKLA", "NKLA");
	        rules.put("BYND","BYND");
	        
	        rules.put("ARKK","ARKK");
	        rules.put("PYPL","PYPL");
	        rules.put("DKNG","DKNG");
	        rules.put("AAPPS","APPS");
	        rules.put("CRSR","CRSR");
	        rules.put("PINS stock","PINS");
	        rules.put("DTIL","DTIL");
	        rules.put("ICLN","ICLN");
	        rules.put("QCLN","QCLN");
	        rules.put("VALE stock","VALE");
	        rules.put("ARKW","ARKW");
	        rules.put("WYNN","WYNN");
	        rules.put("FVRR","FVRR");
	        rules.put("S&P 500","SPY");
	        rules.put("TSLA", "TSLA");
	        
	        rules.put("PLTR","PLTR");
	        rules.put("AAPL","AAPL");
	        rules.put("GOOGL","GOOGL");
	        rules.put("AMZN stock","AMZN");

	        //This includes all Tweets from the following users. Can also add a test twitter account if desired
	        rules.put("from: elonmusk", "elonmusk");
	        rules.put("from: jimcramer", "jimcramer");
	        rules.put("from: chamath", "chamath");
        
	        //Sets up the rules and connects our stream
	        setupRules(bearerToken, rules);
	        connectStream(bearerToken);
	      } else {
	        System.out.println("There was a problem getting your bearer token. Please make sure you set the BEARER_TOKEN environment variable");
	      }
	}
	
	//Connects our stream using HTTP gets with the uri. We can get more fields if we add them into our uri below
	 private void connectStream(String bearerToken) throws IOException, URISyntaxException {

		    HttpClient httpClient = HttpClients.custom()
		        .setDefaultRequestConfig(RequestConfig.custom()
		            .setCookieSpec(CookieSpecs.STANDARD).build())
		        .build();

		    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream?tweet.fields=created_at,lang,public_metrics&expansions=author_id&user.fields=created_at");

		    HttpGet httpGet = new HttpGet(uriBuilder.build());
		    httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));

		    HttpResponse response = httpClient.execute(httpGet);
		    HttpEntity entity = response.getEntity();
		    if (null != entity) {
		      BufferedReader reader = new BufferedReader(new InputStreamReader((entity.getContent())));
		      String line = null;
		      JSONParser jsp = new JSONParser();
		      do {
		    	try {
		    		//continue to read and parse lines from the stream as they come in
		    		line = reader.readLine();
		    		System.out.println(line);
		    		if(line.startsWith("{")) {
		    			MySqlDBHandler.insertTweet(jsp.parseTweet(line), this);
		    		}
		    	} catch (Exception exc) {
		    		System.err.println(exc.getMessage());
		    		exc.printStackTrace(System.err);
		    	}
		      } while (line != null);
		    }

		  }

		  /*
		   * Helper method to setup rules before streaming data
		   * */
		  private static void setupRules(String bearerToken, Map<String, String> rules) throws IOException, URISyntaxException {
		    List<String> existingRules = getRules(bearerToken);
		    if (existingRules.size() > 0) {
		      deleteRules(bearerToken, existingRules);
		    }
		    createRules(bearerToken, rules);
		  }

		  /*
		   * Helper method to create rules for filtering
		   * */
		  private static void createRules(String bearerToken, Map<String, String> rules) throws URISyntaxException, IOException {
		    HttpClient httpClient = HttpClients.custom()
		        .setDefaultRequestConfig(RequestConfig.custom()
		            .setCookieSpec(CookieSpecs.STANDARD).build())
		        .build();

		    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

		    HttpPost httpPost = new HttpPost(uriBuilder.build());
		    httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
		    httpPost.setHeader("content-type", "application/json");
		    StringEntity body = new StringEntity(getFormattedString("{\"add\": [%s]}", rules));
		    httpPost.setEntity(body);
		    HttpResponse response = httpClient.execute(httpPost);
		    HttpEntity entity = response.getEntity();
		    if (null != entity) {
		      System.out.println(EntityUtils.toString(entity, "UTF-8"));
		    }
		  }

		  /*
		   * Helper method to get existing rules
		   * */
		  private static List<String> getRules(String bearerToken) throws URISyntaxException, IOException {
		    List<String> rules = new ArrayList<>();
		    HttpClient httpClient = HttpClients.custom()
		        .setDefaultRequestConfig(RequestConfig.custom()
		            .setCookieSpec(CookieSpecs.STANDARD).build())
		        .build();

		    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

		    HttpGet httpGet = new HttpGet(uriBuilder.build());
		    httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
		    httpGet.setHeader("content-type", "application/json");
		    HttpResponse response = httpClient.execute(httpGet);
		    HttpEntity entity = response.getEntity();
		    if (null != entity) {
		      JSONObject json = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
		      if (json.length() > 1) {
		        JSONArray array = (JSONArray) json.get("data");
		        for (int i = 0; i < array.length(); i++) {
		          JSONObject jsonObject = (JSONObject) array.get(i);
		          rules.add(jsonObject.getString("id"));
		        }
		      }
		    }
		    return rules;
		  }

		  /*
		   * Helper method to delete rules
		   * */
		  private static void deleteRules(String bearerToken, List<String> existingRules) throws URISyntaxException, IOException {
		    HttpClient httpClient = HttpClients.custom()
		        .setDefaultRequestConfig(RequestConfig.custom()
		            .setCookieSpec(CookieSpecs.STANDARD).build())
		        .build();

		    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

		    HttpPost httpPost = new HttpPost(uriBuilder.build());
		    httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
		    httpPost.setHeader("content-type", "application/json");
		    StringEntity body = new StringEntity(getFormattedString("{ \"delete\": { \"ids\": [%s]}}", existingRules));
		    httpPost.setEntity(body);
		    HttpResponse response = httpClient.execute(httpPost);
		    HttpEntity entity = response.getEntity();
		    if (null != entity) {
		      System.out.println(EntityUtils.toString(entity, "UTF-8"));
		    }
		  }

		  private static String getFormattedString(String string, List<String> ids) {
		    StringBuilder sb = new StringBuilder();
		    if (ids.size() == 1) {
		      return String.format(string, "\"" + ids.get(0) + "\"");
		    } else {
		      for (String id : ids) {
		        sb.append("\"" + id + "\"" + ",");
		      }
		      String result = sb.toString();
		      return String.format(string, result.substring(0, result.length() - 1));
		    }
		  }

		  private static String getFormattedString(String string, Map<String, String> rules) {
		    StringBuilder sb = new StringBuilder();
		    if (rules.size() == 1) {
		      String key = rules.keySet().iterator().next();
		      return String.format(string, "{\"value\": \"" + key + "\", \"tag\": \"" + rules.get(key) + "\"}");
		    } else {
		      for (Map.Entry<String, String> entry : rules.entrySet()) {
		        String value = entry.getKey();
		        String tag = entry.getValue();
		        sb.append("{\"value\": \"" + value + "\", \"tag\": \"" + tag + "\"}" + ",");
		      }
		      String result = sb.toString();
		      return String.format(string, result.substring(0, result.length() - 1));
		    }
		  }

		
}
