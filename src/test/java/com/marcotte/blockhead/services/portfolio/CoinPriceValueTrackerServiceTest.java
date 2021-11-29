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

        coinPriceValueTrackerService.deleteAll();
    }

    @Test
    public void testSave() {
        System.out.println("testSave");
    }

    @Test
    public void findByID() {
        System.out.println("findByID");
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findAllByPriceDate() {
    }

    @Test
    public void findAllByPriceDateAndTicker() {
    }

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
}