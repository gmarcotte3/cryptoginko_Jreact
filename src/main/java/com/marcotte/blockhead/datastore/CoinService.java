package com.marcotte.blockhead.datastore;

import com.marcotte.blockhead.model.CoinDTO;
import com.marcotte.blockhead.model.FiatCurrency;
import com.marcotte.blockhead.model.FiatNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CoinService {

    @Autowired
    private CoinRepository coinRepository;

    public void save( Coin coin) {
        List<Coin> coinsfound = coinRepository.findByTicker(coin.getTicker());
        if ( coinsfound.size() > 0) {
            Coin coinfound = coinsfound.get(0);
            coinfound.setCoin(coin);
            coinRepository.save(coinfound);
        } else {
            coinRepository.save(coin);
        }
    }

    public List<Coin> findAll()
    {
        List<Coin> results = new ArrayList<Coin>();
        for (Coin coin : coinRepository.findAll())
        {
            results.add(coin);
        }
        return results;
    }

    /**
     * get all the coin price and coin data as a hash map.
     * @return
     */
    public HashMap<String, Coin> findAllReturnTickerCoinMap()
    {
        HashMap<String, Coin> coinMap = new HashMap<String, Coin>();
        for (Coin coin : coinRepository.findAll()) {
            coinMap.put( coin.getTicker(), coin);
        }
        return coinMap;
    }

    /**
     * a hash map of all coin+fiat = price
     * @return
     */
    public HashMap<String, FiatCurrency> findAllReturnTickerFiatHashmap()
    {
        //FiatCurrency(double value, FiatNames fiatType)
        HashMap<String, FiatCurrency> priceMap = new HashMap<String, FiatCurrency>();
        for (Coin coin : coinRepository.findAll())
        {
            priceMap.put( coin.getTicker() + "-USD",  new FiatCurrency(coin.getPriceUSD(), FiatNames.USD) );
            priceMap.put( coin.getTicker() + "-NZD",  new FiatCurrency(coin.getPriceNZD(), FiatNames.NZD) );
            priceMap.put( coin.getTicker() + "-AUD",  new FiatCurrency(coin.getPriceAUD(), FiatNames.AUD) );
            priceMap.put( coin.getTicker() + "-JPY",  new FiatCurrency(coin.getPriceJPY(), FiatNames.JPY) );
            priceMap.put( coin.getTicker() + "-JPM",  new FiatCurrency(coin.getPriceJPM(), FiatNames.JPM) );
            priceMap.put( coin.getTicker() + "-EUR",  new FiatCurrency(coin.getPriceEUR(), FiatNames.EUR) );
            priceMap.put( coin.getTicker() + "-GBP",  new FiatCurrency(coin.getPriceGBP(), FiatNames.GBP) );
            priceMap.put( coin.getTicker() + "-KRW",  new FiatCurrency(coin.getPriceKRW(), FiatNames.KRW) );
            priceMap.put( coin.getTicker() + "-INR",  new FiatCurrency(coin.getPriceINR(), FiatNames.INR) );
            priceMap.put( coin.getTicker() + "-BTC",  new FiatCurrency(coin.getPriceBTC(), FiatNames.BTC) );
            priceMap.put( coin.getTicker() + "-ETH",  new FiatCurrency(coin.getPriceETH(), FiatNames.ETH) );
        }
        return priceMap;
    }

    public List<Coin>  findByTicker(String ticker) {
        List<Coin> results = new ArrayList<>();
        for ( Coin coin : coinRepository.findByTicker( ticker))
        {
            results.add(coin);
        }
        return results;
    }

    public List<Coin> updateCoins(List<CoinDTO> coinDTOS) {
        List<Coin> coinList = new ArrayList<Coin>();
        for (CoinDTO coinDTO : coinDTOS ) {
            Coin newCoin;
            List<Coin> foundCoins = findByTicker(coinDTO.getTicker());
            if ( foundCoins.size() == 0) {
                newCoin =  new Coin();
                newCoin.setCoinName( coinDTO.getCoinName());
                newCoin.setTicker( coinDTO.getTicker());
                newCoin.setDescription(coinDTO.getCoinName());
            } else {
                newCoin = foundCoins.get(0);
            }

            for ( FiatCurrency fiat : coinDTO.getFiat_prices() ) {
                switch (fiat.getCode()) {
                    case "USD" :
                        newCoin.setPriceUSD( fiat.getValue());
                        break;
                    case "NZD" :
                        newCoin.setPriceNZD( fiat.getValue());
                        break;
                    case "AUD" :
                        newCoin.setPriceAUD( fiat.getValue());
                        break;
                    case "JPY" :
                        newCoin.setPriceJPY( fiat.getValue());
                        break;
                    case "JPM" :
                        newCoin.setPriceJPM( fiat.getValue());
                        break;
                    case "EUR" :
                        newCoin.setPriceEUR( fiat.getValue());
                        break;
                    case "GBP" :
                        newCoin.setPriceGBP( fiat.getValue());
                        break;
                    case "KRW" :
                        newCoin.setPriceKRW( fiat.getValue());
                        break;
                    case "INR" :
                        newCoin.setPriceINR( fiat.getValue());
                        break;
                    case "ETH" :
                        newCoin.setPriceETH( fiat.getValue());
                        break;
                    case "BTC" :
                        newCoin.setPriceBTC( fiat.getValue());
                        break;
                }
            }
            save( newCoin);
            coinList.add(newCoin);
        }
        return coinList;
    }
}
