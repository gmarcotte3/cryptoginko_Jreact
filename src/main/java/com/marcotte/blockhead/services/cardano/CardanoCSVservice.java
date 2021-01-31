package com.marcotte.blockhead.services.cardano;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.model.WalletTransaction;
import com.marcotte.blockhead.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * Cardano CSV service.
 * this handles the csv formats as used by the Cardano daedalus wallet as of 2021-01-30
 */
@Service
public class CardanoCSVservice {
    private static final Logger log = LoggerFactory.getLogger(com.marcotte.blockhead.wallets.exodus.ExodosCsvService.class);

    private static final int TXID_COL = 0;          // transaction ID
    private static final int TYPE_COL = 1;          // "Received" or "Sent"
    private static final int COIN_AMOUNT_COL = 2;   // amount of ADA always a positive number.
    private static final int DATE_COL = 3;          // date time stamp in the form: 2021-01-06T182909.0000Z
    private static final int STATUS_COL = 4;        // should be "Confirmed"
    private static final int ADDRESS_FROM_COL = 5;  // list of addresses
    private static final int ADDRESS_TO = 6;        // list of addresses

    private static String DATE_PATTERN = "yyyy-MM-dd'T'HHmmss.SSSS'Z'";


    /**
     * parse the csv transaction from the daedalus wallet and return transaction list
     *
     * This routine will parse the csv file input and create a transaction list. Daedalus wallet csv transactions
     * in reverse chronological order ( latest transaction first) but we want to process the transaction in the
     * chronological order so it easer to understand from reporting poin of view. (the reverse chron works if you
     * are using it as a wallet).
     *
     * So this routine will sort the transaction list chronological be for returning the array. This also makes it
     * consistant with all the other parseTransactionCsv implementations we do in this application.
     *
     * @param csvFileArray
     * @return
     */
    public List<WalletTransaction> parseTransactionCsv(List<List<String>> csvFileArray) {
        List<WalletTransaction> walletTransactions = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp rightNow = new Timestamp(now.getTime());
        Double transactionType = 1.0;

        if (csvFileArray == null || csvFileArray.size() == 0) {
            return walletTransactions;
        }

        // parse the transactions skip the first line that is the header.
        for (int j = 1; j < csvFileArray.size(); j++) {
            List<String> row = csvFileArray.get(j);
            WalletTransaction walletTransaction = new WalletTransaction();
            walletTransaction.setTransactionID( row.get(TXID_COL));

            // transaction type send or receive
            if ( row.get(TYPE_COL).compareToIgnoreCase("Sent") == 0 ) {
                transactionType = -1.0;
            } else if ( row.get(TYPE_COL).compareToIgnoreCase("Received") == 0 ) {
                transactionType = 1.0;
            } else {
                // TODO is there anything besides send|received for this field?
                transactionType = 1.0;
                log.error("unkown transaction type of " + row.get(TYPE_COL) + " assuming amount is positive");
            }

            // transaction amount
            Double coinAmount = amountStringToDouble(row.get(COIN_AMOUNT_COL));
            walletTransaction.setCoinAmount(coinAmount * transactionType);

            // transaction date
            String dateStr = row.get(DATE_COL);
            Timestamp transactionDate = rightNow;
            try {
                transactionDate = cardanoDateToTimeStamp(dateStr);
            } catch ( Exception e) {
                log.error("date format parse error [" + dateStr + "]" + e.getMessage());
            }
            walletTransaction.setTransactionTimestamp(transactionDate);

            walletTransactions.add(walletTransaction);
        }
        return sortTransactionByDate(walletTransactions);  // return in the chronological
    }

    /**
     * convert the date string as formated in cardano daedalus wallet into a proper timestamp.
     * @param dateStr
     * @return
     */
     private Timestamp cardanoDateToTimeStamp( String dateStr ) {
        Timestamp transactionDate = Utils.dateConvert(dateStr, DATE_PATTERN );
        return transactionDate;
    }


    /**
     * converts a string formated number into a double.
     * The input string is the form #,###.#### XXX where there is a space following
     * the number and a coin currency code. The currency code is stripped out before
     * normal string to double conversion is done.
     *
     * @param AmountStr    number string of the form "#,###.#### XXX"
     * @return
     */
    private Double amountStringToDouble(String AmountStr) {
        String cleanString = AmountStr.trim();
        Double cleanAmount = 0.0;
        if ( cleanString == null || cleanString.length() == 0) {
            return 0.0;
        }
        cleanString = cleanString.substring(0, cleanString.indexOf(' '));
        cleanString = cleanString.replaceAll(",", "");

        try {
            cleanAmount = Double.valueOf(cleanString);
        } catch ( Exception e) {
            log.error("failed to convert amount input=>" + AmountStr + "<=,  assuming 0.0" );

            cleanAmount = 0.0;
        }
        return cleanAmount;
    }

    /**
     * sort transactions by date returning the array in chronological order
     *
     * @param transactions
     * @return
     */
    public List<WalletTransaction> sortTransactionByDate(List<WalletTransaction> transactions)
    {
        transactions.sort( new Comparator<WalletTransaction>() {
            @Override
            public int compare(WalletTransaction lhs, WalletTransaction rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getTransactionTimestamp().compareTo(rhs.getTransactionTimestamp());
            }
        });
        return transactions;
    }

}
