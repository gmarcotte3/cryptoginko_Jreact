package com.marcotte.blockhead.model;

import java.util.List;


public class Currency
{
    private String symbol;
    private String description;
    private double rate;
    private String code;

    @Override
    public String toString()
    {
        return "Currency{" +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                ", rate=" + rate +
                ", code='" + code + '\'' +
                '}';
    }


    public static Currency findCurrencyByName(List<Currency> currencies, String currencyName)
    {
        Currency foundCurrency = null;
        for (Currency cur : currencies)
        {
            if ( cur.getCode().compareToIgnoreCase(currencyName) == 0)
            {
                foundCurrency = cur;
                break;
            }
        }
        return foundCurrency;
    }

    public String getSymbol() {
        return symbol;
    }

    public Currency setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Currency setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getRate() {
        return rate;
    }

    public Currency setRate(double rate) {
        this.rate = rate;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Currency setCode(String code) {
        this.code = code;
        return this;
    }
}
