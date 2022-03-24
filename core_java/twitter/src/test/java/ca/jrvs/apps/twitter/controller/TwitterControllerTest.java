package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TwitterControllerTest {
    private final static String CONSUMER_KEY = System.getenv("consumerKey");
    private final static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private final static String ACCESS_TOKEN = System.getenv("accessToken");
    private final static String TOKEN_SECRET = System.getenv("tokenSecret");

    private TwitterDao dao;
    private TwitterService service;
    private TwitterController controller;

    @Before
    public void setUp() throws Exception {
        TwitterHttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
        dao = new TwitterDao(httpHelper);
        service = new TwitterService(dao);
        controller = new TwitterController(service);
    }

    @Test
    public void postTweet() {
        String[] args = new String[] {"post", "post tweet using controller test","43:79"};
        Tweet tweet = controller.postTweet(args);
        assertNotNull(tweet);
        assertEquals(tweet.getText(), args[1]);
    }

    @Test
    public void showTweet() {
        String text = "No one can make you feel inferior without your consent";
        String[] args = new String[] {"show", "1498379253892714505"};
        Tweet tweet = controller.showTweet(args);
        Assert.assertNotNull(tweet);
        Assert.assertEquals(tweet.getText(), text);
    }

    @Test
    public void deleteTweet() {
        String[] args = new String[] {"delete", "1498743824008503303"};
        List<Tweet> tweets = controller.deleteTweet(args);

        Assert.assertNotNull(tweets);
    }
}