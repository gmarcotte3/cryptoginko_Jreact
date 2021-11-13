package com.marcotte.blockhead.services.portfolio;


import com.marcotte.blockhead.datastore.coin.Coin;
import com.marcotte.blockhead.datastore.coin.CoinRepository;
import com.marcotte.blockhead.model.QuoteGeneric;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.services.blockchainaddressstore.BlockchainAddressStoreService;
import com.marcotte.blockhead.services.coin.CoinService;
import com.marcotte.blockhead.services.explorerServices.pricequote.PriceServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *  Service to provide the current price of coins in the wallet. It only uses coins that are used by the wallet.
 */
@Service
public class CurrentPriceService {

    @Autowired
    private PriceServiceInterface coinGeckoService;

    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;

    @Autowired
    private CoinService coinService;
    @Autowired
    private CoinRepository coinRepository;


    private List<CoinDTO> currentTrackedCoins;      // cache of the current tracked coin prices.
    private  Timestamp lastUpdated;                 // timestamp of the last cache prices

    public CurrentPriceService() {
        this.currentTrackedCoins = new ArrayList<CoinDTO>();
    }

    /**
     * use the price oracle to get a list of coin prices for each coin tracked by this application.
     * with the side effect of saving a cache of the coin prices.
     * @return
     */
    public  List<CoinDTO> getPriceAllTrackedCoinsNow() {
        List<CoinDTO> coinsFromOnlineOracle = coinGeckoService.getPriceAllCoinsNow();
        List<CoinDTO> coinsTracked = new ArrayList<>();
        List<String>  tickers = blockchainAddressStoreService.findAllTickers();

        // create a map of all the coins from oricle
        HashMap<String, CoinDTO> coinMap = new HashMap<String, CoinDTO>();
        for (CoinDTO coin : coinsFromOnlineOracle ) {
            coinMap.put( coin.getTicker(), coin);
        }

        for ( String ticker : tickers) {
            CoinDTO coinDTO = coinMap.get(ticker);
            if ( coinDTO != null )  {
                coinsTracked.add(coinDTO);
            } else {
                List<Coin> coins = coinService.findByTicker(ticker);
                if ( coins.size() > 0 ) {
                    coinDTO = new CoinDTO(coins.get(0));
                    QuoteGeneric quote = coinGeckoService.getQuote(ticker);
                    if ( quote.getCoinDTO() != null ) {
                        coinDTO = quote.getCoinDTO();
                    }
                    coinsTracked.add(coinDTO);
                }
            }
        }

        setCurrentTrackedCoins(coinsTracked);   // side effect: update the cache
        return coinsTracked;
    }

    private void saveCacheCoinPrices() {
        for (CoinDTO coinDTO : currentTrackedCoins  ) {
            coinService.save( new Coin(coinDTO));
        }
    }


    /**
     * retrieve the last cache of coin price list.
     * @return
     */
    public List<CoinDTO> getCurrentTrackedCoins() {
        return currentTrackedCoins;
    }

    /**
     * update the cache
     * save the cache to the database.
     * @param currentTrackedCoins
     */
    public void setCurrentTrackedCoins(List<CoinDTO> currentTrackedCoins) {
        this.currentTrackedCoins = currentTrackedCoins;

        Date date = new Date();
        this.lastUpdated  = new Timestamp(date.getTime());

        saveCacheCoinPrices();
    }

    /**
     * get the age of the coin cache by returning the last timestamp of the updating
     * @return
     */
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    /**
     * get the current oracle of price service
     * @return
     */
    public PriceServiceInterface getCoinGeckoService() {
        return coinGeckoService;
    }

    /**
     * change the price service oracle
     * @param coinGeckoService
     */
    public void setCoinGeckoService(PriceServiceInterface coinGeckoService) {
        this.coinGeckoService = coinGeckoService;
    }
}
