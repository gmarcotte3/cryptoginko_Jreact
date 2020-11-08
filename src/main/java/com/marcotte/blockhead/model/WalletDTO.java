package com.marcotte.blockhead.model;

import java.util.List;

public class WalletDTO {
    private String walletName;
    private String description;
    List<CoinDTO> coinDTOs;

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
