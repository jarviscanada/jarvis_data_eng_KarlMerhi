package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.view.PortfolioView;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DashboardService {

    private TraderDao traderDao;
    private PositionDao positionDao;
    private AccountDao accountDao;
    private QuoteDao quoteDao;

    @Autowired
    public DashboardService(TraderDao traderDao, PositionDao positionDao, AccountDao accountDao, QuoteDao quoteDao) {
        this.accountDao = accountDao;
        this.positionDao = positionDao;
        this.quoteDao = quoteDao;
        this.traderDao = traderDao;
    }

    public TraderAccountView getTraderAccount(Integer traderId){
        TraderAccountView traderAccountView = new TraderAccountView();
        return traderAccountView;
    }

    public PortfolioView getProfileViewByTraderId(Integer traderId){
        PortfolioView portfolioView = new PortfolioView();
        return portfolioView;
    }

    private Account findAccountByTraderId(Integer traderId){
        return accountDao.findByTraderId(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid traderId"));
    };
}