package com.marcotte.blockhead.explorerServices.blockcypher;

import com.marcotte.blockhead.config.BlockheadConfig;
import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.model.CryptoNames;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * This service supports BTC, BCH,  ETH and ?
 *
 * this service has a stuppidly low limit for requests
 */
@Service
public class BlockCypherComService
{
    private static final Logger log = LoggerFactory.getLogger(BlockCypherComService.class);
    public  final String BLOCKCYPHER_SERVICE = "blockCypherCom";


    //TODO use the blockcyper api key
    @Autowired
    private BlockheadConfig blockheadConfig;

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
     * API docks at blockcypher:
     * https://www.blockcypher.com/dev/bitcoin/
     * https://www.blockcypher.com/dev/bitcoin/#address-api
     *
     * curl https://api.blockcypher.com/v1/btc/main/addrs/1DEP8i3QJCsomS4BSMY2RpU1upv62aGvhD/balance
     *
     * {
     * "address": "1DEP8i3QJCsomS4BSMY2RpU1upv62aGvhD",
     * "total_received": 4433416,
     * "total_sent": 0,
     * "balance": 4433416,
     * "unconfirmed_balance": 0,
     * "final_balance": 4433416,
     * "n_tx": 7,
     * "unconfirmed_n_tx": 0,
     * "final_n_tx": 7
     * }
     *
     * API key not need for GET
     *
     */
    public boolean addressInfo(BlockchainAddressStore blockchainAddressStore)
    {
        boolean result = false;
        String crypto = blockchainAddressStore.getCurrency();
        String address = blockchainAddressStore.getAddress();
        String theRawJsonQuote;
        Timestamp lastupdated;

        String message = "";
        long satoshi_final_balance;
        long satoshi_balance;
        double calculated_coin_balance;
        double calculated_coin_final_blance;
        long total_spent;
        long total_received;
        double coin_total_spent;
        double coin_total_received;

        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);


        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        String url = "https://api.blockcypher.com/v1/"
                + crypto.toLowerCase() + "/main/addrs/"
                + address
                + "/balance";
        log.info("url =" + url);

        try
        {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity, String.class);
            theRawJsonQuote = response.getBody();
            log.debug(BLOCKCYPHER_SERVICE + "/addrs=" + theRawJsonQuote);



            JSONObject obj = new JSONObject(theRawJsonQuote);
            satoshi_final_balance = obj.getLong("final_balance");
            satoshi_balance = obj.getLong("balance");

            double satoshiDecimals = getSatochiDecmalsForCrypto(crypto);
            calculated_coin_balance = (double) satoshi_balance / satoshiDecimals;
            calculated_coin_final_blance = (double) satoshi_final_balance / satoshiDecimals;

            total_spent = obj.getLong("total_sent");
            total_received = obj.getLong("total_received");
            coin_total_spent = (double) total_spent / satoshiDecimals;
            coin_total_received = (double) total_received  / satoshiDecimals;

            int no_transactions = obj.getInt("n_tx");
            log.info("satoshi_balance=" + satoshi_balance
                    + " coin_balance=" + calculated_coin_balance
                    + " satoshi_final_balance=" + calculated_coin_final_blance
                    + " coin_total_spent=" + coin_total_spent
                    + " coin_total_received=" + coin_total_received
                    + " No Transactions=" + no_transactions
            );

            lastupdated = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = "Balance update " + lastupdated.toString() + " via " + BLOCKCYPHER_SERVICE;

            blockchainAddressStore.setLastBalance(calculated_coin_balance);
            blockchainAddressStore.setNumTransactions(no_transactions);
            blockchainAddressStore.setMessage(message);
            blockchainAddressStore.setLastUpdated(lastupdated);

            log.info(blockchainAddressStore.getMessage());
            result = true;
        } catch ( Exception e )
        {
            Timestamp errorTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = BLOCKCYPHER_SERVICE + " failure at " + errorTime.toString()  + " error=" + e.getMessage();
            blockchainAddressStore.setMessage(message);

            result = false;
            log.error(message);
        }

        return result;
    }

    /**
     * this service handles more than one currency but they may have different number of satoshi decimal places for
     * each coin. so we have to set the decimal by the coin name.
     *
     * @param crypto
     * @return
     */
    private double getSatochiDecmalsForCrypto(String crypto)
    {
        double satoshiDecimals;
        CryptoNames cryptoNames;

        if ( crypto.toUpperCase().equals(CryptoNames.ETHEREUM))
        {
            // Etherioum uses a finer number of satoshi for each coin
            satoshiDecimals = 1E18;
        } else {
            // Bitcoin, Bitcoin-cash, Dash and litecoin all use this one
            satoshiDecimals = 1E8;
        }

        return satoshiDecimals;
    }

}
