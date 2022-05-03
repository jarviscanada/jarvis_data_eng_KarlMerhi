package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteDao quoteDao;

    private Quote quote;

    @Before
    public void setUp() {
        quoteDao.deleteAll();
        quote = new Quote();

        quote.setAskPrice(1d);
        quote.setAskSize(2);
        quote.setBidPrice(6.9d);
        quote.setBidSize(7);
        quote.setTicker("GAM");
        quote.setLastPrice(55.2d);
        quoteDao.save(quote);
    }

    @Test
    public void updateMarketData() {
        quoteService.updateMarketData();
        Assert.assertEquals(1, quoteDao.count());
        Assert.assertTrue(quoteDao.existsById(quote.getTicker()));
    }

    @Test
    public void saveQuotes() {
        List<String> tickers =  new ArrayList(){{
            add("TSLA");
            add("AMZN");
        }};
        List<Quote> savedQuotes = quoteService.saveQuotes(tickers);
        Assert.assertNotNull(savedQuotes);
        Assert.assertEquals(2, savedQuotes.size());
    }

    @Test
    public void saveQuote() {
        Quote quote = new Quote();
        quote.setAskPrice(1d);
        quote.setAskSize(2);
        quote.setBidPrice(6.9d);
        quote.setBidSize(7);
        quote.setTicker("JNJ");
        quote.setLastPrice(55.2d);
        Quote savedQuote  = quoteService.saveQuote(quote);

        Assert.assertNotNull(savedQuote);
        Assert.assertEquals(quote.getAskPrice(), savedQuote.getAskPrice());
        Assert.assertEquals(quote.getAskSize(), savedQuote.getAskSize());
        Assert.assertEquals(quote.getBidPrice(), savedQuote.getBidPrice());
        Assert.assertEquals(quote.getBidSize(), savedQuote.getBidSize());
        Assert.assertEquals(quote.getTicker(), savedQuote.getTicker());
        Assert.assertEquals(quote.getLastPrice(), savedQuote.getLastPrice());
    }

    @Test
    public void findIexQuoteByTicker() {
        IexQuote iexQuote = quoteService.findIexQuoteByTicker("AAPL");
        Assert.assertEquals("AAPL", iexQuote.getSymbol());
        Assert.assertEquals("Apple Inc", iexQuote.getCompanyName());
    }

    @Test
    public void findAllQuotes() {
        List<Quote> quoteList = quoteService.findAllQuotes();
        Assert.assertNotNull(quoteList);
        Assert.assertEquals(1, quoteList.size());
        Assert.assertEquals("GAM", quoteList.get(0).getTicker());
    }
}