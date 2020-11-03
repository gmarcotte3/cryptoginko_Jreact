package com.marcotte.blockhead.model;

/**
 * This represents a simple coin having name, balance, fiat prices, and fiat values
 */
public class Coin {
    private String coinName;    // name of the coin ie Bitcoin, Dash, BitcoinCash, Cardano etc.
    private String ticker;      // trading symbol ie BTH, DASH, BCH, ADA etc
    private Double coinBalance; // the coin balance in Satoshis for bitcoin DASH and BitcoinCash etc.
    private Fiat priceFiat;     // the current price in default fiat currency
    private Fiat valueFiat;     // The fiat value (coinBalance * priceFiat)
    private Fiat priceFiatAlt1; // same as priceFiat but using fiat alternate currency 1
    private Fiat valueFiatAlt1; // same as valueFiat but using fiat alternate currency 1
    private Fiat priceFiatAlt2; // same as priceFiat but using fiat alternate currency 2
    private Fiat valueFiatAlt2; // same as valueFiat but using fiat alternate currency 2

    public Coin() {

    }
    public Coin(String coinName, String ticker)
    {
        this.coinName = coinName;
        this.ticker = ticker;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getCoinBalance() {
        return coinBalance;
    }

    public void setCoinBalance(Double coinBalance) {
        this.coinBalance = coinBalance;
    }

    public Fiat getPriceFiat() {
        return priceFiat;
    }

    public void setPriceFiat(Fiat priceFiat) {
        this.priceFiat = priceFiat;
    }

    public Fiat getValueFiat() {
        return valueFiat;
    }

    public void setValueFiat(Fiat valueFiat) {
        this.valueFiat = valueFiat;
    }

    public Fiat getPriceFiatAlt1() {
        return priceFiatAlt1;
    }

    public void setPriceFiatAlt1(Fiat priceFiatAlt1) {
        this.priceFiatAlt1 = priceFiatAlt1;
    }

    public Fiat getValueFiatAlt1() {
        return valueFiatAlt1;
    }

    public void setValueFiatAlt1(Fiat valueFiatAlt1) {
        this.valueFiatAlt1 = valueFiatAlt1;
    }

    public Fiat getPriceFiatAlt2() {
        return priceFiatAlt2;
    }

    public void setPriceFiatAlt2(Fiat priceFiatAlt2) {
        this.priceFiatAlt2 = priceFiatAlt2;
    }

    public Fiat getValueFiatAlt2() {
        return valueFiatAlt2;
    }

    public void setValueFiatAlt2(Fiat valueFiatAlt2) {
        this.valueFiatAlt2 = valueFiatAlt2;
    }


}
