package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class TraderAccountServiceIntTest {

    @Autowired
    private TraderAccountService traderAccountService;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private AccountDao accountDao;

    private TraderAccountView traderAccountView;

    @Before
    public  void setUp() {
        traderDao.deleteAll();

        Trader trader = new Trader();
        trader.setEmail("kmerhi@gmail.com");
        trader.setFirstName("Karl");
        trader.setLastName("Merhi");
        trader.setDob(new Date(978823));
        trader.setCountry("Canada");
        traderAccountView = traderAccountService.createTraderAndAccount(trader);
    }

    @Test
    public void createTraderAndAccount() {
        Trader trader = new Trader();
        trader.setEmail("kme@yahoo.ca");
        trader.setFirstName("Karl");
        trader.setLastName("Merhi");
        trader.setDob(new Date(978823));
        trader.setCountry("Canada");

        traderAccountView = traderAccountService.createTraderAndAccount(trader);
        Assert.assertEquals(trader, traderAccountView.getTrader());
    }

    @Test
    public void deposit() {
        Account account = traderAccountService.deposit(
                traderAccountView.getTrader().getId(), 500d);
        traderAccountView.setAccount(account);
        Assert.assertEquals(new Double (500), traderAccountView.getAccount().getAmount());
    }

    @Test
    public void withdraw() {
        deposit();
        Account account = traderAccountService.withdraw(
                traderAccountView.getTrader().getId(), 10d);
        traderAccountView.setAccount(account);

        Assert.assertEquals(new Double(490), traderAccountView.getAccount().getAmount());
    }

    @Test
    public void deleteTraderById() {
        Assert.assertTrue(traderDao.existsById(traderAccountView.getAccount().getTraderId()));
        traderAccountService.deleteTraderByID(traderAccountView.getAccount().getTraderId());
        Assert.assertFalse(traderDao.existsById(traderAccountView.getAccount().getTraderId()));
    }
}