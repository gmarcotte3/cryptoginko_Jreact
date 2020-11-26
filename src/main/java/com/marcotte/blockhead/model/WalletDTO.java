package com.marcotte.blockhead.model;

import java.util.List;

public class WalletDTO {
    private String walletName;
    private String description;
    private List<CoinDTO> coinDTOs;
    private List<FiatCurrency> fiat_balances;  // list of all the supported fiat currencies values( total values = sum over coin totals).

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
