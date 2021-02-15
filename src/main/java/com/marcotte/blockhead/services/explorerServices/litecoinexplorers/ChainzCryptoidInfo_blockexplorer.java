package com.marcotte.blockhead.services.explorerServices.litecoinexplorers;

import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.model.blockchaintransaction.BlockchainTransactionList;
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
public class ChainzCryptoidInfo_blockexplorer
{
    private static final Logger log = LoggerFactory.getLogger(ChainzCryptoidInfo_blockexplorer.class);
    private static final String CHAIN_NZ_CRYPTOID_INFO = "chainnzCryptoidInfo";
    public String getServiceName() { return CHAIN_NZ_CRYPTOID_INFO;}
    public String getServiceNameLong() { return CHAIN_NZ_CRYPTOID_INFO + " (chainz.cryptoid.info/ltc)";}

    private final double satoshiDecimals = 1E8;


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
        double final_balance;
        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String url = "https://chainz.cryptoid.info/ltc/api.dws?q=getbalance&a="
                + address;
        log.info("url =" + url);


        try
        {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity, String.class);
            theRawJsonQuote = response.getBody();
            log.debug(CHAIN_NZ_CRYPTOID_INFO + "/addrs=" + theRawJsonQuote);

            lastupdated = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            double balance = Double.valueOf(theRawJsonQuote);
            final_balance = balance * satoshiDecimals;
            blockchainAddressStore.setLastBalance(balance);
            blockchainAddressStore.setMessage("Balance update " + lastupdated.toString() + " via chainz.cryptoid.info Service");
            blockchainAddressStore.setLastUpdated(lastupdated);
            log.info(blockchainAddressStore.getMessage());
            result = true;
        } catch ( Exception e )
        {
            Timestamp errorTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = "BlockCypherComService failure at " + errorTime.toString()  + " error=" + e.getMessage();
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
