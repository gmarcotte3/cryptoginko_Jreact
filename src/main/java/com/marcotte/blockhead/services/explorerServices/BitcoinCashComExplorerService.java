package com.marcotte.blockhead.services.explorerServices;


import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.model.blockchaintransaction.BlockchainTransaction;
import com.marcotte.blockhead.model.blockchaintransaction.BlockchainTransactionList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


@Service
public class BitcoinCashComExplorerService
{
    private static final Logger log = LoggerFactory.getLogger(BitcoinCashComExplorerService.class);
    private final double satoshiDecimals = 1E8;
    private final String BITCOIN_CON_EXLORER_SERVICE = "BitcoinComExplorer";
    public String getServiceName() { return BITCOIN_CON_EXLORER_SERVICE;}
    public String getServiceNameLong() { return BITCOIN_CON_EXLORER_SERVICE + " (rest.bitcoin.com)";}


    public boolean addressInfo( List<BlockchainAddressStore> blockchainAddressStores)
    {
        boolean result = false;
        for (BlockchainAddressStore addressStore : blockchainAddressStores)
        {
            result = addressInfo(addressStore);
            addressStore.setUpdatedViaBlockChainExplorer(result);
        }
        return result;
    }

    /**
     * extract address info from rest.bitcoin.com rest service
     *
     * curl -X GET "https://rest.bitcoin.com/v2/address/details/bitcoincash:qzs02v05l7qs5s24srqju498qu55dwuj0cx5ehjm2c" -H "accept: application/json"
     *
     * // returns -------
     * {
     *   "addrStr": "1Fg4r9iDrEkCcDmHTy2T79EusNfhyQpu7W",
     *   "balance": 0.01,
     *   "balanceSat": 1000000,
     *   "totalReceived": 0.05185868,
     *   "totalReceivedSat": 5185868,
     *   "totalSent": 0.04185868,
     *   "totalSentSat": 4185868,
     *   "unconfirmedBalance": 0,
     *   "unconfirmedBalanceSat": 0,
     *   "unconfirmedTxAppearances": 0,
     *   "txAppearances": 36,
     *   "transactions": [
     *     "ac444896b3e32d17824fa6573eed3b89768c5c9085b7a71f3ba88e9d5ba67355",
     *     "a5f972572ee1753e2fd2457dd61ce5f40fa2f8a30173d417e49feef7542c96a1",
     *     "81039b1d7b855b133f359f9dc65f776bd105650153a941675fedc504228ddbd3"
     *   ],
     *   "legacyAddress": "1Fg4r9iDrEkCcDmHTy2T79EusNfhyQpu7W",
     *   "cashAddress": "bitcoincash:qzs02v05l7qs5s24srqju498qu55dwuj0cx5ehjm2c",
     *   "currentPage": 0,
     *   "pagesTotal": 1
     * }
     *
     * Get address information
     * @param blockchainAddressStore
     * @param satoshiDecimals
     * @return
     */
    public boolean addressInfo(BlockchainAddressStore blockchainAddressStore)
    {
        boolean result = false;
        String address = blockchainAddressStore.getAddress();
        String theRawJsonQuote;
        Timestamp lastupdated;
        String message = "";

        try
        {
            theRawJsonQuote = rawAddressInfo(address);

            JSONObject obj = new JSONObject(theRawJsonQuote);
            double coin_balance = obj.getDouble("balance");


            lastupdated = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = "Balance update " + lastupdated.toString() + " via " + BITCOIN_CON_EXLORER_SERVICE;
            blockchainAddressStore.setLastBalance(coin_balance);
            blockchainAddressStore.setMessage(message);
            blockchainAddressStore.setLastUpdated(lastupdated);

            log.info(blockchainAddressStore.getMessage());
            result = true;
        } catch ( Exception e )
        {
            Timestamp errorTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = BITCOIN_CON_EXLORER_SERVICE + " failure at " + errorTime.toString()  + " error=" + e.getMessage();
            blockchainAddressStore.setMessage(message);

            result = false;
            log.error(message);
        }
        return result;
    }

    private String rawAddressInfo(String address) throws Exception
    {
        String theRawJsonQuote;
        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String url = "https://rest.bitcoin.com/v2/address/details/" + address;
        log.info("url =" + url);


        response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity, String.class);
        theRawJsonQuote = response.getBody();
        log.debug( BITCOIN_CON_EXLORER_SERVICE + "/addrs=" + theRawJsonQuote);

        return theRawJsonQuote;

    }

    /**
     * curl -X GET "https://rest.bitcoin.com/v2/address/utxo/bitcoincash:qzs02v05l7qs5s24srqju498qu55dwuj0cx5ehjm2c" -H "accept: application/json"
     *
     * // returns
     * {
     *   "utxos": [
     *     {
     *       "txid": "eea57285462dd70dadcd431fc814857b3f81fe4d0a059a8c02c12fd7d33c02d1",
     *       "vout": 0,
     *       "amount": 0.01,
     *       "satoshis": 1000000,
     *       "height": 566900,
     *       "confirmations": 760
     *     },
     *     {
     *       "txid": "282b3b296b6aed7122586ed69f7a57d35584eaf94a4d1b1ad7d1b05d36cb79d1",
     *       "vout": 0,
     *       "amount": 0.01,
     *       "satoshis": 1000000,
     *       "height": 563858,
     *       "confirmations": 3802
     *     },
     *     {
     *       "txid": "ac444896b3e32d17824fa6573eed3b89768c5c9085b7a71f3ba88e9d5ba67355",
     *       "vout": 13,
     *       "amount": 0.01,
     *       "satoshis": 1000000,
     *       "height": 558992,
     *       "confirmations": 8668
     *     }
     *   ],
     *   "legacyAddress": "1Fg4r9iDrEkCcDmHTy2T79EusNfhyQpu7W",
     *   "cashAddress": "bitcoincash:qzs02v05l7qs5s24srqju498qu55dwuj0cx5ehjm2c",
     *   "scriptPubKey": "76a914a0f531f4ff810a415580c12e54a7072946bb927e88ac"
     * }
     */
    public boolean addressTransactions(BlockchainTransactionList transactionList)
    {
        JSONObject obj;
        JSONArray transactionsObj;
        String address = transactionList.getAddressStore().getAddress();
        List<BlockchainTransaction> transactions = new ArrayList<BlockchainTransaction>();

        String theRawTransaction;
        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String url = "https://rest.bitcoin.com/v2/address/utxo/" + transactionList.getAddressStore().getAddress();
        log.info("url =" + url);

        response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity, String.class);
        theRawTransaction = response.getBody();
        log.debug( BITCOIN_CON_EXLORER_SERVICE + "/transaction=" + theRawTransaction);

        try {
            obj = new JSONObject(theRawTransaction);
            transactionsObj = obj.getJSONArray("utxos");
            if (transactionsObj != null) {
                for (int j = 0; j < transactionsObj.length(); j++) {
                    JSONObject trnObj = transactionsObj.getJSONObject(j);
                    BlockchainTransaction transaction = new BlockchainTransaction()
                            .setTransactionID(trnObj.getString("txid"))
                            .setTransactionAmount((float) trnObj.getDouble("amount"));
                    transactions.add(transaction);
                }
                transactionList.setTransactionList(transactions);
            }
            transactionList.setTransactionsFound(true);
        } catch ( Exception e) {
            log.error("errors processing address transactions error={}", e.getMessage());
            transactionList.setTransactionsFound(false);
            return false;
        }
        return true;
    }
}


