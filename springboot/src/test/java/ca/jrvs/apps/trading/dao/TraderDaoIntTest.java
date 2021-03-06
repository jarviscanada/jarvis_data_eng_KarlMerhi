package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Trader;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Sql({"classpath:schema.sql"})
@SpringBootTest(classes = {TestConfig.class})
public class TraderDaoIntTest {

    @Autowired
    private TraderDao traderDao;

    private Trader trader;


    @Before
    public void insertOne() {

        trader = new Trader();

        trader.setId(1);
        trader.setFirstName("John");
        trader.setLastName("Simmons");
        trader.setCountry("Canada");
        trader.setDob(new Date(78997989));
        trader.setEmail("john.simmons@gmail.com");

        traderDao.save(trader);
    }

    @Test
    public void test() {
        assertTrue(traderDao.existsById(1));
    }

//    @Test
//    public void updateOne() {
//        String firstName = "Jamie";
//        String lastName = "Matic";
//        String country = "Canada";
//        trader.setFirstName(firstName);
//        trader.setLastName(lastName);
//        trader.setCountry(country);
//
//        int changeNum = traderDao.updateOne(trader);
//
//        assertEquals(1, changeNum);
//    }

    @Test
    public void findAllById() {

        List<Trader> traders = Lists.newArrayList(
                traderDao.findAllById(Arrays.asList(trader.getId()))
        );

        assertEquals(trader.getFirstName(), traders.get(0).getFirstName());
        assertEquals(trader.getLastName(), traders.get(0).getLastName());
        assertEquals(trader.getCountry(), traders.get(0).getCountry());
        assertEquals(trader.getEmail(), traders.get(0).getEmail());
    }

    @Test
    public void saveAll() {
        List<Trader> traders = new ArrayList<Trader>();
        Trader trader1 = new Trader();
        trader1.setFirstName("T1");
        trader1.setLastName("D1");
        trader1.setCountry("Canada");
        trader1.setDob(new Date(78997989));
        trader1.setEmail("T1@mail.com");

        Trader trader2 = new Trader();
        trader2.setFirstName("T2");
        trader2.setLastName("D2");
        trader2.setCountry("Canada");
        trader2.setDob(new Date(78997989));
        trader2.setEmail("T2@mail.com");

        traders.add(trader1);
        traders.add(trader2);
        Iterable<Trader> savedTraders = traderDao.saveAll(traders);

        assertNotNull(savedTraders);
        assertEquals(3, traderDao.count());
    }

    @Test
    public void findById() {
        Trader savedTrader = traderDao.findById(trader.getId()).get();
        assertEquals(trader.getFirstName(), savedTrader.getFirstName());
        assertEquals(trader.getLastName(), savedTrader.getLastName());
        assertEquals(trader.getCountry(), savedTrader.getCountry());
        assertEquals(trader.getEmail(), savedTrader.getEmail());
    }

    @Test
    public void existsById() {
        List<Trader> traders = Lists.newArrayList(traderDao.findAll());
        for(Trader trader: traders) {
            System.out.println("Id:" + trader.getId());
        }
        assertTrue(traderDao.existsById(trader.getId()));
    }

    @Test
    public void findAll() {

        List<Trader> traders = traderDao.findAll();
        assertEquals(1, traders.size());

        Trader newTrader = new Trader();
        newTrader.setFirstName("Karl");
        newTrader.setLastName("Merhi");
        newTrader.setCountry("Canada");
        newTrader.setDob(new Date(78997989));
        newTrader.setEmail("karlmerhi99@gmail.com");
        traderDao.save(newTrader);

        List<Trader> trader1 = traderDao.findAll();
        assertEquals(2, trader1.size());
    }

    @Test
    public void count() {
        assertEquals(1, traderDao.count());
    }

    @After
    public void deleteAll() {
        traderDao.deleteAll();
        assertEquals(0, traderDao.count());
    }
//    public void deleteOne() {
//        traderDao.deleteById(trader.getId());
//    }

}