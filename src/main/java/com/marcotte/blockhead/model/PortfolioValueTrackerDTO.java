package com.marcotte.blockhead.model;

import java.util.ArrayList;

public class PortfolioValueTrackerDTO {


    private FiatCurrencyList fiat_balances;  // list of all the supported fiat currencies values( price * coinBalance).


    // these getters for json ouput
    public double getPriceUSD() {
        return fiat_balances.findFiat("USD").getValue();
    }
    public double getPriceNZD() {
        return fiat_balances.findFiat("NZD").getValue();
    }
    public double getPriceJPY() {
        return fiat_balances.findFiat("JPY").getValue();
    }
    public double getPriceJPM() {
        return fiat_balances.findFiat("JPM").getValue();
    }
    public double getPriceAUD() {
        return fiat_balances.findFiat("AUD").getValue();
    }
    public double getPriceGBP() {
        return fiat_balances.findFiat("GBP").getValue();
    }
    public double getPriceEUR() {
        return fiat_balances.findFiat("EUR").getValue();
    }
    public double getPriceINR() {
        return fiat_balances.findFiat("INR").getValue();
    }
    public double getPriceKRW() {
        return fiat_balances.findFiat("KRW").getValue();
    }


    public PortfolioValueTrackerDTO() {
        this.fiat_balances = new FiatCurrencyList();
    }

    public FiatCurrencyList getFiat_balances() {
        return fiat_balances;
    }

    public void setFiat_balances(FiatCurrencyList fiat_balances) {
        this.fiat_balances = fiat_balances;
    }
}
