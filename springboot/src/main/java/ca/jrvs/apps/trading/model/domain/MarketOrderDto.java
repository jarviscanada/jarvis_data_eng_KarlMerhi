package ca.jrvs.apps.trading.model.domain;

public class MarketOrderDto{

    private String ticker;
    private Integer accountId;
    private Integer size;
    private String type;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String orderType) {
        this.type = orderType;
    }

}
