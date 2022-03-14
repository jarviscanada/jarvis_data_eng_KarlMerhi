package ca.jrvs.apps.twitter.model;

import java.util.List;

public class Entities {
    private List<Hashtags> hastags;
    private List<UserMentions> user_mentions;

    public List<Hashtags> getHastags() {
        return hastags;
    }

    public void setHastags(List<Hashtags> hastags) {
        this.hastags = hastags;
    }

    public List<UserMentions> getUser_mentions() {
        return user_mentions;
    }

    public void setUser_mentions(List<UserMentions> user_mentions) {
        this.user_mentions = user_mentions;
    }
}
