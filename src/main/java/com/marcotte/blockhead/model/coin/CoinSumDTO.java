package com.marcotte.blockhead.model.coin;

import java.math.BigDecimal;

public class CoinSumDTO {
    private String ticker;      // trading symbol ie BTH, DASH, BCH, ADA etc
    private Double coinBalance; // the coin balance in Satoshis for bitcoin DASH and BitcoinCash etc.
    private String walletName;  // wallet name

    public CoinSumDTO() {
        this.ticker = "";
        this.coinBalance = 0.0;
        this.walletName = "";
    }
    public CoinSumDTO(String ticker, Double coinBalance)
    {
        this.ticker = ticker;
        this.coinBalance = coinBalance;
        this.walletName = "";
    }
    public CoinSumDTO(Object[] columns) {
        this.ticker = (String) columns[0];
        this.coinBalance = (Double) columns[1];
        if ( columns.length > 2 ) {
            this.walletName = (String) columns[2];
        }
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

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
}
