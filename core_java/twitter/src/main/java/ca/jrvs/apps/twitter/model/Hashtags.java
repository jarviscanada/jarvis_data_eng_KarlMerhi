package ca.jrvs.apps.twitter.model;

import java.util.List;

public class Hashtags {
    private List<Integer> indices;
    private String text;

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
