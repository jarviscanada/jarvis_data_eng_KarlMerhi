package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@Sql({"classpath:schema.sql"})
@SpringBootTest(classes = {TestConfig.class})
public class PositionDaoIntTest {

    @Autowired
    private SecurityOrderDao securityOrderDao;

    @Autowired
    private PositionDao positionDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private QuoteDao quoteDao;

    private SecurityOrder securityOrder;

    private Position position;

    private Account account;

    private Trader trader;

    private Quote quote;

    @Before
    public void setUp() {
        quoteDao.deleteAll();
        quote = new Quote();
        quote.setTicker("aapl");
        quote.setAskPrice(26.3);
        quote.setAskSize(52);
        quote.setBidPrice(55.6);
        quote.setBidSize(12);
        quote.setLastPrice(26.6);
        quoteDao.save(quote);

        traderDao.deleteAll();
        trader = new Trader();
        trader.setFirstName("John");
        trader.setLastName("Matic");
        trader.setCountry("Serbia");
        trader.setDob(new Date(123123));
        trader.setEmail("john.matic@mail.com");
        traderDao.save(trader);

        accountDao.deleteAll();
        account = new Account();
        account.setAmount(200.99);
        account.setTraderId(trader.getId());
        accountDao.save(account);

        securityOrderDao.deleteAll();
        securityOrder = new SecurityOrder();
        securityOrder.setTicker("aapl");
        securityOrder.setAccountId(securityOrder.getId());
        securityOrder.setAccountId(account.getId());
        securityOrder.setStatus("FILLED");
        securityOrder.setSize(100);
        securityOrder.setPrice(20.0);
        securityOrder.setNotes("Notes");
        securityOrderDao.save(securityOrder);

        position = new Position();
        position.setTicker("aapl");
        position.setAccountId(account.getId());
        position.setAccountId(1);
        position.setPosition(100);
    }

    @Test
    public void getByAccountIdAndTicker() {
        Position p = new Position();
        p = positionDao.findById(account.getId()).get();
        assertEquals(p.getPosition(), position.getPosition());
    }

    @After
    public void deleteAll() {
        securityOrderDao.deleteById(securityOrder.getId());
        accountDao.deleteById(account.getId());
        quoteDao.deleteById(quote.getTicker());
        traderDao.deleteById(trader.getId());
    }
}