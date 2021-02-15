package com.marcotte.blockhead.model.wallet;

import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.fiat.FiatCurrency;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;

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
    public double getValueUSD() {
        return findFiatValue("USD").getValue();
    }
    public double getValueNZD() {
        return findFiatValue("NZD").getValue();
    }
    public double getValueJPY() {
        return findFiatValue("JPY").getValue();
    }
    public double getValueJPM() {
        return findFiatValue("JPM").getValue();
    }
    public double getValueAUD() {
        return findFiatValue("AUD").getValue();
    }
    public double getValueGBP() {
        return findFiatValue("GBP").getValue();
    }
    public double getValueEUR() {
        return findFiatValue("EUR").getValue();
    }
    public double getValueINR() {
        return findFiatValue("INR").getValue();
    }
    public double getValueKRW() {
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

    public FiatCurrencyList getFiat_balances() {
        return fiat_balances;
    }

    public void setFiat_balances(FiatCurrencyList fiat_balances) {
        this.fiat_balances = fiat_balances;
    }
}
