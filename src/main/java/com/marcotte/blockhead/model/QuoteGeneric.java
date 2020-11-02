package com.marcotte.blockhead.model;

import java.util.List;


/**
 * generic coin price quote
 */
public class QuoteGeneric
{
    private String timeISO;
    private String coinName;
    private String symbol;
    private List<Currency> currency;

    public QuoteGeneric()
    {

    }

    public String getTimeISO() {
        return timeISO;
    }

    public QuoteGeneric setTimeISO(String timeISO) {
        this.timeISO = timeISO;
        return this;
    }

    public String getCoinName() {
        return coinName;
    }

    public QuoteGeneric setCoinName(String coinName) {
        this.coinName = coinName;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public QuoteGeneric setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public List<Currency> getCurrency() {
        return currency;
    }

    public QuoteGeneric setCurrency(List<Currency> currency) {
        this.currency = currency;
        return this;
    }

    @Override
    public String toString()
    {
        return "QuoteGeneric{" +
                "timeISO='" + timeISO + '\'' +
                ", coinName='" + coinName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", currency=" + currency +
                '}';
    }
}