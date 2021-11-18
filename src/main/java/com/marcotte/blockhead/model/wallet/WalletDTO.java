package com.marcotte.blockhead.model.wallet;

import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.coin.CoinSumDTO;
import com.marcotte.blockhead.model.fiat.FiatCurrency;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;
import com.marcotte.blockhead.services.portfolio.PortfolioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WalletDTO {
    private static final Logger log = LoggerFactory.getLogger(WalletDTO.class);

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

    public void addValue(String fiatCode, Double moMoney) {
        try {
            FiatCurrency fiat = fiat_balances.findFiat(fiatCode);
            fiat.setValue(fiat.getValue() + moMoney);
        } catch ( Exception e) {
            // if we dont support or the fiat code is invalid then we should get a null_pointer  exception
            log.error("Invalid fiat code  exception=" + e.getMessage());
        }
    }
    public void addValues(FiatCurrencyList fiat_values) {
        fiat_balances.addToFiatList(fiat_values);
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

    public String getValueUSDstring() {
        return findFiatValue("USD").getValueMoneyFormat();
    }
    public String getValueNZDstring() {
        return findFiatValue("NZD").getValueMoneyFormat();
    }
    public String getValueJPYstring() {
        return findFiatValue("JPY").getValueMoneyFormat();
    }
    public String getValueJPMstring() {
        return findFiatValue("JPM").getValueMoneyFormat();
    }
    public String getValueAUDstring() {
        return findFiatValue("AUD").getValueMoneyFormat();
    }
    public String getValueGBPstring() {
        return findFiatValue("GBP").getValueMoneyFormat();
    }
    public String getValueEURstring() {
        return findFiatValue("EUR").getValueMoneyFormat();
    }
    public String getValueINRstring() {
        return findFiatValue("INR").getValueMoneyFormat();
    }
    public String getValueKRWstring() {
        return findFiatValue("KRW").getValueMoneyFormat();
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

    public void addCoinDTO( CoinDTO coinDTO ) {
        if ( this.coinDTOs == null ) {
            this.coinDTOs = new ArrayList<CoinDTO>();
        }
        this.coinDTOs.add(coinDTO);
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
