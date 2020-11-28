package com.marcotte.blockhead.model;

import java.util.ArrayList;
import java.util.List;

public class WalletDTO {
    private String walletName;
    private String description;
    private List<CoinDTO> coinDTOs;

    private FiatCurrencyList fiat_balances;  // list of all the supported fiat currencies values( total values = sum over coin totals).

    public WalletDTO() {
        this.coinDTOs = new ArrayList<>();
        this.fiat_balances = new FiatCurrencyList();
    }

    public FiatCurrency findFiatValue(String fiatCode ) {
        return fiat_balances.findFiat(fiatCode );
    }

    // these getters for json ouput
    public double getPriceUSD() {
        return findFiatValue("USD").getValue();
    }
    public double getPriceNZD() {
        return findFiatValue("NZD").getValue();
    }
    public double getPriceJPY() {
        return findFiatValue("JPY").getValue();
    }
    public double getPriceJPM() {
        return findFiatValue("JPM").getValue();
    }
    public double getPriceAUD() {
        return findFiatValue("AUD").getValue();
    }
    public double getPriceGBP() {
        return findFiatValue("GBP").getValue();
    }
    public double getPriceEUR() {
        return findFiatValue("EUR").getValue();
    }
    public double getPriceINR() {
        return findFiatValue("INR").getValue();
    }
    public double getPriceKRW() {
        return findFiatValue("KRW").getValue();
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CoinDTO> getCoinDTOs() {
        return coinDTOs;
    }

    public void setCoinDTOs(List<CoinDTO> coinDTOs) {
        this.coinDTOs = coinDTOs;
    }
}
