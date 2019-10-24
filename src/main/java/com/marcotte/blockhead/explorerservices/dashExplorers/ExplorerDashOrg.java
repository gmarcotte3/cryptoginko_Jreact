package com.marcotte.blockhead.explorerservices.dashExplorers;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.BlockchainTransactionList;
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
public class ExplorerDashOrg
{
    private static final Logger log = LoggerFactory.getLogger(ExplorerDashOrg.class);

    private final String EXPLORER_DASH_ORG_SERVICE = "explorer_dash_org_Service";

    public String getServiceName() { return EXPLORER_DASH_ORG_SERVICE; }
    public String getServiceNameLong() { return EXPLORER_DASH_ORG_SERVICE + " (explorer.dash.org)"; }

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
     *
     * https://explorer.dash.org/q/addressbalance
     * returns amount of money at the given address
     * /chain/CHAIN/q/addressbalance/ADDRESS
     * example
     * https://explorer.dash.org/chain/Dash/q/addressbalance/XnhJdVxxxxxxxxxxxxxxxxxxxxxxxxxx4
     * results
     *
     * 1.9
     *
     * it is a simple text number not json return
     *
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
        double coin_balance;

        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try {
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String url = "https://explorer.dash.org/chain/Dash/q/addressbalance/"
                    + address;

            log.info("url =" + url);

            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity, String.class);
            log.debug("api.blockcypher resonse=" + response.toString());

            theRawJsonQuote = response.getBody();
            lastupdated = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            coin_balance = Double.valueOf(theRawJsonQuote);
            blockchainAddressStore.setLastBalance(coin_balance);
            blockchainAddressStore.setMessage("Balance update " + lastupdated.toString() + " via explorer.dash.org");
            blockchainAddressStore.setLastUpdated(lastupdated);

            log.info(blockchainAddressStore.getMessage());
            result = true;

        } catch (NumberFormatException e) {
            Timestamp errorTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = "explorer.dash.org failure at " + errorTime.toString()  + " error=" + e.getMessage();
            blockchainAddressStore.setMessage(message);

            result = false;
            log.error(message);

        } catch ( Exception e) {
            Timestamp errorTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = "explorer.dash.org failure at " + errorTime.toString()  + " error=" + e.getMessage();
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
