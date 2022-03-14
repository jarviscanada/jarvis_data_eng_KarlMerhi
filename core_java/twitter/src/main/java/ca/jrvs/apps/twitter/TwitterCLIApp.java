package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterCLIApp {

    private Controller controller;

    @Autowired
    public TwitterCLIApp(Controller controller){

        this.controller = controller;
    }
}
