/*
 * Copyright (c) 2021. tranquilitySoftware
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcotte.blockhead.services.financial;

import com.marcotte.blockhead.datastore.wallets.WalletTransactionTracker;
import com.marcotte.blockhead.model.wallet.WalletTransationType;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@TestPropertySource("WalletTransactionTrackerServiceTest.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class WalletTransactionTrackerServiceTest {

    @Autowired
    WalletTransactionTrackerService walletTransactionTrackerService;


    @Test
    public void save() {
        WalletTransactionTracker walletTransactionTracker = createOneWalletTracker();
        walletTransactionTrackerService.save(walletTransactionTracker);

        List<WalletTransactionTracker> foundAll = walletTransactionTrackerService.findAll();
        assertEquals(1, foundAll.size());

        walletTransactionTrackerService.deleteAll();
    }

    @Test
    public void testSave() {
        List<WalletTransactionTracker> trackerList = createListOfTracker();
        walletTransactionTrackerService.save(trackerList);

        List<WalletTransactionTracker> foundAll = walletTransactionTrackerService.findAll();
        assertEquals(2, foundAll.size());

        walletTransactionTrackerService.deleteAll();
    }

    @Test
    public void findAllByWalletNameAndTicker() {
    }


    /**
     * create one record
     * @return
     */
    private WalletTransactionTracker createOneWalletTracker() {
        Timestamp transactionTimestamp = new Timestamp(System.currentTimeMillis());
        WalletTransactionTracker walletTransactionTracker = new WalletTransactionTracker();

        // @Column(name = "WalletName", nullable = false)
        // @Column(name = "Ticker", nullable = false)
        // @Column(name = "TransactionTimestamp", nullable = false)
        // @Column(name = "coinAmount", nullable = false)
        // @Column(name = "fee")
        // @Column(name = "RunningBalance", nullable = false)
        // @Column(name = "TransactionType", nullable = false)
        // @Column(name = "FiatCurrencyCode", nullable = false)
        // @Column(name = "FiatPriceAtTransactionDate")
        // @Column(name = "FiatValueAtTransactionDate")
        // @Column(name = "ActualFiatValueAtTransactionDate")
        // @Column(name = "FiatFeeValueAtTransactionDate")
        // @Column(name = "FiatRunningCost")
        // @Column(name = "FiatRunningAverageUnitPrice")
        // @Column(name = "FiatGainOrLossAtTransactionDate")
        // @Column(name = "personalNote")
        // @Column(name = "transactionID")
        // @Column(name = "transactionURL")

        walletTransactionTracker.setWalletName("MyMoonBags");                                   // required
        walletTransactionTracker.setTicker("BTC");                                              // required
        walletTransactionTracker.setTransactionTimestamp(transactionTimestamp);                 // required
        walletTransactionTracker.setCoinAmount(100.0);                                          // required
        walletTransactionTracker.setFee(0.01);
        walletTransactionTracker.setRunningBalance(0.0);                                        // required
        walletTransactionTracker.setTransactionType(WalletTransationType.BUY_COINS.code);       // required
        walletTransactionTracker.setFiatCurrencyCode("USD");                                    // required
        walletTransactionTracker.setFiatValueAtTransactionDate(210.0);
        walletTransactionTracker.setActualFiatValueAtTransactionDate(200.0);
        walletTransactionTracker.setFiatFeeValueAtTransactionDate(400000.0);
        walletTransactionTracker.setFiatRunningCost(220.0);
        walletTransactionTracker.setFiatRunningAverageUnitPrice(199.5);
        walletTransactionTracker.setFiatGainOrLossAtTransactionDate(150.0);
        walletTransactionTracker.setPersonalNote("I take this personally");
        walletTransactionTracker.setTransactionID("transID1234dfkjhj23j23h2j3hh23g423hg");
        walletTransactionTracker.setTransactionURL("blockchainexplorer.com/transactionid=XXXXXXxxxxxxxxxxxxxxxxx");

        return walletTransactionTracker;
    }

    private List<WalletTransactionTracker> createListOfTracker() {
        List<WalletTransactionTracker> trackerList = new ArrayList<>();

        Timestamp transactionTimestamp = new Timestamp(System.currentTimeMillis());
        WalletTransactionTracker walletTransactionTracker = new WalletTransactionTracker();

        walletTransactionTracker.setWalletName("MyMoonBags");                                   // required
        walletTransactionTracker.setTicker("BTC");                                              // required
        walletTransactionTracker.setTransactionTimestamp(transactionTimestamp);                 // required
        walletTransactionTracker.setCoinAmount(100.0);                                          // required
        walletTransactionTracker.setFee(0.01);
        walletTransactionTracker.setRunningBalance(0.0);                                        // required
        walletTransactionTracker.setTransactionType(WalletTransationType.BUY_COINS.code);       // required
        walletTransactionTracker.setFiatCurrencyCode("USD");                                    // required
        walletTransactionTracker.setFiatValueAtTransactionDate(210.0);
        walletTransactionTracker.setActualFiatValueAtTransactionDate(200.0);
        walletTransactionTracker.setFiatFeeValueAtTransactionDate(400000.0);
        walletTransactionTracker.setFiatRunningCost(220.0);
        walletTransactionTracker.setFiatRunningAverageUnitPrice(199.5);
        walletTransactionTracker.setFiatGainOrLossAtTransactionDate(150.0);
        walletTransactionTracker.setPersonalNote("I take this personally");
        walletTransactionTracker.setTransactionID("transID1234dfkjhj23j23h2j3hh23g423hg");
        walletTransactionTracker.setTransactionURL("blockchainexplorer.com/transactionid=XXXXXXxxxxxxxxxxxxxxxxx");

        trackerList.add(walletTransactionTracker);

        walletTransactionTracker = new WalletTransactionTracker();

        walletTransactionTracker.setWalletName("MyMoonBags");                                   // required
        walletTransactionTracker.setTicker("BTC");                                              // required
        walletTransactionTracker.setTransactionTimestamp(transactionTimestamp);                 // required
        walletTransactionTracker.setCoinAmount(1000.0);                                         // required
        walletTransactionTracker.setFee(0.0199);
        walletTransactionTracker.setRunningBalance(0.0);                                        // required
        walletTransactionTracker.setTransactionType(WalletTransationType.DEPOSIT_COIN.code);    // required
        walletTransactionTracker.setFiatCurrencyCode("USD");                                    // required
        walletTransactionTracker.setFiatValueAtTransactionDate(2105.0);
        walletTransactionTracker.setActualFiatValueAtTransactionDate(2001.0);
        walletTransactionTracker.setFiatFeeValueAtTransactionDate(4000050.0);
        walletTransactionTracker.setFiatRunningCost(2200.0);
        walletTransactionTracker.setFiatRunningAverageUnitPrice(1990.5);
        walletTransactionTracker.setFiatGainOrLossAtTransactionDate(1500.0);
        walletTransactionTracker.setPersonalNote("Be professional and stand on the plastic");
        walletTransactionTracker.setTransactionID("transID1234dfkjhj23j23h2j3h11234523hg");
        walletTransactionTracker.setTransactionURL("blockchainexplorer.com/transactionid=XXXXYYYYYYYYYYYYYYxxxx");
        trackerList.add(walletTransactionTracker);

        return trackerList;
    }

}