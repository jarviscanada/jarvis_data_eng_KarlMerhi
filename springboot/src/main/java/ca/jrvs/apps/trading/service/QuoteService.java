package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class QuoteService {

    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private QuoteDao quoteDao;
    private MarketDataDao marketDataDao;

    @Autowired
    public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao){
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    /**
     * Helper method. Map an IexQuote object to a Quote object.
     *
     * Note:
     * - 'iexQuote.getLatestPrice == null' if the stock market is closed
     * - Make sure to set a default value for number field(s)
     * @param iexQuote
     * @return
     */
    protected static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
        Quote quote = new Quote();
        quote.setTicker(iexQuote.getSymbol());
        quote.setLastPrice(iexQuote.getLatestPrice());
        quote.setAskPrice(iexQuote.getIexAskPrice());
        quote.setAskSize(iexQuote.getIexAskSize());
        quote.setBidPrice(iexQuote.getIexBidPrice());
        quote.setBidSize(iexQuote.getIexBidSize());
        return quote;

    }

    /**
     * Update quote table against IEX source
     * - get all quotes from the db
     * - foreach ticker get iexQuote
     * - convert each iexQuote to a quote entity
     * - persist quote entities to db
     *
     * @return saved quotes
     */
    public List<Quote> updateMarketData() {

        List<Quote> quotes = quoteDao.findAll();
        List<Quote> result = new ArrayList<>();

        for (Quote quote : quotes) {
            String ticker = quote.getTicker();
            IexQuote iexQuote = marketDataDao.findById(ticker).get();
            Quote tempQuote = buildQuoteFromIexQuote(iexQuote);
            result.add(tempQuote);
            quoteDao.save(tempQuote);
        }
        return result;
    }

    /**
     * Validate (against IEX) and save given tickers to quote table.
     *
     * - Get iexQuote(s)
     * - convert each iexQuote to Quote entity
     * - persist the quote to db
     *
     * @param tickers a list of tickers/symbols
     * @throws IllegalArgumentException if ticker not found from Iex
     */
    public List<Quote> saveQuotes(List<String> tickers) {
        List<Quote> quoteList = new ArrayList<>();
        Quote quoteTemp = new Quote();

        List<IexQuote> iexQuoteList = marketDataDao.findAllById(tickers);

        for (IexQuote iexQuote : iexQuoteList) {
            quoteTemp = buildQuoteFromIexQuote(iexQuote);
            quoteList.add(quoteTemp);
            quoteDao.save(quoteTemp);
        }

        return quoteList;
    }

    /**
     * helper method
     * @param ticker
     * @return Quote
     */
    public Quote saveQuote(String ticker) {
        IexQuote iexQuote = new IexQuote();
        iexQuote = marketDataDao.findById(ticker).get();
        Quote quote = buildQuoteFromIexQuote(iexQuote);
        return quoteDao.save(quote);
    }

    /**
     * Find an IexQuote
     *
     * @param ticker id
     * @return IexQuote object
     * @throws IllegalArgumentException if ticker is invalid
     */
    public IexQuote findIexQuoteByTicker(String ticker){
        return marketDataDao.findById(ticker)
                .orElseThrow(() -> new IllegalArgumentException(ticker + " is invalid."));
    }

    /**
     * Update a given quote to quote table without validation
     * @param quote entity
     */
    public Quote saveQuote(Quote quote) { return quoteDao.save(quote);}

    /**
     * Find all quotes from the quote table
     * @return a list of quotes
     */
    public List<Quote> findAllQuotes() { return quoteDao.findAll();}
}
