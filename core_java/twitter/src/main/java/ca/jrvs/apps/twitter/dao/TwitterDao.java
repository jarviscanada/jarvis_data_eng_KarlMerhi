package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterDao implements CrdDao{

    private HttpHelper httpHelper;

    @Autowired
    public TwitterDao(HttpHelper httpHelper){
        this.httpHelper = httpHelper;
    }
    @Override
    public Object create(Object entity) {
        return null;
    }

    @Override
    public Object findById(Object o) {
        return null;
    }

    @Override
    public Object deleteById(Object o) {
        return null;
    }
}
