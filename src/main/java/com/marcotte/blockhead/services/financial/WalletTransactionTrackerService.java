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
import com.marcotte.blockhead.datastore.wallets.WalletTransactionTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This service provides saving and retrieving wallet transaction tracking data from the databsae.
 */
@Service
public class WalletTransactionTrackerService {

    @Autowired
    private WalletTransactionTrackerRepository walletTransactionTrackerRepository;

    /**
     * save one walletTransactionTracker
     * @param walletTransactionTracker
     */
    public void save( WalletTransactionTracker walletTransactionTracker) {
        walletTransactionTrackerRepository.save(walletTransactionTracker);
    }

    /**
     * save a list
     * @param walletTransactionTrackerList
     */
    public void save(List<WalletTransactionTracker> walletTransactionTrackerList)
    {
        for (WalletTransactionTracker walletTransactionTracker : walletTransactionTrackerList )
        {
            save( walletTransactionTracker);
        }
    }

    /**
     * returns wallet transaction set (transaction of a coin in a wallet)
     * @param walletName
     * @param ticker
     * @return
     */
    public List<WalletTransactionTracker> findAllByWalletNameAndTicker(String walletName, String ticker) {
        return walletTransactionTrackerRepository.findAllByWalletNameAndTickerOrderByTransactionTimestamp(walletName, ticker);
    }

    public List<WalletTransactionTracker> findAll() {
        List<WalletTransactionTracker> foundAll = new ArrayList<>();
        for (WalletTransactionTracker walletTransactionTracker: walletTransactionTrackerRepository.findAll())
        {
            foundAll.add(walletTransactionTracker);
        }
        return foundAll;
    }

    public void deleteAll() {
        walletTransactionTrackerRepository.deleteAll();
    }

}
