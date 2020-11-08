package com.marcotte.blockhead.datastore;

import com.marcotte.blockhead.model.CoinDTO;
import com.marcotte.blockhead.model.FiatCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoinService {

    @Autowired
    private CoinRepository coinRepository;

    public void save( Coin coin) {
        coinRepository.save(coin);
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
