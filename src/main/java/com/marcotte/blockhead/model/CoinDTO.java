package com.marcotte.blockhead.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents a simple coin having name, balance, fiat prices, and fiat values.
 * The coin can represents multiple UTXOs of a coin, it can repressent coins that belong to
 * a wallet.
 */
public class CoinDTO {
    private String coinName;    // name of the coin ie Bitcoin, Dash, BitcoinCash, Cardano etc.
    private String ticker;      // trading symbol ie BTH, DASH, BCH, ADA etc
    private Double coinBalance; // the coin balance in Satoshis for bitcoin DASH and BitcoinCash etc.

    private List<FiatCurrency> fiat_prices;  // list of all the supported fiat currencies prices.
    private List<FiatCurrency> fiat_balances;  // list of all the supported fiat currencies values( price * coinBalance).


    public CoinDTO() {
        this.coinName = "";
        this.ticker = "";
        this.coinBalance = 0.0;
        this.fiat_prices = new ArrayList<>();
        this.fiat_balances = new ArrayList<>();
    }
    public CoinDTO(String coinName, String ticker)
    {
        this.coinName = coinName;
        this.ticker = ticker;
        this.coinBalance = 0.0;
        this.fiat_prices = new ArrayList<>();
        this.fiat_balances = new ArrayList<>();
    }

    public FiatCurrency findFiatPrice(String fiatCode ) {
        for( int j = 0; j < fiat_prices.size(); j++) {
            if ( fiat_prices.get(j).getCode().equals(fiatCode)) {
                return fiat_prices.get(j);
            }
        }
        return new FiatCurrency(); // return a dummy
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

    public List<FiatCurrency> getFiat_prices() {
        return fiat_prices;
    }

    public void setFiat_prices(List<FiatCurrency> fiat_prices) {
        this.fiat_prices = fiat_prices;
    }

    public List<FiatCurrency> getFiat_balances() {
        return fiat_balances;
    }

    public void setFiat_balances(List<FiatCurrency> fiat_balances) {
        this.fiat_balances = fiat_balances;
    }

}
