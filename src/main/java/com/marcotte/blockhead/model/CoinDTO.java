package com.marcotte.blockhead.model;

import com.marcotte.blockhead.datastore.Coin;

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

    public void setCoinDTO(Coin coin) {
        this.ticker = coin.getTicker();
        this.coinName = coin.getCoinName();

        this.fiat_prices = new ArrayList<>();
        this.fiat_balances = new ArrayList<>();

        this.fiat_prices.add ( new FiatCurrency(coin.getPriceUSD(), FiatNames.USD));
        this.fiat_prices.add ( new FiatCurrency(coin.getPriceNZD(), FiatNames.NZD));
        this.fiat_prices.add ( new FiatCurrency(coin.getPriceJPY(), FiatNames.JPY));
        this.fiat_prices.add ( new FiatCurrency(coin.getPriceJPM(), FiatNames.JPM));
        this.fiat_prices.add ( new FiatCurrency(coin.getPriceAUD(), FiatNames.AUD));
        this.fiat_prices.add ( new FiatCurrency(coin.getPriceGBP(), FiatNames.GBP));
        this.fiat_prices.add ( new FiatCurrency(coin.getPriceEUR(), FiatNames.EUR));
        this.fiat_prices.add ( new FiatCurrency(coin.getPriceINR(), FiatNames.INR));
        this.fiat_prices.add ( new FiatCurrency(coin.getPriceKRW(), FiatNames.KRW));
        this.fiat_prices.add ( new FiatCurrency(coin.getPriceBTC(), FiatNames.BTC));
        this.fiat_prices.add ( new FiatCurrency(coin.getPriceETH(), FiatNames.ETH));
    }

    /**
     * calculates the coin's value (coin balance * price) in all the supported currencies
     */
    public void calculateCoinValue() {
        this.fiat_balances = new ArrayList<>();
        for ( FiatCurrency fiat : this.fiat_prices ) {
            this.fiat_balances.add(new FiatCurrency((fiat.getValue() * this.coinBalance), fiat.getFiatType()));
        }
    }

    /**
     * a look up for the coins price by specifying the fiat type (USD, NZD, EUR etc..)
     * @param fiatCode
     * @return
     */
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
