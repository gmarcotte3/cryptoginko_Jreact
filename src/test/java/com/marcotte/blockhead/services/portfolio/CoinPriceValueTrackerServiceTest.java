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

import com.marcotte.blockhead.datastore.portfolio.CoinPriceValueTracker;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@TestPropertySource("CoinPriceValueTrackerServiceTest.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class CoinPriceValueTrackerServiceTest {

    @Autowired
    CoinPriceValueTrackerService coinPriceValueTrackerService;

    @BeforeAll
    void seetUpOnce() {
        System.out.println("seetUpOnce");
    }


    @BeforeEach
    void seetUp() {
        System.out.println("setupBeforeEach");
    }

    /**
     * test the save. create a list (has no id set) save it read it  and see the id set see all
     * the original set values round trip though the database correctly.
     *
     * Test making a change to an existing data and see if it was updated (not a new insert)
     */
    @Test
    public void save() {
        List<CoinPriceValueTracker> coinlist = createCoinValueList();
        coinPriceValueTrackerService.save( coinlist.get(0));

        List<CoinPriceValueTracker> savedcoins = coinPriceValueTrackerService.findAll();
        assertEquals(1, savedcoins.size());
        assertEquals("ADA", savedcoins.get(0).getTicker());
        assertEquals("USD", savedcoins.get(0).getCoinPriceFiatTicker());
        assertEquals( (Long) 10L, (Long) (savedcoins.get(0).getCoinPrice()).longValue() );
        assertEquals( (Long) 100000L, (Long) (savedcoins.get(0).getCoinBalance()).longValue() );
        assertEquals( LocalDate.now(), savedcoins.get(0).getPriceDate());

        savedcoins.get(0).setCoinPrice(10000.0);
        coinPriceValueTrackerService.save(savedcoins.get(0));

        List<CoinPriceValueTracker> savedcoins2 = coinPriceValueTrackerService.findAll();
        assertEquals(1, savedcoins2.size());
        assertEquals("ADA", savedcoins2.get(0).getTicker());
        assertEquals("USD", savedcoins2.get(0).getCoinPriceFiatTicker());
        assertEquals( (Long) 10000L, (Long) (savedcoins2.get(0).getCoinPrice()).longValue() );
        assertEquals( (Long) 100000L, (Long) (savedcoins2.get(0).getCoinBalance()).longValue() );
        assertEquals( LocalDate.now(), savedcoins2.get(0).getPriceDate());

        coinPriceValueTrackerService.deleteAll();
    }

    @Test
    public void findByID() {
        List<CoinPriceValueTracker> coinlist = createCoinValueList2();
        coinPriceValueTrackerService.save( coinlist);
        List<CoinPriceValueTracker> savedcoins = coinPriceValueTrackerService.findAll();

        CoinPriceValueTracker foundcoin = coinPriceValueTrackerService.findByID(savedcoins.get(2).getId());
        assertTrue(foundcoin != null);
//        assertEquals(savedcoins.get(2), foundcoin);
        coinPriceValueTrackerService.deleteAll();
    }

    /**
     * test the find all function.
     */
    @Test
    public void findAll() {
        List<CoinPriceValueTracker> coinlist = createCoinValueList2();
        coinPriceValueTrackerService.save( coinlist);

        List<CoinPriceValueTracker> savedcoins2 = coinPriceValueTrackerService.findAll();
        assertEquals(4, savedcoins2.size());
        coinPriceValueTrackerService.deleteAll();
    }

    /**
     * find a list of cons by date
     */
    @Test
    public void findAllByPriceDate() {
        List<CoinPriceValueTracker> coinlist = createCoinValueList2();
        coinPriceValueTrackerService.save( coinlist);

        List<CoinPriceValueTracker> foundCcoinlist =coinPriceValueTrackerService.findAllByPriceDate(LocalDate.of(2021,6,7));

        //   coinPriceValueTracker = new CoinPriceValueTracker();
        //   coinPriceValueTracker.setCoinPrice( 200.1);
        //   coinPriceValueTracker.setCoinBalance(50.00);
        //   coinPriceValueTracker.setTicker("dash");
        //   coinPriceValueTracker.setPriceDate(LocalDate.of(2021,6,7));
        //   coinPriceValueTracker.setCoinPriceFiatTicker("usd");

        assertEquals(2, foundCcoinlist.size());
        assertEquals("DASH", foundCcoinlist.get(0).getTicker());
        assertEquals("USD", foundCcoinlist.get(0).getCoinPriceFiatTicker());
        assertEquals( (Long) 200L, (Long) (foundCcoinlist.get(0).getCoinPrice()).longValue() );
        assertEquals( (Long) 50L, (Long) (foundCcoinlist.get(0).getCoinBalance()).longValue() );
        assertEquals( LocalDate.of(2021,6,7), foundCcoinlist.get(0).getPriceDate());


        coinPriceValueTrackerService.deleteAll();
    }

    @Test
    public void findAllByPriceDateAndTicker() {
        List<CoinPriceValueTracker> coinlist = createCoinValueList2();
        coinPriceValueTrackerService.save( coinlist);

        List<CoinPriceValueTracker> foundCcoinlist =coinPriceValueTrackerService.findAllByPriceDateAndTicker(LocalDate.of(2021,6,7), "dash");
//        coinPriceValueTracker = new CoinPriceValueTracker();
//        coinPriceValueTracker.setCoinPrice( 200.1);
//        coinPriceValueTracker.setCoinBalance(50.00);
//        coinPriceValueTracker.setTicker("dash");
//        coinPriceValueTracker.setPriceDate(LocalDate.of(2021,6,7));
//        coinPriceValueTracker.setCoinPriceFiatTicker("usd");

        assertEquals(1, foundCcoinlist.size());
        assertEquals("DASH", foundCcoinlist.get(0).getTicker());
        assertEquals("USD", foundCcoinlist.get(0).getCoinPriceFiatTicker());
        assertEquals( (Long) 200L, (Long) (foundCcoinlist.get(0).getCoinPrice()).longValue() );
        assertEquals( (Long) 50L, (Long) (foundCcoinlist.get(0).getCoinBalance()).longValue() );
        assertEquals( LocalDate.of(2021,6,7), foundCcoinlist.get(0).getPriceDate());


        coinPriceValueTrackerService.deleteAll();
    }

    //==================================================================================================================
    // test utilities
    //==================================================================================================================

    /**
     * create a data list of one coin.
     * @return
     */
    private List<CoinPriceValueTracker> createCoinValueList() {
        List<CoinPriceValueTracker>  coinlist = new ArrayList<CoinPriceValueTracker>();

        CoinPriceValueTracker coinPriceValueTracker = new CoinPriceValueTracker();
        coinPriceValueTracker.setCoinPrice( 10.1);
        coinPriceValueTracker.setCoinBalance(100000.00);
        coinPriceValueTracker.setTicker("ada");
        coinPriceValueTracker.setPriceDate(LocalDate.now());
        coinPriceValueTracker.setCoinPriceFiatTicker("usd");

        coinlist.add(coinPriceValueTracker);
        return coinlist;
    }

    /**
     * create 4 coins with 4 different coins
     * @return
     */
    private List<CoinPriceValueTracker> createCoinValueList2() {
        List<CoinPriceValueTracker>  coinlist = new ArrayList<CoinPriceValueTracker>();

        CoinPriceValueTracker coinPriceValueTracker = new CoinPriceValueTracker();
        coinPriceValueTracker.setCoinPrice( 10.2);
        coinPriceValueTracker.setCoinBalance(123.00);
        coinPriceValueTracker.setTicker("btc");
        coinPriceValueTracker.setPriceDate(LocalDate.now());
        coinPriceValueTracker.setCoinPriceFiatTicker("nzd");

        coinlist.add(coinPriceValueTracker);

        coinPriceValueTracker = new CoinPriceValueTracker();
        coinPriceValueTracker.setCoinPrice( 10.1);
        coinPriceValueTracker.setCoinBalance(100000.00);
        coinPriceValueTracker.setTicker("ada");
        coinPriceValueTracker.setPriceDate(LocalDate.of(2021, 1, 15));
        coinPriceValueTracker.setCoinPriceFiatTicker("usd");

        coinlist.add(coinPriceValueTracker);

        coinPriceValueTracker = new CoinPriceValueTracker();
        coinPriceValueTracker.setCoinPrice( 200.1);
        coinPriceValueTracker.setCoinBalance(50.00);
        coinPriceValueTracker.setTicker("dash");
        coinPriceValueTracker.setPriceDate(LocalDate.of(2021,6,7));
        coinPriceValueTracker.setCoinPriceFiatTicker("usd");

        coinlist.add(coinPriceValueTracker);


        coinPriceValueTracker = new CoinPriceValueTracker();
        coinPriceValueTracker.setCoinPrice( 54.3);
        coinPriceValueTracker.setCoinBalance(30.00);
        coinPriceValueTracker.setTicker("dot");
        coinPriceValueTracker.setPriceDate(LocalDate.of(2021,9,17));
        coinPriceValueTracker.setCoinPriceFiatTicker("aud");


        coinPriceValueTracker = new CoinPriceValueTracker();
        coinPriceValueTracker.setCoinPrice( 77.3);
        coinPriceValueTracker.setCoinBalance(40.00);
        coinPriceValueTracker.setTicker("dot");
        coinPriceValueTracker.setPriceDate(LocalDate.of(2021,6,7));
        coinPriceValueTracker.setCoinPriceFiatTicker("nzd");

        coinlist.add(coinPriceValueTracker);
        return coinlist;
    }
}