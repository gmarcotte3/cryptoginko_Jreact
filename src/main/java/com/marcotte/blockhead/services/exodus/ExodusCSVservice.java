package com.marcotte.blockhead.services.exodus;

import com.marcotte.blockhead.model.wallet.WalletTransaction;
import com.marcotte.blockhead.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * this service handles all the Exodus CSV conversion logic.
 */
@Service
public class ExodusCSVservice {
    private static final Logger log = LoggerFactory.getLogger(com.marcotte.blockhead.wallets.exodus.ExodosCsvService.class);

    // transaction csv columns
    private static int TXID_COL = 0;
    private static int TXURL_COL = 1;
    private static int DATE_COL = 2;
    private static int TYPE_COL = 3;
    private static int FROMPORTFOLIO_COL = 4;
    private static int TOPORTFOLIO_COL = 5;
    private static int COINAMOUNT_COL = 6;
    private static int FEE_COL = 7;
    private static int BALANCE_COL = 8;
    private static int EXCHANGE_COL = 9;
    private static int PERSONALNOTE_COL = 10;

    private static String DATE_PATTERN = "EEE MMM dd yyyy HH:mm:ss z";


    public List<WalletTransaction> parseTransactionCsv(List<List<String>> csvFileArray) {
        List<WalletTransaction> walletTransactions = new ArrayList<>();

        // what time is it?
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp rightNow = new Timestamp(now.getTime());

        // return if the transaction list is empty
        if ( csvFileArray == null || csvFileArray.size() == 0)
        {
            return walletTransactions;
        }

        updateTransactionColumnIndexes( csvFileArray.get(0));
        // parse the transactions skip the first line that is the header.
        for( int j = 1 ; j < csvFileArray.size(); j++) {

            List<String> row = csvFileArray.get(j);
            WalletTransaction walletTransaction = new WalletTransaction();
            walletTransaction.setTransactionID( row.get(TXID_COL));
            walletTransaction.setTransactionURL( row.get(TXURL_COL));

            Double coinAmount = amountStringToDouble(row.get(COINAMOUNT_COL));
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
            walletTransaction.setPersonalNote(row.get(PERSONALNOTE_COL));

            walletTransactions.add(walletTransaction);
        }

        return walletTransactions;
    }

    /**
     * update the transaction column indexes by matching up the column names in the
     * header row
     *
     * We do this because Exodus adds new fields to its csv file export.
     * hopefully they dont change the column names too.
     *
     * @param headerArray  the header row is a list of strings
     */
    private void updateTransactionColumnIndexes( List<String> headerArray)
    {
        for ( int j = 0; j < headerArray.size(); j++ ) {
            if ( headerArray.get(j).compareToIgnoreCase("TXID") == 0) {
                TXID_COL = j;
                continue;
            }
            if ( headerArray.get(j).compareToIgnoreCase("TXURL") == 0) {
                TXURL_COL = j;
                continue;
            }
            if ( headerArray.get(j).compareToIgnoreCase("DATE") == 0) {
                DATE_COL = j;
                continue;
            }
            if ( headerArray.get(j).compareToIgnoreCase("TYPE") == 0) {
                TYPE_COL = j;
                continue;
            }
            if ( headerArray.get(j).compareToIgnoreCase("FROMPORTFOLIO") == 0) {
                FROMPORTFOLIO_COL = j;
                continue;
            }
            if ( headerArray.get(j).compareToIgnoreCase("TOPORTFOLIO") == 0) {
                TOPORTFOLIO_COL = j;
                continue;
            }
            if ( headerArray.get(j).compareToIgnoreCase("COINAMOUNT") == 0) {
                COINAMOUNT_COL = j;
                continue;
            }
            if ( headerArray.get(j).compareToIgnoreCase("FEE") == 0) {
                FEE_COL = j;
                continue;
            }
            if ( headerArray.get(j).compareToIgnoreCase("BALANCE") == 0) {
                BALANCE_COL = j;
                continue;
            }
            if ( headerArray.get(j).compareToIgnoreCase("EXCHANGE") == 0) {
                EXCHANGE_COL = j;
                continue;
            }
            if ( headerArray.get(j).compareToIgnoreCase("PERSONALNOTE") == 0) {
                PERSONALNOTE_COL = j;
                continue;
            }
        }
    }
    /**
     * Exodus date clean up
     * Need to do some preprocessing of the date comming from exodus csv output before we can convert
     * to a Timestamp.
     *
     * @param dateStr
     * @return
     */
    private Timestamp exodusDateClean( String dateStr ) {
        int timeZoneIndex = dateStr.indexOf("GMT");
        String dateStrClean = dateStr.substring(0, timeZoneIndex + 6) + ":" + dateStr.substring(timeZoneIndex + 6, timeZoneIndex + 8);
        Timestamp transactionDate = Utils.dateConvert(dateStrClean, DATE_PATTERN );
        return transactionDate;
    }

    /**
     * converts a string formated number into a double.
     * The input string is the form ####.#### XXX where there is a space following
     * the number and a coin currency code. The currency code is stripped out before
     * normal string to double conversion is done.
     *
     * @param coinBalanceStr    number string of the form "####.#### XXX"
     * @return
     */
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
