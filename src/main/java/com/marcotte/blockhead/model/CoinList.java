package com.marcotte.blockhead.model;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;

import java.util.ArrayList;
import java.util.List;


public class CoinList
{
    private String coinName;
    private List<BlockchainAddressStore> coins;
    private List<Currency> fiat_balances;
    private List<String> errorMessages;
    private double balance;

    public CoinList()
    {
        errorMessages = new ArrayList<String>();
        balance = 0.0;
    }

    public void calculateCoinBalance()
    {
        balance = 0.0;
        if ( coins == null ) return;

        for (BlockchainAddressStore address: getCoins())
        {
            balance += address.getLastBalance();
        }
    }

    public void calculateFietBalances(QuoteGeneric quoteGeneric)
    {
        List<Currency> fiatBalances = new ArrayList<>();
        for (Currency fiat :  quoteGeneric.getCurrency())
        {
            Currency currency = new Currency();
            currency.setSymbol(fiat.getSymbol());
            currency.setCode( fiat.getCode());
            currency.setRate( getBalance() * fiat.getRate());
            currency.setDescription( fiat.getDescription());

            fiatBalances.add(currency);
        }
        setFiat_balances(fiatBalances);
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public List<BlockchainAddressStore> getCoins() {
        return coins;
    }

    public void setCoins(List<BlockchainAddressStore> coins) {
        this.coins = coins;
    }

    public List<Currency> getFiat_balances() {
        return fiat_balances;
    }

    public void setFiat_balances(List<Currency> fiat_balances) {
        this.fiat_balances = fiat_balances;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addErrorMessage(String errorMessage)
    {
        if ( this.errorMessages == null )
        {
            this.errorMessages = new ArrayList<String>();
        }
        this.errorMessages.add(errorMessage);
    }
}