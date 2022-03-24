package ca.jrvs.apps.twitter.dao.helper;

import junit.framework.TestCase;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.net.URI;

public class TwitterHttpHelperTest extends TestCase {

    public void httpPost() throws Exception {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");

        System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
        //Create components
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
        HttpResponse response = httpHelper.
                httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=GoodMorning"));
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    public void httpGet() throws Exception{
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");

        System.out.println(consumerKey + " | " + consumerSecret + " | " + accessToken + " | " + tokenSecret);

        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
        HttpResponse response = httpHelper.
                httpGet(new URI("https://api.twitter.com/1.1/search/tweets.json?q=1498362736388063235"));
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}