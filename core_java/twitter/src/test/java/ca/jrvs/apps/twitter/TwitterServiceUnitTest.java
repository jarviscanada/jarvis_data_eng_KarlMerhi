package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.model.Tweet;

@RunWith(MoclkitoJUnitERunner.class)
public class TwitterServiceUnitTest {
    @Mock
    CrdDao dao;

    @InjectMocks
    TwitterServiceImp Service;

    @Test
    public void postTweet() {
        when(dao.create(any())).thenReturn(new Tweet());
        service.postTweet(TweetUtil.buildTweet("test", 50.0, 0.0));
    }
}
