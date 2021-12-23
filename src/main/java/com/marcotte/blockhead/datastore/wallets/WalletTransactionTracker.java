/*
 * Copyright (c) 2021. tranquilitySoftware
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcotte.blockhead.datastore.wallets;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * This table contains the processing of a wallet transaction dumps. The csv transaction dumps from wallet software
 * (such as exodus and cardano Daedalus wallet) are processed (calculate a running cost base, gain/loss for each transaction
 * and adding addtiional fields that can be used to create an investment/tax report later.
 */
@Entity
public class WalletTransactionTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;                                        // generated primary key

    @Column(name = "WalletName", nullable = false)
    private String walletName;                              // key field, all transaction belong to a wallet

    @Column(name = "Ticker", nullable = false)
    private String ticker;                                  // key field, all transaction involve a coin (coin ticker)

    @Column(name = "TransactionTimestamp", nullable = false)
    private Timestamp transactionTimestamp;                 // the time of the transaction, important field for order/exact time of the transaction.

    @Column(name = "coinAmount", nullable = false)
    private Double coinAmount = 0.0;                        // the coin transaction amount in the coin units

    @Column(name = "fee")
    private Double fee = 0.0;                               // the fee in coin units

    @Column(name = "RunningBalance", nullable = false)
    private Double runningBalance;                          // running balance

    @Column(name = "TransactionType", nullable = false)
    private String transactionType;                         // transaction type: (PURCHASE, SALE, REMIT, GIVEAWAY, MOVEOUT, MOVEIN)

    @Column(name = "FiatCurrencyCode", nullable = false)
    private String fiatCurrencyCode;                        // fiat currency that is being used

    @Column(name = "FiatPriceAtTransactionDate")
    private Double fiatPriceAtTransactionDate;              // fiat price of the coin at transaction time

    @Column(name = "FiatValueAtTransactionDate")
    private Double fiatValueAtTransactionDate;              // fiat value of coin price * coinAmount

    @Column(name = "ActualFiatValueAtTransactionDate")
    private Double actualFiatValueAtTransactionDate;        // actual value at time of sale, adjusted cost/sale price depending on what the transaction is (ie remitance is a sale for $0)


    @Column(name = "FiatFeeValueAtTransactionDate")
    private Double fiatFeeValueAtTransactionDate;           // fiat fee value : coin price * fee

    @Column(name = "FiatRunningCost")
    private Double fiatRunningCost;                         // priorRunningCost + coinAmount * fiatPrice

    @Column(name = "FiatRunningAverageUnitPrice")
    private Double fiatRunningAverageUnitPrice;             // calculated unit price based on all the cost bases and sale prices

    @Column(name = "FiatGainOrLossAtTransactionDate")
    private Double fiatGainOrLossAtTransactionDate;         // fiatValue - (runnaveUnit * coinAmount)


    @Column(name = "personalNote")
    private String personalNote;                            // user added comments to the transaction

    @Column(name = "transactionID")
    private String transactionID;                           // extra data for the report to link the transaction ID on the blockchain

    @Column(name = "transactionURL")
    private String transactionURL;                          // extra data for the report to link to blockchain explorer page


    // Getter and Setters ----------------------------------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Timestamp getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(Timestamp transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    public Double getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(Double coinAmount) {
        this.coinAmount = coinAmount;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(Double runningBalance) {
        this.runningBalance = runningBalance;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getFiatCurrencyCode() {
        return fiatCurrencyCode;
    }

    public void setFiatCurrencyCode(String fiatCurrencyCode) {
        this.fiatCurrencyCode = fiatCurrencyCode;
    }

    public Double getFiatPriceAtTransactionDate() {
        return fiatPriceAtTransactionDate;
    }

    public void setFiatPriceAtTransactionDate(Double fiatPriceAtTransactionDate) {
        this.fiatPriceAtTransactionDate = fiatPriceAtTransactionDate;
    }

    public Double getFiatValueAtTransactionDate() {
        return fiatValueAtTransactionDate;
    }

    public void setFiatValueAtTransactionDate(Double fiatValueAtTransactionDate) {
        this.fiatValueAtTransactionDate = fiatValueAtTransactionDate;
    }

    public Double getActualFiatValueAtTransactionDate() {
        return actualFiatValueAtTransactionDate;
    }

    public void setActualFiatValueAtTransactionDate(Double actualFiatValueAtTransactionDate) {
        this.actualFiatValueAtTransactionDate = actualFiatValueAtTransactionDate;
    }

    public Double getFiatFeeValueAtTransactionDate() {
        return fiatFeeValueAtTransactionDate;
    }

    public void setFiatFeeValueAtTransactionDate(Double fiatFeeValueAtTransactionDate) {
        this.fiatFeeValueAtTransactionDate = fiatFeeValueAtTransactionDate;
    }

    public Double getFiatRunningCost() {
        return fiatRunningCost;
    }

    public void setFiatRunningCost(Double fiatRunningCost) {
        this.fiatRunningCost = fiatRunningCost;
    }

    public Double getFiatRunningAverageUnitPrice() {
        return fiatRunningAverageUnitPrice;
    }

    public void setFiatRunningAverageUnitPrice(Double fiatRunningAverageUnitPrice) {
        this.fiatRunningAverageUnitPrice = fiatRunningAverageUnitPrice;
    }

    public Double getFiatGainOrLossAtTransactionDate() {
        return fiatGainOrLossAtTransactionDate;
    }

    public void setFiatGainOrLossAtTransactionDate(Double fiatGainOrLossAtTransactionDate) {
        this.fiatGainOrLossAtTransactionDate = fiatGainOrLossAtTransactionDate;
    }

    public String getPersonalNote() {
        return personalNote;
    }

    public void setPersonalNote(String personalNote) {
        this.personalNote = personalNote;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionURL() {
        return transactionURL;
    }

    public void setTransactionURL(String transactionURL) {
        this.transactionURL = transactionURL;
    }
}
