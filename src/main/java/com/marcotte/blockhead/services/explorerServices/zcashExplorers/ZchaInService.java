package com.marcotte.blockhead.services.explorerServices.zcashExplorers;

import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.model.blockchaintransaction.BlockchainTransactionList;
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
public class ZchaInService
{
    private static final Logger log = LoggerFactory.getLogger(ZchaInService.class);
    private static final String ZCHAN_IN_SEVICE = "zchaInService";

    public String getServiceName() { return ZCHAN_IN_SEVICE;}
    public String getServiceNameLong() { return ZCHAN_IN_SEVICE + " (api.zcha.in)"; }

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
        String crypto = blockchainAddressStore.getTicker();
        String address = blockchainAddressStore.getAddress();
        String theRawJsonQuote;
        Timestamp lastupdated;

        String message = "";
        double coin_balance;
        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try
        {
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String url = "https://api.zcha.in/v2/mainnet/accounts/"
                    + address;
            log.info("url =" + url);

            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity, String.class);
            log.debug("ZchaInService resonse=" + response.toString());

            theRawJsonQuote = response.getBody();
            log.debug("ZchaInService response.body=" + theRawJsonQuote);

            JSONObject obj = new JSONObject(theRawJsonQuote);
            coin_balance = obj.getDouble("balance");
            blockchainAddressStore.setLastBalance(coin_balance);

            int sentCount = obj.getInt("sentCount");
            int recvCount = obj.getInt("recvCount");
            int transactionCount = sentCount + recvCount;
            blockchainAddressStore.setNumTransactions(transactionCount);

            lastupdated = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            blockchainAddressStore.setMessage("Balance update " + lastupdated.toString() + " via " + ZCHAN_IN_SEVICE);
            blockchainAddressStore.setLastUpdated(lastupdated);

            log.info(blockchainAddressStore.getMessage());
            result = true;
        } catch ( Exception e )
        {
            Timestamp errorTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = ZCHAN_IN_SEVICE + " failure at " + errorTime.toString()  + " error=" + e.getMessage();
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
