package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MarketDataDaoIntTest {

    private MarketDataDao dao;

    @Before
    public void init() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);

        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/v1/");
        //marketDataConfig.setToken(System.getProperty("IEX_PUB_TOKEN"));
        marketDataConfig.setToken("sk_bf9c12a648c347dba28544a58c5ca09b");

        dao = new MarketDataDao(cm, marketDataConfig);
    }

    @Test
    public void findByTicker() {
        String ticker = "AAPL";
        IexQuote iexQuote = dao.findById(ticker).get();
        Assert.assertEquals(ticker, iexQuote.getSymbol());
    }

    @Test
    public void findIexQuotesByTickers() {
        List<IexQuote> quoteList = dao.findAllById(Arrays.asList("AAPL", "FB"));
        Assert.assertEquals(2, quoteList.size());
        Assert.assertEquals("AAPL", quoteList.get(0).getSymbol());
        Assert.assertEquals("FB", quoteList.get(1).getSymbol());
    }
}