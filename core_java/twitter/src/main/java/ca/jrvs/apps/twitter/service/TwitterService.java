package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TwitterService implements Service{
    private CrdDao <Tweet, String> dao;

    @Autowired
    public TwitterService(CrdDao dao) {
        this.dao = dao;
    }

    @Override
    public Tweet postTweet(Tweet tweet) {
        validatePostTweet(tweet);
        return dao.create(tweet);
    }

    @Override
    public Tweet showTweet(String id, String[] fields) {
        if (id == null) {
            throw new NullPointerException("No Id Provided!");
        }

        if (!id.matches("[0-9]+")) {
            throw new IllegalArgumentException("Incorrect ID format: " + id);
        }

        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Incorrect ID format : " + id);
        }

        return dao.findById(id);
    }

    @Override
    public List<Tweet> deleteTweets(String[] ids) {
        List<Tweet> deleteTweet = new ArrayList<>();

        for (String id : ids) {
            if (!id.matches("[0-9]+")) {
                throw new IllegalArgumentException("Tweet could not be deleted");
            } else {
                deleteTweet.add(dao.deleteById(id));
            }
        }
        return deleteTweet;
    }

    private void validatePostTweet(Tweet tweet) {
        int lengthTweet = tweet.getText().length();
        Double lon = tweet.getCoordinates().getCoordinates().get(0);
        Double lat = tweet.getCoordinates().getCoordinates().get(1);

        // check if the tweet text exceeds 140 characters
        if (lengthTweet > 140) {
            throw new IllegalArgumentException("Tweet characters exceeds 140 characters!");
        }

        // check if lon/lat is out of range
        if (lon <= -180 || lon >= 80) {
            throw new IllegalArgumentException("Longitude-out of range");
        }

        if (lat <= -90 || lat >= 90) {
            throw new IllegalArgumentException("Latitude-out of range");
        }
    }
}
