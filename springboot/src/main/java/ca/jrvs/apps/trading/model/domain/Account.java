package ca.jrvs.apps.trading.model.domain;

public class Account implements Entity<Integer>{

    private Integer id;
    private Integer trader_id;
    private Double amount;
    @Override
    public Integer getId() {
        return null;
    } d hj

    @Override
    public void setId(Integer integer) {


    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }

    public Integer getTrader_id() {
        return trader_id;
    }

    public void setTrader_id(Integer trader_id) {
        this.trader_id = trader_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
