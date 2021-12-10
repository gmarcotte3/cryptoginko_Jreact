/*
 * Copyright (c) 2021. tranquilitySoftware
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcotte.blockhead.services.portfolio;

import com.marcotte.blockhead.config.BlockheadConfig;
import com.marcotte.blockhead.datastore.portfolio.CoinPriceValueTracker;
import com.marcotte.blockhead.datastore.portfolio.CoinPriceValueTrackerRepository;
import com.marcotte.blockhead.model.coin.CoinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoinPriceValueTrackerService {
    private static final Logger log = LoggerFactory.getLogger(CoinPriceValueTrackerService.class);

    @Autowired
    private CoinPriceValueTrackerRepository coinPriceValueTrackerRepository;

    @Autowired
    private BlockheadConfig blockheadConfig;


    /**
     * save a single record
     * @param coinPriceValueTracker
     */
    public void save(CoinPriceValueTracker coinPriceValueTracker)
    {
        coinPriceValueTrackerRepository.save(coinPriceValueTracker);
    }

    /**
     * save a full list of coins.
     * @param coinPriceValueTrackerList
     */
    public void save(List<CoinPriceValueTracker>  coinPriceValueTrackerList)
    {
        for ( CoinPriceValueTracker coin : coinPriceValueTrackerList ) {
            coinPriceValueTrackerRepository.save(coin);
        }
    }

    public void save (CoinDTO coinDTO, LocalDate nowDate ) {

        String fiatCurrencyDefault = blockheadConfig.getFiatCurrencyDefault();
        String fiatCurrencyDefault2 = blockheadConfig.getFiatCurrencyDefault2();
        String fiatCurrencyDefault3 = blockheadConfig.getFiatCurrencyDefault3();


        CoinPriceValueTracker coinPriceValueTracker;
        List<CoinPriceValueTracker> foundCoinTrackers = findAllByPriceDateAndTicker(nowDate, coinDTO.getTicker());
        if ( foundCoinTrackers != null && foundCoinTrackers.size() > 0) {
            coinPriceValueTracker = foundCoinTrackers.get(0);
        } else {
            coinPriceValueTracker = new CoinPriceValueTracker();
        }
        coinPriceValueTracker.setTicker(coinDTO.getTicker());
        coinPriceValueTracker.setCoinPrice(coinDTO.getFiat_prices().findFiat(fiatCurrencyDefault).getValue());
        coinPriceValueTracker.setCoinPrice2(coinDTO.getFiat_prices().findFiat(fiatCurrencyDefault2).getValue());
        coinPriceValueTracker.setCoinPrice3(coinDTO.getFiat_prices().findFiat(fiatCurrencyDefault3).getValue());
        coinPriceValueTracker.setCoinPriceFiatTicker(fiatCurrencyDefault);
        coinPriceValueTracker.setCoinPriceFiatTicker2(fiatCurrencyDefault2);
        coinPriceValueTracker.setCoinPriceFiatTicker3(fiatCurrencyDefault3);
        coinPriceValueTracker.setCoinBalance(coinDTO.getCoinBalance());
        coinPriceValueTracker.setPriceDate(nowDate);
        save(coinPriceValueTracker);
    }

    public void saveDTOs(List<CoinDTO>  coinDTOsSummary) {
        LocalDate nowDate = LocalDate.now();
        CoinPriceValueTracker coinPriceValueTracker;

        List<CoinPriceValueTracker> coinPriceValueTrackerList = new ArrayList<>();
        for ( CoinDTO coinDTO : coinDTOsSummary) {
            save (coinDTO, nowDate );
        }
        save(coinPriceValueTrackerList);
    }

    /**
     * find by database primary key
     * @param id
     * @return
     */
    public CoinPriceValueTracker findByID(Long id)
    {
        Optional<CoinPriceValueTracker> foundCoinPriceValueTracker =  coinPriceValueTrackerRepository.findById(id);
        if ( foundCoinPriceValueTracker.isPresent())
        {
            return foundCoinPriceValueTracker.get();
        }
        return null;
    }

    /**
     * find all records and return a list
     * this will return all of history.
     * @return
     */
    public List<CoinPriceValueTracker> findAll()
    {
        List<CoinPriceValueTracker> foundAll = new ArrayList<>();
        for (CoinPriceValueTracker coinPriceValueTracker: coinPriceValueTrackerRepository.findAll())
        {
            foundAll.add(coinPriceValueTracker);
        }
        return foundAll;
    }

    /**
     * find all for a single date.
     * @param priceDate
     * @return
     */
    public List<CoinPriceValueTracker> findAllByPriceDate(LocalDate priceDate)
    {
        return coinPriceValueTrackerRepository.findAllByPriceDate(priceDate);
    }

    /**
     * find all for a single date and a coin ticker.
     * @param priceDate
     * @param coinTticker
     * @return
     */
    public List<CoinPriceValueTracker> findAllByPriceDateAndTicker(LocalDate priceDate, String coinTticker)
    {
        List<CoinPriceValueTracker> foundAll = new ArrayList<>();
        for (CoinPriceValueTracker coinPriceValueTracker : coinPriceValueTrackerRepository.findAllByPriceDateAndTicker(priceDate, coinTticker.toUpperCase()) )
        {
            foundAll.add(coinPriceValueTracker);
        }
        return foundAll;
    }

    public void deleteAll() {
        coinPriceValueTrackerRepository.deleteAll();
    }
}
