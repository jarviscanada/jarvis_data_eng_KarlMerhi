package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private AccountDao accountDao;
    private PositionDao positionDao;
    private SecurityOrderDao securityOrderDao;
    private QuoteDao quoteDao;

    @Autowired
    public OrderService(QuoteDao quoteDao, AccountDao accountDao, PositionDao positionDao,
                        SecurityOrderDao securityOrderDao) {
        this.quoteDao = quoteDao;
        this.accountDao = accountDao;
        this.positionDao = positionDao;
        this.securityOrderDao = securityOrderDao;
    }

    /**
     * Execute a market order
     * <p>
     * - validate the order(e.g.size and ticker)
     * - create a securityOrder(for security_order table)
     * - Handle buy or sell order
     * - buy order: check account balance (calls helper method)
     * - sell order: check position for the ticker/symbol (calls helper method)
     * - (please don't forget to update securityOrder.status)
     * - Save and return securityOrder
     * <p>
     * NOTE: you will need to some helper methods (protected or private)
     *
     * @param orderDto market order
     * @return SecurityOrder from security_order table
     * @throws org.springframework.dao.DataAccessException if unable to get data from DAO
     * @throws IllegalArgumentException                    for invalid input
     */
    public SecurityOrder executeMarketOrder(MarketOrderDto orderDto) {
        if(orderDto.getSize() == 0 || orderDto.getTicker() == null ){
            throw new IllegalArgumentException("Invalid input");
        }

        Quote quote = quoteDao.findById(orderDto.getTicker()).orElseThrow(() ->
                new IllegalArgumentException("The trader account specified could not be found"));
        Account account = accountDao.findById(orderDto.getAccountId()).orElseThrow(() ->
                new IllegalArgumentException("No account found with specified accountId"));

        SecurityOrder securityOrder = new SecurityOrder();
        securityOrder.setStatus("CREATED");
        securityOrder.setAccountId(orderDto.getAccountId());
        securityOrder.setSize(orderDto.getSize());
        securityOrder.setTicker(orderDto.getTicker());

        if(orderDto.getType() == "buy") {
            securityOrder.setPrice(quote.getAskPrice());
            handleBuyMarketOrder(orderDto, securityOrder, account);
        } else if (orderDto.getType() == "sell") {
            securityOrder.setPrice(quote.getBidPrice());
            handleSellMarketOrder(orderDto, securityOrder, account);
        }

        securityOrderDao.save(securityOrder);
        return securityOrder;

    }

    /**
     * Helper method that execute a buy order
     * @param orderDto user order
     * @param securityOrder to be saved in database
     * @param account account
     */
    protected void handleBuyMarketOrder(MarketOrderDto orderDto, SecurityOrder securityOrder,
                                        Account account) {
        Double price = securityOrder.getPrice() * orderDto.getSize();
        if (account.getAmount() >= price) {
            account.setAmount(account.getAmount() - price);
            accountDao.save(account);
            securityOrder.setStatus("FILLED");
            securityOrder.setNotes("Order has been filled");
        } else {
            securityOrder.setStatus("CANCELLED");
            securityOrder.setNotes("Not enough funds in your account");
        }
    }

    /**
     * Helper method that execute a sell order
     *
     * @param marketOrderDto user order
     * @param securityOrder to be saved in database
     * @param account account
     */
    protected void handleSellMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder,
                                         Account account){

        Position position = positionDao.findById(securityOrder.getAccountId())
                .orElseThrow(() ->
                        new IllegalArgumentException("No position size available for selling"));

        if(position.getPosition() >= marketOrderDto.getSize()) {
            account.setAmount(account.getAmount() - securityOrder.getPrice() * securityOrder.getSize());
            accountDao.save(account);
            securityOrder.setStatus("FILLED");
            securityOrder.setNotes("Order has been filled");
        } else {
            securityOrder.setStatus("CANCELLED");
            securityOrder.setNotes("size is not enough");
        }
    }


}
