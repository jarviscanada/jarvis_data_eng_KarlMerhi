# Introduction
This application is meant to be used through the command line with the the 'Twitter REST API' to show, create or delete tweets by calling the Twitter Rest endpoints using Java. `TwitterHttpHelper` is used to send HTTP requests and receive responses, these requests are signed by using the OAuth 1.0 keys; consumer key/secret, access token, and token secret. These are all stored in the command lines environment variables. 

The Jackson JSON Java library is used in the project to process the JSON information from the API. The Spring Framework was used to manage the application dependencies. JUnit4 and Mockito was used to perform both integration 
and unit testing. A Docker image was built to deploy the application.

# Quick Start
### how to package app using mvn:
```
mvn clean compile package
java -jar target/twitter-1.0-SNAPSHOT.jar show|post|delete [options]
```
### how to run app with docker:
- Post a tweet
```
docker build -t twitter_app .
docker run --rm \
-e consumerKey=YOUR_CONSUMER_KEY \
-e consumerSecret=YOUR_CONSUMER_SECRET \
-e accessToken=YOUR_ACCESS_TOKEN \
-e tokenSecret=YOUR_TOKEN_SECRET \
twitter_app post tweet_text latitude:longitude
```

- Show a tweet
```
docker build -t twitter_app .
docker run --rm \
-e consumerKey=YOUR_CONSUMER_KEY \
-e consumerSecret=YOUR_CONSUMER_SECRET \
-e accessToken=YOUR_ACCESS_TOKEN \
-e tokenSecret=YOUR_TOKEN_SECRET \
twitter_app show tweet_id [field1,field2,...]
```

- Delete tweet(s)
```
docker build -t twitter_app .
docker run --rm \
-e consumerKey=YOUR_CONSUMER_KEY \
-e consumerSecret=YOUR_CONSUMER_SECRET \
-e accessToken=YOUR_ACCESS_TOKEN \
-e tokenSecret=YOUR_TOKEN_SECRET \
twitter_app delete [tweet_id1,tweet_id2,...]
```

# Design
## UML diagram
![UML Diagram](/core_java/twitter/assets/UML_Diagram.png)

### App/Main (TwitterCLIApp)
This application was designed using the MVC design pattern. The `main` method is the applications entry point, it checks that the user has provided valid arguments and then calls the controller layer.

### Controller (TwitterController)
The responsibility of the controller is to parse the user input from the command line and run the appropriate service method `[post|show|delete]`. Also checks if the number of arguments is correct and then calls the service layer and returns the tweet(s) back to `TwitterCLIApp`.

### Service (TwitterService)
The servic layer is responsible for the applications business logic, this includes validations such as checking that the length of the tweet doesn't exceed 140 characters and the longitude and latitude is within a certain range. It then calls the DAO layer and returns the result.

### DAO
The DAO layer is responsible for executing HTTP with a given URI, passed either to the `post` or `get` methods within `TwitterHttpHelper`. It also authorizes the HTTP request using the OAuth 1.0 keys; consumer key/secret, access token, and token secret. It sends the HTTP requests and receives the responses from the Twitter REST server. I also handles errors from the server by checking the response status code, it then parses the response into a JSON string and returns it to the top layer.

### TwitterHttpHelper
`TwitterHttpHelper` uses `HttpClientBuilder` to create a http client and executes a request with the uri provided as an argument.
There is one method for `post` and one method for `get`. 

## Models
<img src="./assets/TwitterERDModel.png"/>

## Spring
The dependencies in the application were managed using Spring. The first of two methods used within the Spring Framework is the `@Beans` aproach implemented within `TwitterCLIBean`. This defines the dependency relationships using the `@Bean` annotation, which passes the dependencies through the  method arguments. 

The second method used within the Spring Framework is the `@ComponentScan` aproach implemented within `TwitterCLIComponentScan`. This defines the dependency relationships using the `@Autowired` annotation, which is used to tell the Inversion of Control (IOC) container to inject the dependencies through the constructors. 

# Test
The applications testing was done using JUnit 4 for each method. Mockito was used to test isolated components.

## Deployment
The application was dockerized and deployed onto a docker hub repository under 'karlmerhi/twitterapi'

# Improvements
- Imporvement 1: Implement a clean GUI
- Imporvement 2: Add extra functionality i.e search for tweets
- Imporvement 3: Implement a better way to display data using the JSON data
