package com.marcotte.blockhead.explorerServices.etheriumexplorers;

import com.marcotte.blockhead.config.BlockheadConfig;
import com.marcotte.blockhead.datastore.BlockchainAddressStore;
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


@Service
public class EtherscanExplorerService
{
    private static final Logger log = LoggerFactory.getLogger(EtherscanExplorerService.class);
    public final double satoshiDecimals = 1E18;
    public final String ETHERSCAN_BLOCK_CHAIN_EXPLORER = "etherscanBlockChainExplorer";


    @Autowired
    private BlockheadConfig blocktestConfig;


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
     *  gets a single address from etherscan
     *
     *  Api docs: https://etherscan.io/apis
     *
     *  get a single address url
     *  https://api.etherscan.io/api?module=account&action=balance&address=0xddbdxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx40121a&tag=latest&apikey=YourApiKeyToken
     *
     *  https://api.etherscan.io/api?module=account&action=balance&address=0x0E73B3f0521743d10ab56759B71Fd1678067B935&tag=latest&apikey=QZ48YHB58ENDP34CQGI4FMYJUGEBD752HD
     *
     *  results:
     *  {
     *    status: "1",
     *    message: "OK",
     *    result: "10xxxxxxxx747991234",
     *  }
     *
     * @param blockchainAddressStore
     * @return
     */
    public boolean addressInfo(BlockchainAddressStore blockchainAddressStore)
    {
        boolean result = false;
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

        String url = "https://api.etherscan.io/api?module=account&action=balance&address="
                + address
                + "&tag=latest&apikey="
                + blocktestConfig.getEtherscanApiToken();
        log.info("url =" + url);

        try
        {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity, String.class);
            theRawJsonQuote = response.getBody();
            log.debug("api.blockcypher/addrs=" + theRawJsonQuote);


            JSONObject obj = new JSONObject(theRawJsonQuote);
            final_balance = obj.getDouble("result");

            lastupdated = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = "Balance update " + lastupdated.toString() + " via " + ETHERSCAN_BLOCK_CHAIN_EXPLORER;

            double balance = ((double) final_balance) / satoshiDecimals;
            blockchainAddressStore.setLastBalance(balance);
            blockchainAddressStore.setMessage(message);
            blockchainAddressStore.setLastUpdated(lastupdated);

            log.info(blockchainAddressStore.getMessage());
            result = true;
        } catch ( Exception e )
        {
            Timestamp errorTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = ETHERSCAN_BLOCK_CHAIN_EXPLORER + " failure at " + errorTime.toString()  + " error=" + e.getMessage();
            blockchainAddressStore.setMessage(message);

            result = false;
            log.error(message);
        }

        return result;
    }
}