/*
 * Copyright (c) 2021. Lorem
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcotte.blockhead.datastore.portfolio;


import javax.persistence.*;
import java.time.LocalDate;

/**
 * coin price and value tracker.
 *
 * this class is a database entity for trackling the historical price and value data by coin ticker for a
 * period of one day for each record (datetime is truncated to just the date
 */
@Entity
public class CoinPriceValueTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;                                    // primary key

    @Column(name = "PriceDate", nullable = false)
    private LocalDate priceDate;                     // date of the transaction/price data yyyy-mm-dd (no time)

    @Column(name = "Ticker", nullable = false)
    private String ticker;                              // coin ticker

    @Column(name = "CoinBalance", nullable = false)
    private Double coinBalance;                         // coin balance in the coin specific units (ie. satoshis)

    @Column(name = "CoinPrice", nullable = false)
    private Double coinPrice;                           // The price in fiat

    @Column(name = "CoinPriceFiatTicker", nullable = false)
    private String coinPriceFiatTicker;                 // the fiat currency ticker that defines what fiat currency is used for the price.

    /*
     * getters and setters *****************************8
     */

    public long getId() {
        return id;
    }

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
        this.ticker = ticker.toUpperCase();
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

    public String getCoinPriceFiatTicker() {
        return coinPriceFiatTicker;
    }

    public void setCoinPriceFiatTicker(String coinPriceFiatTicker) {
        this.coinPriceFiatTicker = coinPriceFiatTicker.toUpperCase();
    }
}
