package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.InjectMocks;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

    @Mock
    HttpHelper mockHelper;

    @InjectMocks
    TwitterDao dao;

    @Test
    public void showTweet() throws Exception {
        //test failed request
        String hashtag = "#abc";
        String text = "@someone sometext " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        //exception is expected here
        when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException(("mock")));
        try {
            dao.create(TweetUtil.buildTweet(text, lon, lat));
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        //Test happy path
        //however, we don't to call parseResponseBody.
        //we will make a spyDao which can fake parseResponseBody return value
        String tweetJsonStr = "{\n"
                + "    \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
                + "    \"id\":1097607853932564480,\n"
                + "    \"id_str\":\"1097607853932564480\",\n"
                + "    \"text\":\"test with loc223\",\n"
                + "    \"entities\":{\n"
                + "       \"hashtags\":[],"
                + "       \"user_mentions\":[]"
                + "    },\n"
                + "    \"coordinates\":null,"
                + "    \"retweet_count\":0,\n"
                + "    \"favorite_count\":0,\n"
                + "    \"favorited\":false,\n"
                + "    \"retweeted\":false\n"
                + "}";

        when(mockHelper.httpPost(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);
        //mock parseResponseBody
       // doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.create(TweetUtil.buildTweet(text, lon, lat));
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
    }
}
