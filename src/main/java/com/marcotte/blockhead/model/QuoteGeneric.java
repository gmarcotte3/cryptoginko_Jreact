package com.marcotte.blockhead.model;

import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;


/**
 * generic coin price quote
 * contains a coinDTO that has coin information along with a list of fiat currencies with corresponding price
 * the CoinDTO also has a fiat value list but we are not using it in this class.
 */
public class QuoteGeneric
{
    private String timeISO;         // the time of the quote
    private CoinDTO coinDTO;        // coin info together with fiat price list.

    /**
     * constructor, initialize a coinDTO member.
     */
    public QuoteGeneric()
    {
       this.coinDTO = new CoinDTO();
    }

    public CoinDTO getCoinDTO() {
        return coinDTO;
    }

    public void setCoinDTO(CoinDTO coinDTO) {
        this.coinDTO = coinDTO;
    }

    /**
     * get the time of the quote
     * @return
     */
    public String getTimeISO() {
        return timeISO;
    }

    /**
     * set the time of the quote
     * @param timeISO
     * @return
     */
    public QuoteGeneric setTimeISO(String timeISO) {
        this.timeISO = timeISO;
        return this;
    }

    /**
     * get the coin name
     * @return
     */
    public String getCoinName() {
        return this.coinDTO.getCoinName();
    }

    public QuoteGeneric setCoinName(String coinName) {
        this.coinDTO.setCoinName(coinName);
        return this;
    }

    /**
     * get the coin ticker code, used by exchanges and price services like goingeko
     * @return
     */
    public String getSymbol() {
        return this.coinDTO.getTicker();
    }

    public QuoteGeneric setSymbol(String symbol) {
        this.coinDTO.setTicker(symbol);
        return this;
    }

    /**
     * return the price quotes in popular fiat currencies.
     * @return
     */
    public FiatCurrencyList getCurrency() {
        return this.coinDTO.getFiat_prices();
    }

    public QuoteGeneric setCurrency(FiatCurrencyList currencyList) {
        this.coinDTO.setFiat_prices(currencyList);
        return this;
    }

    @Override
    public String toString()
    {
        return "QuoteGeneric{" +
                "timeISO='" + timeISO + '\'' +
                ", coinName='" + this.getCoinName() + '\'' +
                ", symbol='" + this.getSymbol() + '\'' +
                ", currency=" + this.getCurrency() +
                '}';
    }
}