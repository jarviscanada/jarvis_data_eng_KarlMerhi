package ca.jrvs.apps.trading.dao;


import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * MarketDataDao is responsible for getting Quotes from IEX
 */
@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {

    private static final String IEX_BATCH_PATH = "stock/market/batch?symbols=%s&types=quote&token=";
    private final String IEX_BATCH_URL;

    private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
    private HttpClientConnectionManager httpClientConnectionManager;

    @Autowired
    public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
                         MarketDataConfig marketDataConfig){
        this.httpClientConnectionManager = httpClientConnectionManager;
        IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
    }

    /**
     * Get a single IexQuote
     * @param ticker
     * @return IexQuote
     * @throws IllegalArgumentException if ticker is invalid
     * @throws DataRetrievalFailureException if Http request fails
     */
    @Override
    public Optional<IexQuote> findById(String ticker) {
        Optional<IexQuote> iexQuote;
        List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

        if (quotes.size() == 0){
            return Optional.empty();
        } else if (quotes.size() ==1) {
            iexQuote = Optional.of(quotes.get(0));
        } else {
            throw new DataRetrievalFailureException("Unexpected number of quotes.");
        }
        return iexQuote;
    }


    /**
     * Get quotes from IEX
     * @param tickers
     * @return a list of IexQuote objects
     * @throws IllegalArgumentException if a any one ticker is invalid or if tickers is empty
     * @throws DataRetrievalFailureException if HTTP request failed
     */
    @Override
    public List<IexQuote> findAllById(Iterable<String> tickers) {

        String quoteString = null;
        IexQuote iexQuote = null;
        List<IexQuote> iexQuoteList = new ArrayList<>();
        Optional<String> httpResponseString = null;
        ObjectMapper mapper = new ObjectMapper();

        for(String ticker : tickers) {
            //if(!ticker.matches("[a-zA-Z]{2,4}"))
                //throw new IllegalArgumentException("Ticker format is invalid or ticker is empty");
        }
        String URI = String.format(IEX_BATCH_URL, String.join(",", tickers));
        System.out.print(URI);
        try {
            httpResponseString = executeHttpGet(URI);
        } catch (DataRetrievalFailureException e) {
            e.printStackTrace();
            throw e;
        }

        JSONObject jsonObject = new JSONObject(httpResponseString.get());

        for(String ticker : tickers) {
            quoteString = jsonObject.getJSONObject(ticker).getJSONObject("quote").toString();
            try {
                iexQuote = mapper.readValue(quoteString, IexQuote.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            iexQuoteList.add(iexQuote);
        }

        return iexQuoteList;
    }

    /**
     * Execute a get and return http entity/body as a string
     *
     * @param url resource URL
     * @return http response body or Optional.empty for 404 response
     * @throws DataRetrievalFailureException if HTTP failed or status code is unexpected
     */
    private Optional<String> executeHttpGet(String url) {

        HttpClient httpClient = getHttpClient();
        HttpGet request = new HttpGet(URI.create(url));
        request.addHeader("Content-Type", "application/json");

        HttpResponse httpResponse;

        try {
            httpResponse = httpClient.execute(request);
        } catch (IOException e) {
            throw new DataRetrievalFailureException("The Http request could not be completed");
        }

        int code = httpResponse.getStatusLine().getStatusCode();

        if (code == HttpStatus.SC_BAD_REQUEST)
            throw new DataRetrievalFailureException("Unable to deposit due to user input (Error 400)");
        else if(code == HttpStatus.SC_UNAUTHORIZED) // 401
            throw new DataRetrievalFailureException("Unauthorized (Error 401)");
        else if (code == HttpStatus.SC_FORBIDDEN) // 403
            throw new DataRetrievalFailureException("Forbidden (Error 403)");
        else if(code == HttpStatus.SC_NOT_FOUND) // 404
            return Optional.empty();

        Optional<String> httpResponseString = Optional.empty();

        try {
            httpResponseString = Optional.of(EntityUtils.toString(httpResponse.getEntity(), "UTF-8"));
        } catch (IOException e) {
            throw new DataRetrievalFailureException("Response could not be parsed to string");
        }

        return httpResponseString;
    }

    /**
     * Borrow a Http client form httpClientConnectionManager
     * @return httpClient
     */
    private CloseableHttpClient getHttpClient(){
        return HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .setConnectionManagerShared(true)
                .build();
    }

    @Override
    public <S extends IexQuote> S save(S s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean existsById(String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Iterable<IexQuote> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(IexQuote iexQuote) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends IexQuote> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
