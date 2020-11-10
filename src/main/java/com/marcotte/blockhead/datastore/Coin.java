package com.marcotte.blockhead.datastore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Coin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String coinName;    // name of the coin ie Bitcoin, Dash, BitcoinCash, Cardano etc.
    private String ticker;      // trading symbol ie BTH, DASH, BCH, ADA etc
    private String description;

    private Double priceUSD;
    private Double priceNZD;
    private Double priceJPY;
    private Double priceJPM;
    private Double priceAUD;
    private Double priceEUR;
    private Double priceGBP;
    private Double priceKRW;
    private Double priceINR;
    private Double priceBTC;
    private Double priceETH;

    // TODO: add things like current market cap, link to current news, links to coin websites up/down in last 24h etc
    // some of these could be part of the CoinDTO, have to think about what we want to track on the disc.

    public Coin() {
        this.coinName = "";
        this.ticker = "";
        this.description = "";
        this.priceUSD = 0.0;
        this.priceNZD = 0.0;
        this.priceJPY = 0.0;
        this.priceJPM = 0.0;
        this.priceAUD = 0.0;
        this.priceEUR = 0.0;
        this.priceGBP = 0.0;
        this.priceKRW = 0.0;
        this.priceINR = 0.0;
        this.priceBTC = 0.0;
        this.priceETH = 0.0;
    }
    public Coin(String coinName, String ticker, String description)
    {
        this.coinName = coinName;
        this.ticker = ticker.toUpperCase();
        this.description = description;
        this.priceUSD = 0.0;
        this.priceNZD = 0.0;
        this.priceJPY = 0.0;
        this.priceJPM = 0.0;
        this.priceAUD = 0.0;
        this.priceEUR = 0.0;
        this.priceGBP = 0.0;
        this.priceKRW = 0.0;
        this.priceINR = 0.0;
        this.priceBTC = 0.0;
        this.priceETH = 0.0;
    }

    public void setCoin( Coin newCoin) {
        this.coinName = newCoin.getCoinName();
        this.ticker = newCoin.getTicker();
        this.description = newCoin.getDescription();
        this.priceUSD = newCoin.getPriceUSD();
        this.priceNZD = newCoin.getPriceNZD();
        this.priceJPY = newCoin.getPriceJPY();
        this.priceJPM = newCoin.getPriceJPM();
        this.priceAUD = newCoin.getPriceAUD();
        this.priceEUR = newCoin.getPriceEUR();
        this.priceGBP = newCoin.getPriceGBP();
        this.priceKRW = newCoin.getPriceKRW();
        this.priceINR = newCoin.getPriceINR();
        this.priceBTC = newCoin.getPriceBTC();
        this.priceETH = newCoin.getPriceETH();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.ticker = ticker.toUpperCase();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPriceUSD() {
        return priceUSD;
    }

    public void setPriceUSD(Double priceUSD) {
        this.priceUSD = priceUSD;
    }

    public Double getPriceNZD() {
        return priceNZD;
    }

    public void setPriceNZD(Double priceNZD) {
        this.priceNZD = priceNZD;
    }

    public Double getPriceJPY() {
        return priceJPY;
    }

    public void setPriceJPY(Double priceJPY) {
        this.priceJPY = priceJPY;
    }

    public Double getPriceJPM() {
        return priceJPM;
    }

    public void setPriceJPM(Double priceJPM) {
        this.priceJPM = priceJPM;
    }

    public Double getPriceAUD() {
        return priceAUD;
    }

    public void setPriceAUD(Double priceAUD) {
        this.priceAUD = priceAUD;
    }

    public Double getPriceEUR() {
        return priceEUR;
    }

    public void setPriceEUR(Double priceEUR) {
        this.priceEUR = priceEUR;
    }

    public Double getPriceGBP() {
        return priceGBP;
    }

    public void setPriceGBP(Double priceGBP) {
        this.priceGBP = priceGBP;
    }

    public Double getPriceKRW() {
        return priceKRW;
    }

    public void setPriceKRW(Double priceKRW) {
        this.priceKRW = priceKRW;
    }

    public Double getPriceINR() {
        return priceINR;
    }

    public void setPriceINR(Double priceINR) {
        this.priceINR = priceINR;
    }

    public Double getPriceBTC() {
        return priceBTC;
    }

    public void setPriceBTC(Double priceBTC) {
        this.priceBTC = priceBTC;
    }

    public Double getPriceETH() {
        return priceETH;
    }

    public void setPriceETH(Double priceETH) {
        this.priceETH = priceETH;
    }
}
