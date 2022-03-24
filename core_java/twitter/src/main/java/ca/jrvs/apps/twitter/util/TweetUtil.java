package ca.jrvs.apps.twitter.util;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import com.google.gdata.util.common.base.PercentEscaper;

import java.util.Arrays;

public class TweetUtil {
    public static Tweet buildTweet(String text, Double longitude, Double latitude) {
        Tweet tweet = new Tweet();
        PercentEscaper percentEscaper = new PercentEscaper("", false);
        Coordinates coordinates = new Coordinates();
        coordinates.setCoordinates(Arrays.asList(longitude, latitude));
        coordinates.setType("Point");

        tweet.setText(percentEscaper.escape(text));
        tweet.setCoordinates(coordinates);
        return tweet;
    }


    public static Tweet createTweet(String text, double longitude, double latitude) {

        Coordinates geoCoordinates = new Coordinates();
        Tweet tweet = new Tweet();

        try {
            geoCoordinates.setCoordinates(Arrays.asList(longitude, latitude));
            geoCoordinates.setType("Point");
            tweet.setText(text);
            tweet.setCoordinates(geoCoordinates);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tweet;
    }
}
