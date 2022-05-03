package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql(scripts={"classpath:schema.sql"})
public class QuoteDaoIntTest {

    @Autowired
    private QuoteDao quoteDao;

    private Quote savedQuote;

    @Before
    public void insertOne() {
        savedQuote = new Quote();
        savedQuote.setAskPrice(10d);
        savedQuote.setAskSize(10);
        savedQuote.setBidPrice(10.2d);
        savedQuote.setBidSize(10);
        savedQuote.setId("aapl");
        savedQuote.setTicker("aapl");
        savedQuote.setLastPrice(10.1d);
        quoteDao.save(savedQuote);
    }


    @Test
    public void saveAll() throws Exception {
        Quote quote = new Quote();
        quote.setId(String.valueOf("abc"));
        quote.setAskPrice(10d);
        quote.setAskSize(10);
        quote.setBidPrice(10.2d);
        quote.setBidSize(10);
        quote.setLastPrice(10.1d);

        Quote quote_two = new Quote();
        quote_two.setId(String.valueOf("LOL"));
        quote_two.setAskPrice(10d);
        quote_two.setAskSize(10);
        quote_two.setBidPrice(10.2d);
        quote_two.setBidSize(10);
        quote_two.setLastPrice(10.1d);
        List<Quote> quoteList = new ArrayList<>();
        quoteList.add(quote);
        quoteList.add(quote_two);
        List<Quote> returnQuote = quoteDao.saveAll(quoteList);
        assertEquals(returnQuote, quoteList);
        quoteDao.deleteAll();
    }

    //
    @Test
    public void existsById() throws Exception {
        assertEquals(true, quoteDao.existsById(savedQuote.getId()));
    }


    @Test
    public void findAll() throws Exception {
        String ticker = savedQuote.getTicker();
        List<Quote> quotes = quoteDao.findAll();
        assertEquals(quotes.get(0).getAskPrice(), savedQuote.getAskPrice());
        assertEquals(quotes.get(0).getAskSize(), savedQuote.getAskSize());
        assertEquals(quotes.get(0).getBidPrice(), savedQuote.getBidPrice());
        assertEquals(quotes.get(0).getBidSize(), savedQuote.getBidSize());
        assertEquals(quotes.get(0).getTicker(), savedQuote.getTicker());
        assertEquals(quotes.get(0).getLastPrice(), savedQuote.getLastPrice());
    }

    @Test
    public void count() throws Exception {
        assertEquals(1, quoteDao.count());
    }

    @Test
    public void deleteById() throws Exception {
        //quoteDao.deleteById(savedQuote.getTicker());
        //assertEquals(1, quoteDao.count());
        quoteDao.deleteAll();
        assertEquals(0, quoteDao.count());
    }

    @After
    public void deleteOne() {
        quoteDao.deleteById(savedQuote.getId());
    }
}