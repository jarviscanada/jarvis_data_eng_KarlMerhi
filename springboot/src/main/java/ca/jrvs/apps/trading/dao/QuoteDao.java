package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class QuoteDao implements CrudRepository<Quote, String> {

    private static final String TABLE_NAME = "quote";
    private static final String ID_COLUMN_NAME = "ticker";

    private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public QuoteDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
    }

    @Override
    public Quote save(Quote quote) {

        if (existsById(quote.getTicker())){
            int updateRowNum = updateOne(quote);
            if (updateRowNum != 1){
                throw new DataRetrievalFailureException("Unable to update quote.");
            }
        } else {
            addOne(quote);
        }
        return quote;
    }

    private void addOne(Quote quote){
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(quote);
        int rowNo = simpleJdbcInsert.execute(parameterSource);
        if (rowNo != 1) {
            throw new IncorrectResultSizeDataAccessException("Failed to insert", 1, rowNo);
        }
    }

    /**
     * update quote information
     * @param quote
     */
    private int updateOne(Quote quote){
        String updateSQL = "UPDATE quote SET last_price=?, bid_price=?, bid_size=?, "
                + "ask_price=?, ask_size=? WHERE ticker=?";
        return jdbcTemplate.update(updateSQL, makeUpdateValues(quote));
    }

    private Object[] makeUpdateValues(Quote quote) {
        return new  Object[] {quote.getLastPrice(), quote.getBidPrice(), quote.getBidSize(),
                quote.getAskPrice(), quote.getAskSize(), quote.getTicker()};
    }

    @Override
    public <S extends Quote> List<S> saveAll(Iterable<S> quotes) {
        List<S> quotesOut = new ArrayList<>();
        for (Quote q : quotes) {
            quotesOut.add((S) save(q));
        }
        return (List<S>)quotesOut;
    }

    @Override
    public Optional<Quote> findById(String ticker) {
        String selectSql = "SELECT " + ID_COLUMN_NAME
                + " FROM " + TABLE_NAME
                + " WHERE " + ID_COLUMN_NAME + "= " + ticker;
        List<Quote> quotes =  jdbcTemplate
                .query(selectSql, BeanPropertyRowMapper.newInstance(Quote.class));
        if(quotes.size()==1){
            Quote outQuote = quotes.get(0);
            return Optional.ofNullable(outQuote);
        }else{
            throw new DataRetrievalFailureException("cannot Get data");
        }
    }

    @Override
    public boolean existsById(String ticker) {
        String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE ticker = '" + ticker + "'";
        List<Quote> quotes =  jdbcTemplate
                .query(selectSql, BeanPropertyRowMapper.newInstance(Quote.class));
        if(quotes.size()==1) {
            Quote outQuote = quotes.get(0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Quote> findAll() {
        String selectSql = "SELECT * FROM " + TABLE_NAME;
        List<Quote> quotes =  jdbcTemplate
                .query(selectSql, BeanPropertyRowMapper.newInstance(Quote.class));
        return quotes;
    }

    @Override
    public long count() {
        String sqlCount = "SELECT COUNT(*) FROM" + TABLE_NAME;
        return jdbcTemplate.queryForObject(sqlCount, Long.class);
    }

    @Override
    public void deleteById(String ticker) {
        if (ticker == null) {
            throw new IllegalArgumentException("ID can't be null");
        }
        String deleteSql = "DELETE FROM " + TABLE_NAME
                + " WHERE " + ID_COLUMN_NAME + " =?";
        jdbcTemplate.update(deleteSql, ticker);
    }

    @Override
    public void deleteAll() {
        String deleteSql = "DELETE FROM " + TABLE_NAME;
        jdbcTemplate.update(deleteSql);
    }

    @Override
    public Iterable<Quote> findAllById(Iterable<String> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(Quote quote) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends Quote> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
