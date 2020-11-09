package com.marcotte.blockhead.model;

import java.util.List;


public class FiatCurrency
{
    private String symbol;
    private String description;
    private double value;
    private FiatNames fiatType;

    public FiatCurrency() {
        this.value = 0.0;
        this.fiatType = FiatNames.USD;
    }
    public FiatCurrency(double value, FiatNames fiatType) {
        this.value = value;
        this.fiatType = fiatType;
    }

    public FiatCurrency(double value, String fiatCode) {
        this.value = value;
        this.fiatType = FiatNames.valueOfCode(fiatCode);
    }

    @Override
    public String toString()
    {
        return "Currency{" +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", code='" + fiatType.code + '\'' +
                '}';
    }

    public String toStringDisplay()
    {
        return  symbol + " " + value + " " + fiatType.code;
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

    public FiatNames getFiatType() {
        return fiatType;
    }

    public void setFiatType(FiatNames fiatType) {
        this.fiatType = fiatType;
    }
}
