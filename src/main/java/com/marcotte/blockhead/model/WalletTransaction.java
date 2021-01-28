package com.marcotte.blockhead.model;

import com.opencsv.bean.CsvBindByName;

import java.sql.Timestamp;

public class WalletTransaction {
    private String transactionID;
    private String transactionURL;
    private Timestamp transactionTimestamp;
    private Double coinAmount;
    private Double fee;
    private Double balance;                         // running balance
    private String fiatCurrencyCode;
    private Double fiatPriceAtTransactionDate;      // fiat price of the coin at transaction time
    private Double fiatValueAtTransactionDate;      // fiat value of coin price * coinAmount
    private Double fiatFeeValueAtTransactionDate;   // fiat fee value : price * fee
    private Double fiatRunningCost;                 // priorRunningCost + coinAmount * fiatPrice
    private Double fiatRunningAverageUnitPrice;
    private Double fiatGainOrLossAtTransactionDate;  // fiatValue - (runnaveUnit * coinAmount)
    private String exchangeNote;
    private String personalNote;

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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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

    public String getExchangeNote() {
        return exchangeNote;
    }

    public void setExchangeNote(String exchangeNote) {
        this.exchangeNote = exchangeNote;
    }

    public String getPersonalNote() {
        return personalNote;
    }

    public void setPersonalNote(String personalNote) {
        this.personalNote = personalNote;
    }
}
