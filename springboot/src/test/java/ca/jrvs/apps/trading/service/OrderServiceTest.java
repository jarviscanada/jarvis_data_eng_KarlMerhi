package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Captor
    ArgumentCaptor<SecurityOrder> captorSecurityOrder;

    @Mock
    AccountDao accountDao;
    @Mock
    SecurityOrderDao securityOrderDao;
    @Mock
    QuoteDao quoteDao;
    @Mock
    PositionDao positionDao;

    @InjectMocks
    public OrderService orderService;

    private MarketOrderDto marketOrderDto;
    private Account account;
    private Quote quote;
    private Position position;


    @Before
    public void setUp() {
        quoteDao.deleteAll();
        marketOrderDto = new MarketOrderDto();
        marketOrderDto.setTicker("AAPL");
        marketOrderDto.setAccountId(2);

        account = new Account();
        account.setId(2);
        account.setAmount(0d);

        quote = new Quote();
        quote.setTicker("AAPL");
        quote.setBidPrice(100d);
        quote.setBidSize(50);
        quote.setAskPrice(120d);
        quote.setAskSize(70);

        position = new Position();
        position.setPosition(0);
        //position.setAccountId(2);
        position.setTicker("AAPL");
        position.setId(2);

        when(quoteDao.findById(any())).thenReturn(java.util.Optional.of(quote));
        when(accountDao.findById(any())).thenReturn(java.util.Optional.of(account));
        //when(quoteDao.existsById(any())).thenReturn(true);
        when(positionDao.findById(anyInt())).thenReturn(Optional.of(position));
    }

    @Test
    public void executeMarketOrder() {

        marketOrderDto.setSize(0);
        try {
            orderService.executeMarketOrder(marketOrderDto);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }

        // Check for not enough Ammout
        marketOrderDto.setType("buy");
        marketOrderDto.setSize(10);
        SecurityOrder order = orderService.executeMarketOrder(marketOrderDto);
        assertEquals("CANCELLED", order.getStatus());

        // Check buy order
        marketOrderDto.setSize(20);
        account.setAmount(2500d);
        order = orderService.executeMarketOrder(marketOrderDto);
        assertEquals("FILLED", order.getStatus());
        assertEquals(new Double(100), account.getAmount());


        // Check for position to sell
        marketOrderDto.setType("sell");
        marketOrderDto.setSize(50);
        position.setPosition(10);
        order = orderService.executeMarketOrder(marketOrderDto);
        assertEquals("CANCELLED", order.getStatus());

        // Check for sell order
        marketOrderDto.setSize(100);
        position.setPosition(210);
        order = orderService.executeMarketOrder(marketOrderDto);
        assertEquals("FILLED", order.getStatus());
    }
}