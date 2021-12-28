/*
 * Copyright (c) 2021. tranquilitySoftware
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcotte.blockhead.model.portfolio;

import com.marcotte.blockhead.datastore.portfolio.CoinPriceValueTracker;

import java.text.NumberFormat;
import java.time.LocalDate;

public class CoinPriceValueTrackerDTO {
    private LocalDate priceDate;                        // date of the transaction/price data yyyy-mm-dd (no time)
    private String ticker;                              // coin ticker
    private Double coinBalance;                         // coin balance in the coin specific units (ie. satoshis)
    private Double coinPrice;                           // The price in fiat default
    private Double coinPrice2;                          // The price in fiat second
    private Double coinPrice3;                          // The price in fiat third
    private String coinPriceFiatTicker;                 // the fiat currency ticker that defines what fiat currency is used for the price.
    private String coinPriceFiatTicker2;                // the fiat currency ticker that defines what fiat currency is used for the price
    private String coinPriceFiatTicker3;                // the fiat currency ticker that defines what fiat currency is used for the price.

    public CoinPriceValueTrackerDTO() {
    }


    public CoinPriceValueTrackerDTO(LocalDate priceDate, String ticker, Double coinBalance, Double coinPrice, Double coinPrice2, Double coinPrice3, String coinPriceFiatTicker, String coinPriceFiatTicker2, String coinPriceFiatTicker3) {
        this.priceDate = priceDate;
        this.ticker = ticker;
        this.coinBalance = coinBalance;
        this.coinPrice = coinPrice;
        this.coinPrice2 = coinPrice2;
        this.coinPrice3 = coinPrice3;
        this.coinPriceFiatTicker = coinPriceFiatTicker;
        this.coinPriceFiatTicker2 = coinPriceFiatTicker2;
        this.coinPriceFiatTicker3 = coinPriceFiatTicker3;
    }

    public CoinPriceValueTrackerDTO(CoinPriceValueTracker coinPriceValueTracker) {
        this.priceDate = coinPriceValueTracker.getPriceDate();
        this.ticker = coinPriceValueTracker.getTicker();
        this.coinBalance = coinPriceValueTracker.getCoinBalance();
        this.coinPrice = coinPriceValueTracker.getCoinPrice();
        this.coinPrice2 = coinPriceValueTracker.getCoinPrice2();
        this.coinPrice3 = coinPriceValueTracker.getCoinPrice3();
        this.coinPriceFiatTicker = coinPriceValueTracker.getCoinPriceFiatTicker();
        this.coinPriceFiatTicker2 = coinPriceValueTracker.getCoinPriceFiatTicker2();
        this.coinPriceFiatTicker3 = coinPriceValueTracker.getCoinPriceFiatTicker3();
    }

    public CoinPriceValueTrackerDTO(CoinPriceValueTrackerDTO coinPriceValueTrackerDTO) {
        this.priceDate = coinPriceValueTrackerDTO.getPriceDate();
        this.ticker = coinPriceValueTrackerDTO.getTicker();
        this.coinBalance = coinPriceValueTrackerDTO.getCoinBalance();
        this.coinPrice = coinPriceValueTrackerDTO.getCoinPrice();
        this.coinPrice2 = coinPriceValueTrackerDTO.getCoinPrice2();
        this.coinPrice3 = coinPriceValueTrackerDTO.getCoinPrice3();
        this.coinPriceFiatTicker = coinPriceValueTrackerDTO.getCoinPriceFiatTicker();
        this.coinPriceFiatTicker2 = coinPriceValueTrackerDTO.getCoinPriceFiatTicker2();
        this.coinPriceFiatTicker3 = coinPriceValueTrackerDTO.getCoinPriceFiatTicker3();
    }

    // Calculated fields
    public double getCoinValue() {
        return this.getCoinBalance() * this.getCoinPrice();
    }

    public Double getCoinValue2() {
        if ( this.getCoinPrice2() != null ) {
            return this.getCoinBalance() * this.getCoinPrice2();
        }
        return 0.0;
    }

    public Double getCoinValue3() {
        if ( this.getCoinPrice3() != null ) {
            return this.getCoinBalance() * this.getCoinPrice3();
        }
        return 0.0;
    }

    public String getCoinBalanceAsFormattedString(int fracdigit) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumFractionDigits(fracdigit);
        numberFormat.setMaximumFractionDigits(fracdigit);

        return numberFormat.format(getCoinBalance());
    }


    // ***********************************************
    // *** Getters and Setters                     ***
    // ***********************************************

    public LocalDate getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(LocalDate priceDate) {
        this.priceDate = priceDate;
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

    public Double getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(Double coinPrice) {
        this.coinPrice = coinPrice;
    }

    public Double getCoinPrice2() {
        return coinPrice2;
    }

    public void setCoinPrice2(Double coinPrice2) {
        this.coinPrice2 = coinPrice2;
    }

    public Double getCoinPrice3() {
        return coinPrice3;
    }

    public void setCoinPrice3(Double coinPrice3) {
        this.coinPrice3 = coinPrice3;
    }

    public String getCoinPriceFiatTicker() {
        return coinPriceFiatTicker;
    }

    public void setCoinPriceFiatTicker(String coinPriceFiatTicker) {
        this.coinPriceFiatTicker = coinPriceFiatTicker;
    }

    public String getCoinPriceFiatTicker2() {
        return coinPriceFiatTicker2;
    }

    public void setCoinPriceFiatTicker2(String coinPriceFiatTicker2) {
        this.coinPriceFiatTicker2 = coinPriceFiatTicker2;
    }

    public String getCoinPriceFiatTicker3() {
        return coinPriceFiatTicker3;
    }

    public void setCoinPriceFiatTicker3(String coinPriceFiatTicker3) {
        this.coinPriceFiatTicker3 = coinPriceFiatTicker3;
    }
}
