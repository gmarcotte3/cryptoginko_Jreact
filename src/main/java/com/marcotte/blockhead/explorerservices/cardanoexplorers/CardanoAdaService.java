package com.marcotte.blockhead.explorerservices.cardanoexplorers;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.BlockchainTransactionList;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


@Service
public class CardanoAdaService
{
    private static final Logger log = LoggerFactory.getLogger(CardanoAdaService.class);
    private final String CARDANO_ADA_SERVICE = "cardanoAda";
    private final double satoshiDecimals = 1E6;

    public String getServiceName() { return CARDANO_ADA_SERVICE;}
    public String getServiceNameLong() { return CARDANO_ADA_SERVICE + "(cardanoexplorer.com)";}


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

    public boolean addressInfo(BlockchainAddressStore blockchainAddressStore)
    {
        boolean result = false;
        String address = blockchainAddressStore.getAddress();
        String theRawJsonQuote;
        Timestamp lastupdated;

        String message = "";
        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);


        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        String url = "https://cardanoexplorer.com/api/addresses/summary/"
                + address;
        log.info("url =" + url);


        try
        {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity, String.class);
            theRawJsonQuote = response.getBody();
            log.info(CARDANO_ADA_SERVICE + "/addrs=" + theRawJsonQuote);

            JSONObject obj = new JSONObject(theRawJsonQuote);
            JSONObject rightObj = obj.getJSONObject("Right");
            JSONObject caBalanceObj = rightObj.getJSONObject("caBalance");

            long satoshi_balance = caBalanceObj.getLong("getCoin");
            double coin_balance = (double) satoshi_balance / satoshiDecimals;
            lastupdated = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            blockchainAddressStore.setLastBalance(coin_balance);
            blockchainAddressStore.setMessage("Balance update " + lastupdated.toString() + " via " + CARDANO_ADA_SERVICE);
            blockchainAddressStore.setLastUpdated(lastupdated);

            log.info(blockchainAddressStore.getMessage());

            result = true;
        } catch ( Exception e )
        {
            Timestamp errorTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = CARDANO_ADA_SERVICE + " failure at " + errorTime.toString()  + " error=" + e.getMessage();
            blockchainAddressStore.setMessage(message);

            result = false;
            log.error(message);
        }

        return result;
    }

    public boolean addressTransactions(BlockchainTransactionList transactionList)
    {
        return false;
    }
}

