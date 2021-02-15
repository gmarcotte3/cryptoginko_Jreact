package com.marcotte.blockhead.services.financial;

import com.marcotte.blockhead.model.fiat.FiatCurrencyList;
import com.marcotte.blockhead.model.wallet.WalletTransaction;
import com.marcotte.blockhead.services.explorerServices.pricequote.CoinGeckoService;
import com.marcotte.blockhead.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Process transaction list for financial analysis
 *
 * calculate gain or los on a transaction list.
 * ... more to come
 */
@Service
public class TransationAnalysisService {

    @Autowired
    CoinGeckoService coinGeckoService;

    /**
     * calculate gain and loss of each transaction
     *
     * uses the average cost method
     *
     * This routine will calculate the gain and loss of each transaction by finding the
     * unit price at the time of the transaction. tracking the running cost, running average unit cost
     * when item is withdrawn the gain or loss is calculated:  valueAtSale - (averageUnitCost * numberCoinsSold)
     * @param walletTransactions
     * @param fiatCode
     * @param cryptoTicker
     */
    public void calculateGainLossTransactions(List<WalletTransaction> walletTransactions, String fiatCode, String cryptoTicker)
    {
        Double fiatRunningCost = 0.0;                 // priorRunningCost + coinAmount * fiatPrice
        Double fiatRunningAverageUnitPrice = 0.0;
        Double runningBalance = 0.0;

        for( int i =0 ; i < walletTransactions.size(); i++) {
            Timestamp transDate = walletTransactions.get(i).getTransactionTimestamp();
            String transDate_mmddyyyy = Utils.timestampToDateStr_mmddyyyy(transDate);
            FiatCurrencyList fiatCurrencyPriceList = coinGeckoService.getPriceByCoinAndDate(cryptoTicker, transDate_mmddyyyy);

            // calculate the fiat values for the coin amout and the transaction fee.
            Double fiatPriceAtTransactionDate = (fiatCurrencyPriceList.findFiat(fiatCode)).getValue();
            walletTransactions.get(i).setFiatPriceAtTransactionDate(fiatPriceAtTransactionDate);
            walletTransactions.get(i).setFiatCurrencyCode(fiatCode);
            walletTransactions.get(i).setFiatFeeValueAtTransactionDate(walletTransactions.get(i).getFee() * fiatPriceAtTransactionDate);

            // track the running balance here
            runningBalance += walletTransactions.get(i).getCoinAmount() + walletTransactions.get(i).getFee();
            walletTransactions.get(i).setBalance(runningBalance);

            // track the running averate unit price, running fiat cost, and if sale calculate gain/loss
            Double fiatValueAtTransactionDate = walletTransactions.get(i).getCoinAmount() * fiatPriceAtTransactionDate;
            walletTransactions.get(i).setFiatValueAtTransactionDate(fiatValueAtTransactionDate);
            if (fiatValueAtTransactionDate > 0 ) {
                // this is a deposit or receiving coin so just calculate running cost, average unit cost.
                fiatRunningCost += fiatValueAtTransactionDate;
                walletTransactions.get(i).setFiatRunningCost(fiatRunningCost);
                fiatRunningAverageUnitPrice = fiatRunningCost / walletTransactions.get(i).getBalance();
                walletTransactions.get(i).setFiatRunningAverageUnitPrice(fiatRunningAverageUnitPrice);
            } else {
                // this is a sale so we calculate gain and loss
                Double fiatCostAtSaleTransaction = walletTransactions.get(i).getCoinAmount() * fiatRunningAverageUnitPrice;
                walletTransactions.get(i).setFiatGainOrLossAtTransactionDate( -(fiatValueAtTransactionDate) + fiatCostAtSaleTransaction);
                fiatRunningCost += fiatCostAtSaleTransaction;
                walletTransactions.get(i).setFiatRunningCost(fiatRunningCost);
                walletTransactions.get(i).setFiatRunningAverageUnitPrice(fiatRunningAverageUnitPrice);
            }
        }
    }

}
