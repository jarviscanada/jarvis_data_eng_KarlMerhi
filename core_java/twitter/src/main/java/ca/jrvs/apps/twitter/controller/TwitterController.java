package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TwitterController implements Controller {

    private static final String COORD_SEP = ";";
    private static final String COMMA = ";";

    private Service service;

    @Autowired
    public TwitterController(Service service) {
        this.service = service;
    }
    @Override
    public Tweet postTweet(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException(" USAGE: TwitterCLIApp post \"Tweet_text\" \"Latitude:Longitude\"");
        }
        String text = args[1];
        String coordinates = args[2];

        String[] coordindatesList = coordinates.split(COORD_SEP);

        if (coordindatesList.length != 2){
            throw new IllegalArgumentException("Inavlid Location Arguements \nUSAGE:\"Tweet_text\" \"Latitude:Longitude\"  ");
        }

        Double latitude = null;
        Double longitude = null;
        try {
            latitude = Double.parseDouble(coordindatesList[0]);
            longitude = Double.parseDouble(coordindatesList[1]);
        } catch (Exception e){
            throw new IllegalArgumentException(" Invalid Location format \nUSAGE: \"Tweet_text\" \"Latitude:Longitude\" ");
        }

        Tweet tweet = TweetUtil.createTweet(text, longitude, latitude);
        //service.postTweet(tweet);
        return tweet;
    }

    @Override
    public Tweet showTweet(String[] args) {

        if (args.length != 2){
            throw new IllegalArgumentException(" USAGE: TwitterCLIApp show \"Tweet_ID\" ");
        }
        String id = args[1];
        Tweet tweet = service.showTweet(id, null);
        return tweet;
    }

    @Override
    public List<Tweet> deleteTweet(String[] args) {

        if (args.length != 2){
            throw new IllegalArgumentException(" USAGE: TwitterCLIApp delete_if \"Tweet_IDS\" ");
        }
        String[] ids = args[1].split(COMMA);
        return service.deleteTweets(ids);
    }
}
