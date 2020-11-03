package com.marcotte.blockhead.model;

import java.util.List;


public class FiatCurrency
{
    private String symbol;
    private String description;
    private double value;
    private FiatNames fiatType;

    @Override
    public String toString()
    {
        return  symbol + " " + value + " " + fiatType.code;
    }

    public String toStringDebug()
    {
        return "Currency{" +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                ", rate=" + value +
                ", code='" + fiatType.code + '\'' +
                '}';
    }

    public static FiatCurrency findCurrencyByName(List<FiatCurrency> currencies, String currencyName)
    {
        FiatCurrency foundCurrency = null;
        for (FiatCurrency cur : currencies)
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

    public FiatCurrency setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FiatCurrency setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getValue() {
        return value;
    }

    public FiatCurrency setValue(double value) {
        this.value = value;
        return this;
    }

    public String getCode() {

        return fiatType.code;
    }

    public FiatCurrency setCode(String code) {
        this.fiatType = FiatNames.valueOfCode(code);
        return this;
    }
}
