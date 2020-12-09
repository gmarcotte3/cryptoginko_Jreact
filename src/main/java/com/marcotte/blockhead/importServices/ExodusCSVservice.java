package com.marcotte.blockhead.importServices;

import com.marcotte.blockhead.explorerServices.pricequote.CoinGeckoService;
import com.marcotte.blockhead.model.FiatCurrency;
import com.marcotte.blockhead.model.FiatCurrencyList;
import com.marcotte.blockhead.model.WalletTransaction;
import com.marcotte.blockhead.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ExodusCSVservice {
    private static final Logger log = LoggerFactory.getLogger(com.marcotte.blockhead.wallets.exodus.ExodosCsvService.class);

    private static final int TXID_COL = 0;
    private static final int TXURL_COL = 1;
    private static final int DATE_COL = 2;
    private static final int TYPE_COL = 3;
    private static final int COIN_AMOUNT_COL = 4;
    private static final int FEE_COL = 5;
    private static final int BALANCE_COL = 6;
    private static final int EXCHANGE_COL = 7;
    private static final int PERSONAL_NOTE_COL = 8;

    private static String DATE_PATTERN = "EEE MMM dd yyyy HH:mm:ss z";

    @Autowired
    CoinGeckoService coinGeckoService;

    public List<WalletTransaction> parseTransactionCsv(List<List<String>> csvFileArray) {
        List<WalletTransaction> walletTransactions = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp rightNow = new Timestamp(now.getTime());

        if ( csvFileArray == null || csvFileArray.size() == 0)
        {
            return walletTransactions;
        }

        // parse the transactions skip the first line that is the header.
        for( int j = 1 ; j < csvFileArray.size(); j++) {

            List<String> row = csvFileArray.get(j);
            WalletTransaction walletTransaction = new WalletTransaction();
            walletTransaction.setTransactionID( row.get(TXID_COL));
            walletTransaction.setTransactionURL( row.get(TXURL_COL));

            Double coinAmount = amountStringToDouble(row.get(COIN_AMOUNT_COL));
            walletTransaction.setCoinAmount(coinAmount);

            Double coinBalance = amountStringToDouble(row.get(BALANCE_COL));
            walletTransaction.setBalance(coinBalance);

            Double fee = amountStringToDouble(row.get(FEE_COL));
            walletTransaction.setFee(fee);

            String dateStr = row.get(DATE_COL);
            Timestamp transactionDate = rightNow;
            try {
                transactionDate = exodusDateClean(dateStr);
            } catch ( Exception e) {
                log.error("date format parse error [" + dateStr + "]");
            }
            walletTransaction.setTransactionTimestamp(transactionDate);

            walletTransaction.setExchangeNote(row.get(EXCHANGE_COL));
            walletTransaction.setPersonalNote(row.get(PERSONAL_NOTE_COL));


            walletTransactions.add(walletTransaction);
        }

            return walletTransactions;
    }

    private Timestamp exodusDateClean( String dateStr ) {
        int timeZoneIndex = dateStr.indexOf("GMT");
        String dateStrClean = dateStr.substring(0, timeZoneIndex + 6) + ":" + dateStr.substring(timeZoneIndex + 6, timeZoneIndex + 8);
        Timestamp transactionDate = Utils.dateConvert(dateStrClean, DATE_PATTERN );
        return transactionDate;
    }

    private Double amountStringToDouble(String coinBalanceStr) {
        String cleanString = coinBalanceStr.trim();
        Double cleanAmount = 0.0;
        if ( cleanString == null || cleanString.length() == 0) {
            return 0.0;
        }
        cleanString = cleanString.substring(0, cleanString.indexOf(' '));
        try {
            cleanAmount = Double.valueOf(cleanString);
        } catch ( Exception e) {
            log.error("failed to convert amount input=>" + coinBalanceStr + "<=,  assuming 0.0" );

            cleanAmount = 0.0;
        }

        return cleanAmount;
    }

    public void  calculateGainLostTransactions(List<WalletTransaction> walletTransactions, String fiatCode, String cryptoTicker) {

        Double fiatRunningBalanceAtTransactionDate = 0.0;
        Double fiatRunningCost = 0.0;                 // priorRunningCost + coinAmount * fiatPrice
        Double fiatRunningAverageUnitPrice = 0.0;



        for( int i =0 ; i < walletTransactions.size(); i++) {
            Timestamp transDate = walletTransactions.get(i).getTransactionTimestamp();
            String transDate_mmddyyyy = Utils.timestampToDateStr_mmddyyyy(transDate);
            FiatCurrencyList fiatCurrencyPriceList = coinGeckoService.getPriceByCoinAndDate(cryptoTicker, transDate_mmddyyyy);

            Double fiatPriceAtTransactionDate = (fiatCurrencyPriceList.findFiat(fiatCode)).getValue();
            walletTransactions.get(i).setFiatPriceAtTransactionDate(fiatPriceAtTransactionDate);
            walletTransactions.get(i).setFiatCurrencyCode(fiatCode);
            walletTransactions.get(i).setFiatFeeValueAtTransactionDate(walletTransactions.get(i).getFee() * fiatPriceAtTransactionDate);

            Double fiatValueAtTransactionDate = walletTransactions.get(i).getCoinAmount() * fiatPriceAtTransactionDate;
            walletTransactions.get(i).setFiatValueAtTransactionDate(fiatValueAtTransactionDate);
            if (fiatValueAtTransactionDate > 0 ) {
                fiatRunningCost += fiatValueAtTransactionDate;
                walletTransactions.get(i).setFiatRunningCost(fiatRunningCost);
                fiatRunningAverageUnitPrice = fiatRunningCost / walletTransactions.get(i).getBalance();
                walletTransactions.get(i).setFiatRunningAverageUnitPrice(fiatRunningAverageUnitPrice);
            } else {
                Double fiatCostAtSaleTransaction = walletTransactions.get(i).getCoinAmount() * fiatRunningAverageUnitPrice;
                walletTransactions.get(i).setFiatGainOrLossAtTransactionDate( -(fiatValueAtTransactionDate) - fiatCostAtSaleTransaction);
                fiatRunningCost += fiatCostAtSaleTransaction;
                walletTransactions.get(i).setFiatRunningCost(fiatRunningCost);
                walletTransactions.get(i).setFiatRunningAverageUnitPrice(fiatRunningAverageUnitPrice);
            }
        }

    }

    /**
     * convert the time stamp into
     * @param timestamp
     * @return  return string in the form dd-mm-yyyy
     */
    private String timeStampToDateStr_ddmmyyyy( Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(new Date(timestamp.getYear(), timestamp.getMonth()-1, timestamp.getDay()));
    }

}
