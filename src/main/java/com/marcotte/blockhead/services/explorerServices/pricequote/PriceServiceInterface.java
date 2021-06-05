package com.marcotte.blockhead.services.explorerServices.pricequote;

import com.marcotte.blockhead.model.QuoteGeneric;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;

import java.util.List;

/**
 * Interface to price quoting services on line
 */
public interface PriceServiceInterface {
    public List<CoinDTO> getPriceAllCoinsNow();
    public FiatCurrencyList getPriceByCoinAndDate(String coinTicker, String date_ddmmyyyy);
    public QuoteGeneric getQuote(String crypto);
}
