package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
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
public class AccountDaoIntTest {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TraderDao traderDao;

    private Account account;

    private Trader trader;

    @Before
    public void insertOne() {
        traderDao.deleteAll();
        trader = new Trader();

        trader.setCountry("Canada");
        trader.setDob(new Date(78997389));
        trader.setEmail("karlm@gmail.com");
        trader.setFirstName("Karl");
        trader.setLastName("Merhi");
        traderDao.save(trader);

        accountDao.deleteAll();
        account = new Account();
        account.setTraderId(trader.getId());
        account.setAmount(2000.12);

        accountDao.save(account);
    }

    @Test
    public void findByTraderId() {
        assertEquals(account.getTraderId(),
                accountDao.findById(account.getId()).get().getTraderId());
        assertEquals(account.getAmount(),
                accountDao.findById(account.getId()).get().getAmount());
    }

    @Test
    public void updateOne() {
        account.setAmount(500.12);
        account.setTraderId(4);

        int changeNum = accountDao.updateOne(account);

        assertEquals(1, changeNum);
    }

    @After
    public void deleteAll() {
        accountDao.deleteAll();
    }
}