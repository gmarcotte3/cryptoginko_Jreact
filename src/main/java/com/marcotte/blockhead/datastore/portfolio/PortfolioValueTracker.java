package com.marcotte.blockhead.datastore.portfolio;

import com.marcotte.blockhead.model.PortfolioValueTrackerDTO;

import javax.persistence.*;
import java.time.Instant;


@Entity
public class PortfolioValueTracker
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "DateTrackerID", nullable = false)
    private long dateTrackerID;

    @Column(name = "DateUpdated", nullable = false)
    private Instant dateUpdated;  //copy of the date in dateTracker

    @Column(name = "CoinValueUSD", nullable = false)
    private double coinValueUSD;

    @Column(name = "CoinValueNZD", nullable = false)
    private double coinValueNZD;

    @Column(name = "CoinValueJPY", nullable = false)
    private double coinValueJPY;

    @Column(name = "CoinValueJPM", nullable = false)
    private double coinValueJPM;

    @Column(name = "CoinValueAUD", nullable = false)
    private double coinValueAUD;

    @Column(name = "CoinValueEUR", nullable = false)
    private double coinValueEUR;

    @Column(name = "CoinValueGBP", nullable = false)
    private double coinValueGBP;

    @Column(name = "CoinValueKRW", nullable = false)
    private double coinValueKRW;

    @Column(name = "CoinValueINR", nullable = false)
    private double coinValueINR;

    @Column(name = "CoinValueBTC", nullable = false)
    private double coinValueBTC;

    @Column(name = "CoinValueETH", nullable = false)
    private double coinValueETH;



    public PortfolioValueTracker()
    {
        rightNow();
    }

    public PortfolioValueTracker(PortfolioValueTrackerDTO portfolioValueTrackerDTO) {
        this.coinValueUSD = portfolioValueTrackerDTO.getPriceUSD();
        this.coinValueNZD = portfolioValueTrackerDTO.getPriceNZD();
        this.coinValueJPY = portfolioValueTrackerDTO.getPriceJPY();
        this.coinValueJPM = portfolioValueTrackerDTO.getPriceJPM();
        this.coinValueAUD = portfolioValueTrackerDTO.getPriceAUD();
        this.coinValueEUR = portfolioValueTrackerDTO.getPriceEUR();
        this.coinValueGBP = portfolioValueTrackerDTO.getPriceGBP();
        this.coinValueKRW = portfolioValueTrackerDTO.getPriceKRW();
        this.coinValueINR = portfolioValueTrackerDTO.getPriceINR();
        this.coinValueBTC = portfolioValueTrackerDTO.getPriceBTC();
        this.coinValueETH = portfolioValueTrackerDTO.getPriceETH();
    }


    public void rightNow()
    {
        this.dateUpdated = Instant.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDateTrackerID() {
        return dateTrackerID;
    }

    public void setDateTrackerID(long dateTrackerID) {
        this.dateTrackerID = dateTrackerID;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public double getCoinValueUSD() {
        return coinValueUSD;
    }

    public void setCoinValueUSD(double coinValueUSD) {
        this.coinValueUSD = coinValueUSD;
    }

    public double getCoinValueNZD() {
        return coinValueNZD;
    }

    public void setCoinValueNZD(double coinValueNZD) {
        this.coinValueNZD = coinValueNZD;
    }

    public double getCoinValueJPY() {
        return coinValueJPY;
    }

    public void setCoinValueJPY(double coinValueJPY) {
        this.coinValueJPY = coinValueJPY;
    }

    public double getCoinValueJPM() {
        return coinValueJPM;
    }

    public void setCoinValueJPM(double coinValueJPM) {
        this.coinValueJPM = coinValueJPM;
    }

    public double getCoinValueAUD() {
        return coinValueAUD;
    }

    public void setCoinValueAUD(double coinValueAUD) {
        this.coinValueAUD = coinValueAUD;
    }

    public double getCoinValueEUR() {
        return coinValueEUR;
    }

    public void setCoinValueEUR(double coinValueEUR) {
        this.coinValueEUR = coinValueEUR;
    }

    public double getCoinValueGBP() {
        return coinValueGBP;
    }

    public void setCoinValueGBP(double coinValueGBP) {
        this.coinValueGBP = coinValueGBP;
    }

    public double getCoinValueKRW() {
        return coinValueKRW;
    }

    public void setCoinValueKRW(double coinValueKRW) {
        this.coinValueKRW = coinValueKRW;
    }

    public double getCoinValueINR() {
        return coinValueINR;
    }

    public void setCoinValueINR(double coinValueINR) {
        this.coinValueINR = coinValueINR;
    }

    public double getCoinValueBTC() {
        return coinValueBTC;
    }

    public void setCoinValueBTC(double coinValueBTC) {
        this.coinValueBTC = coinValueBTC;
    }

    public double getCoinValueETH() {
        return coinValueETH;
    }

    public void setCoinValueETH(double coinValueETH) {
        this.coinValueETH = coinValueETH;
    }
}

