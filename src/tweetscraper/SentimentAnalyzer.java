package tweetscraper;

import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;

public class SentimentAnalyzer {
	
	//Written by Ellis J.
	public static Entity analyzeEntitySetRule(String text, String stock, double salienceThreshold) throws Exception {
        // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
            AnalyzeEntitySentimentRequest request =
                    AnalyzeEntitySentimentRequest.newBuilder()
                            .setDocument(doc)
                            .setEncodingType(EncodingType.UTF16)
                            .build();
            // detect entity sentiments in the given string
            AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
            // Print the response
            for (Entity entity : response.getEntitiesList()) {
                if (entity.getName().contains(stock) && entity.getSalience() >= salienceThreshold) {
                    /*log.debug("Valid Sentiment Entity ------------");
                    log.debug("Entity: {}", entity.getName());
                    log.debug("Salience: {}", entity.getSalience());
                    log.debug("Sentiment : {}", entity.getSentiment());
                    log.debug("-----------------------------------");*/
                	System.out.println("Score: "+entity.getSentiment().getScore());
                	System.out.println("Magnitude: "+entity.getSentiment().getMagnitude());
                	System.out.println("Salience: "+entity.getSalience());
                    return entity;
                }
            }
        } catch (Exception e) {
            //log.error("{} -- {}", dateFormat.format(new Date()), e.getMessage());
            return null;
        }
        return null;
    }

}
