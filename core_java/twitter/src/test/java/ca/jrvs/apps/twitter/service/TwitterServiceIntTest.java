package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TwitterServiceIntTest {
    private final static String CONSUMER_KEY = System.getenv("consumerKey");
    private final static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private final static String ACCESS_TOKEN = System.getenv("accessToken");
    private final static String TOKEN_SECRET = System.getenv("tokenSecret");

    private TwitterDao dao;
    private TwitterService service;

    @Before
    public void setUp() throws Exception {
        TwitterHttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
        dao = new TwitterDao(httpHelper);
        service = new TwitterService(dao);
    }

    @Test
    public void postTweet() {
        String post = "Testing postTweet Service";
        Double longitude = 1d;
        Double latitude = -1d;
        List<Double> coordinateList = new ArrayList<>();
        coordinateList.add(longitude);
        coordinateList.add(latitude);
        Coordinates coordinates = new Coordinates();
        coordinates.setCoordinates(coordinateList);

        Tweet tweet = new Tweet();
        tweet.setText(post);
        tweet.setCoordinates(coordinates);
        Tweet postTweet = service.postTweet(tweet);

        assertEquals(postTweet.getText(), post);
        assertEquals(postTweet.getCoordinates().getCoordinates().get(0), longitude);
        assertEquals(postTweet.getCoordinates().getCoordinates().get(1), latitude);
    }

    @Test
    public void showTweet() {
        String[] fields = {"created_at", "id", "id_str", "text"};
        Tweet tweet = service.showTweet("1498379253892714505", fields);
        System.out.println(tweet.toString());
    }

    @Test
    public void deleteTweets() {
        String[] TweetID = new String[] {"1498713322983677952"};
        List<Tweet> deletedTweets= service.deleteTweets(TweetID);
        Assert.assertEquals(new ArrayList<Tweet>(), deletedTweets);
    }
}