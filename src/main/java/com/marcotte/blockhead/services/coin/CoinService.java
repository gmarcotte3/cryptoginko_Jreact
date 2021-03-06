package com.marcotte.blockhead.services.coin;

import com.marcotte.blockhead.datastore.coin.Coin;
import com.marcotte.blockhead.datastore.coin.CoinRepository;
import com.marcotte.blockhead.model.QuoteGeneric;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.fiat.FiatCurrency;
import com.marcotte.blockhead.model.fiat.FiatNames;
import com.marcotte.blockhead.services.portfolio.PortfolioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CoinService {

    private static final Logger log = LoggerFactory.getLogger(CoinService.class);

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
     * get all the coin price and coin data as a hash map of ticker->CoinDTO
     * @return
     */
    public HashMap<String, CoinDTO> findAllReturnTickerCoinDTOMap()
    {
        HashMap<String, CoinDTO> coinMap = new HashMap<String, CoinDTO>();
        for (Coin coin : coinRepository.findAll()) {
            CoinDTO coinDTO = new CoinDTO();
            coinDTO.setCoinDTO(coin);
            coinMap.put( coin.getTicker(), coinDTO);
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

    public List<Coin>  updateCoins(QuoteGeneric quoteGeneric) {
        List<CoinDTO> coinDTOS = new ArrayList<CoinDTO>();
        coinDTOS.add( quoteGeneric.getCoinDTO());
        log.info("trying to save generic quote=" + quoteGeneric.toString() );

        return updateCoins(coinDTOS);
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

            newCoin.setPriceUSD( coinDTO.getFiat_prices().findFiat("USD").getValue());
            newCoin.setPriceNZD( coinDTO.getFiat_prices().findFiat("NZD").getValue());
            newCoin.setPriceAUD( coinDTO.getFiat_prices().findFiat("AUD").getValue());
            newCoin.setPriceJPY( coinDTO.getFiat_prices().findFiat("JPY").getValue());
            newCoin.setPriceJPM( coinDTO.getFiat_prices().findFiat("JPM").getValue());
            newCoin.setPriceEUR( coinDTO.getFiat_prices().findFiat("EUR").getValue());
            newCoin.setPriceGBP( coinDTO.getFiat_prices().findFiat("GBP").getValue());
            newCoin.setPriceKRW( coinDTO.getFiat_prices().findFiat("KRW").getValue());
            newCoin.setPriceINR( coinDTO.getFiat_prices().findFiat("INR").getValue());
            newCoin.setPriceETH( coinDTO.getFiat_prices().findFiat("ETH").getValue());
            newCoin.setPriceBTC( coinDTO.getFiat_prices().findFiat("BTC").getValue());

            save( newCoin);
            coinList.add(newCoin);
        }
        return coinList;
    }

    /**
     * clear the coin table
     */
    public void deleteAll() {
        coinRepository.deleteAll();
    }

}
