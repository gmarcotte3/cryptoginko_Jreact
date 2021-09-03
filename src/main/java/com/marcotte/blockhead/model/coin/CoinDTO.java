package com.marcotte.blockhead.model.coin;

import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.coin.Coin;
import com.marcotte.blockhead.model.fiat.FiatCurrency;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;

/**
 * This represents a simple coin having name, balance, fiat prices, and fiat values.
 * The coin can represents multiple UTXOs of a coin, it can repressent coins that belong to
 * a wallet.
 */
public class CoinDTO {
    private String coinName;    // name of the coin ie Bitcoin, Dash, BitcoinCash, Cardano etc.
    private String ticker;      // trading symbol ie BTH, DASH, BCH, ADA etc
    private Double coinBalance; // the coin balance in Satoshis for bitcoin DASH and BitcoinCash etc.

    private FiatCurrencyList fiat_prices;  // list of all the supported fiat currencies prices.
    private FiatCurrencyList fiat_balances;  // list of all the supported fiat currencies values( price * coinBalance).


    public CoinDTO() {
        init();
    }
    public CoinDTO(String coinName, String ticker)
    {
        init();
        this.coinName = coinName;
        this.ticker = ticker;
    }
    public CoinDTO(BlockchainAddressStore blockchainAddressStore)
    {
        init();
        this.ticker = blockchainAddressStore.getTicker();
        this.coinBalance = blockchainAddressStore.getLastBalance();
    }

    public CoinDTO(CoinSumDTO coinSummyDTO)
    {
        init();
        this.ticker = coinSummyDTO.getTicker();
        this.coinBalance = coinSummyDTO.getCoinBalance();
    }

    private void init() {
        this.coinName = "";
        this.ticker = "";
        this.coinBalance = 0.0;
        this.fiat_prices = new FiatCurrencyList();
        this.fiat_balances = new FiatCurrencyList();
    }

    public void setCoinDTO(Coin coin) {
        this.ticker = coin.getTicker();
        this.coinName = coin.getCoinName();

        this.fiat_prices = new FiatCurrencyList();
        this.fiat_balances = new FiatCurrencyList();

        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceUSD(), "USD" ));
        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceNZD(), "NZD" ));
        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceJPY(), "JPY" ));
        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceJPM(), "JPM" ));
        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceAUD(), "AUD" ));
        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceEUR(), "EUR" ));
        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceGBP(), "GBP" ));
        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceKRW(), "KRW" ));
        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceINR(), "INR" ));
        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceBTC(), "BTC" ));
        this.fiat_prices.setFiat( new FiatCurrency(coin.getPriceETH(), "ETH" ));

    }

    /**
     * calculates the coin's value (coin balance * price) in all the supported currencies
     */
    public void calculateCoinValue() {
        this.fiat_balances = new FiatCurrencyList();
        for ( FiatCurrency fiat : this.fiat_prices.getFiat_values() ) {
            this.fiat_balances.setFiat(new FiatCurrency((fiat.getValue() * this.coinBalance), fiat.getFiatType()));
        }
    }

    /**
     * a look up for the coins price by specifying the fiat type (USD, NZD, EUR etc..)
     * @param fiatCode
     * @return
     */
    public FiatCurrency findFiatPrice(String fiatCode ) {
        return this.fiat_prices.findFiat( fiatCode );
    }

    public void setFiatPrice( FiatCurrency fiatPrice) {
        this.fiat_prices.setFiat( fiatPrice);
    }

    // these getters for json ouput
    public double getPriceUSD() {
       return findFiatPrice("USD").getValue();
    }
    public double getPriceNZD() {
        return findFiatPrice("NZD").getValue();
    }
    public double getPriceJPY() {
        return findFiatPrice("JPY").getValue();
    }
    public double getPriceJPM() {
        return findFiatPrice("JPM").getValue();
    }
    public double getPriceAUD() {
        return findFiatPrice("AUD").getValue();
    }
    public double getPriceGBP() {
        return findFiatPrice("GBP").getValue();
    }
    public double getPriceEUR() {
        return findFiatPrice("EUR").getValue();
    }
    public double getPriceINR() {
        return findFiatPrice("INR").getValue();
    }
    public double getPriceKRW() {
        return findFiatPrice("KRW").getValue();
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


    /**
     * retrieve the fiat value of the balance with the specified fiat.
     * @param fiatCode  any of the fiat codes i.e. "USD", "NZD", ...
     * @return the value in fiat of the coin (coinprice*coinblance)
     */
    public FiatCurrency findFiatValue(String fiatCode ) {
        return this.fiat_balances.findFiat(fiatCode);
    }

    public FiatCurrencyList getFiat_prices() {
        return fiat_prices;
    }

    public void setFiat_prices(FiatCurrencyList fiat_prices) {
        this.fiat_prices = fiat_prices;
    }

    public FiatCurrencyList getFiat_balances() {
        return fiat_balances;
    }

    public void setFiat_balances(FiatCurrencyList fiat_balances) {
        this.fiat_balances = fiat_balances;
    }

}
