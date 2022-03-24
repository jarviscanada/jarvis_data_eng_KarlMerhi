package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import com.google.gdata.util.common.base.PercentEscaper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;

public class TwitterDao implements CrdDao<Tweet, String>{

    // URI constants
    private static final String API_BASE_URI = "https://api.twitter.com";
    private static final String POST_PATH = "/1.1/statuses/update.json";
    private static final String SHOW_PATH = "/1.1/statuses/show.json";
    private static final String DELETE_PATH = "/1.1/statuses/destroy";
    // URI symbols
    private static final String QUERY_SYM = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "==";

    // Response code
    private static final int HTTP_OK = 200;
    private HttpHelper httpHelper;

    @Autowired
    public TwitterDao(HttpHelper httpHelper){
        this.httpHelper = httpHelper;
    }
    @Override
    public Tweet create(Tweet tweet) {
        URI uri;

        PercentEscaper percentEscaper = new PercentEscaper("", false);
        String statusText = "status" + EQUAL + percentEscaper.escape(tweet.getText());
        uri = URI.create(API_BASE_URI + POST_PATH + QUERY_SYM + statusText);

        HttpResponse response = httpHelper.httpPost(uri);

        return parseResponseBody(response, HTTP_OK);
    }

    @Override
    public Tweet findById(String tweetID) {
        URI uri = URI.create(API_BASE_URI + DELETE_PATH + "id" + EQUAL + tweetID);
        return parseResponseBody(httpHelper.httpGet(uri), HTTP_OK);
    }

    @Override
    public Tweet deleteById(String tweetID) {
        URI uri = URI.create(API_BASE_URI + DELETE_PATH + "id" + EQUAL + tweetID);
        return parseResponseBody(httpHelper.httpPost(uri), HTTP_OK);
    }

    /**
     * Check response status code Convert Response Entity to Tweet
     */
    private Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode) {
        Tweet tweet = null;

        // Check response status
        int status = response.getStatusLine().getStatusCode();
        if (status!= expectedStatusCode) {
            try {
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                System.out.println("Response has no entity");
            }
            throw new RuntimeException("Unexpected HTTP status" + status);
        }

        if (response.getEntity() == null) {
            throw new RuntimeException("Empty response body");
        }

        // Convert Response Entity to str
        String jsonStr;
        try {
            jsonStr = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert entity to String", e);
        }

        // Deser JSON string to Tweet object
        try {
            tweet = JsonUtil.toObjectFromJson(jsonStr, Tweet.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert JSON str to Object", e);
        }

        return tweet;
    }
}
