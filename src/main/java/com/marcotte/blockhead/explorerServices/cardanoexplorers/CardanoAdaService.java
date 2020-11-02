package com.marcotte.blockhead.explorerServices.cardanoexplorers;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.model.BlockchainTransactionList;
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

    /**
     * block space explorer for ADA example:
     * https://explorer.cardano.org/api/addresses/summary/DdzFFzCqrhsq57YDsPfWEPYwg84vQek2V12LTPeEcXFj3TFSpXp9Gd6939qiKVTdPp1eY2bzmARwgD4d2WGainVev2s6AMN1rhkTWxso
     *
     * {
     *   "Right": {
     *     "caAddress": "DdzFFzCqrhsq57YDsPfWEPYwg84vQek2V12LTPeEcXFj3TFSpXp9Gd6939qiKVTdPp1eY2bzmARwgD4d2WGainVev2s6AMN1rhkTWxso",
     *     "caType": "CPubKeyAddress",
     *     "caTxNum": 1,
     *     "caBalance": {
     *       "getCoin": "176083918906"
     *     },
     *     "caTotalInput": {
     *       "getCoin": "0"
     *     },
     *     "caTotalOutput": {
     *       "getCoin": "176083918906"
     *     },
     *     "caTotalFee": {
     *       "getCoin": "205261"
     *     },
     *     "caTxList": [
     *       {
     *         "ctbId": "c2cebffbbab8bb5e952d75effb50162fe11bd5008001e27c839a124dc37e2c82",
     *         "ctbTimeIssued": 1588090271,
     *         "ctbInputs": [
     *           [
     *             "DdzFFzCqrhstmqBkaU98vdHu6PdqjqotmgudToWYEeRmQKDrn4cAgGv9EZKtu1DevLrMA1pdVazufUCK4zhFkUcQZ5Gm88mVHnrwmXvT",
     *             {
     *               "getCoin": "181083936837"
     *             }
     *           ],
     *           [
     *             "DdzFFzCqrht3zmEm7PgeLz8YEhWfRG7MjhPrn2mubekWPHgSu69ZAhgrP7BKAu9HzW4giwYz7MTMcCYETfd4k8Vrts2mj2bRPLeMViAx",
     *             {
     *               "getCoin": "1888064108973"
     *             }
     *           ],
     *           [
     *             "DdzFFzCqrht3UjrJYLcL2SA4vR2yXLpTrvjLhm3dUNcgKhAHZTGEXncy9558EdbN3BPghqZdxYEvjubEgg4SSph3Qas8sbESVMXsdRA6",
     *             {
     *               "getCoin": "144251098882"
     *             }
     *           ],
     *           [
     *             "DdzFFzCqrhstmqBkaU98vdHu6PdqjqotmgudToWYEeRmQKDrn4cAgGv9EZKtu1DevLrMA1pdVazufUCK4zhFkUcQZ5Gm88mVHnrwmXvT",
     *             {
     *               "getCoin": "57275155573"
     *             }
     *           ]
     *         ],
     *         "ctbOutputs": [
     *           [
     *             "DdzFFzCqrhsq57YDsPfWEPYwg84vQek2V12LTPeEcXFj3TFSpXp9Gd6939qiKVTdPp1eY2bzmARwgD4d2WGainVev2s6AMN1rhkTWxso",
     *             {
     *               "getCoin": "176083918906"
     *             }
     *           ],
     *           [
     *             "DdzFFzCqrht72tyhQoXWjkALaBysgAKxrVtUZ3nGmUUL1PgG1FijJA3ThgLfg656zG6xnDBoB3CCDEaztSinMpCekJ4TY8B3vEUmHB3L",
     *             {
     *               "getCoin": "249999000000"
     *             }
     *           ],
     *           [
     *             "DdzFFzCqrhtAvgqaqR9YbAJP9ZnRSKpRcuDJypk2DpvNHZVg34vLVLZKh9V3phpFLfgB1qcWrEZ54LgKcrjAbf83WUNkcqb9nrUf6jhP",
     *             {
     *               "getCoin": "5000000000"
     *             }
     *           ],
     *           [
     *             "DdzFFzCqrhst7ogNn5sbMy4KsVXRBHjioq9nKvtBKHoVMosXx2q7KqKfdhxe5c7HWUSCSiYwdHgfS9ECvKhiNgXCq1JJLD5DQbhw9gTK",
     *             {
     *               "getCoin": "1839591176098"
     *             }
     *           ]
     *         ],
     *         "ctbInputSum": {
     *           "getCoin": "2270674300265"
     *         },
     *         "ctbOutputSum": {
     *           "getCoin": "2270674095004"
     *         },
     *         "ctsFees": {
     *           "getCoin": "205261"
     *         }
     *       }
     *     ]
     *   }
     * }
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
        ResponseEntity<String> response = null;
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
        //    message = CARDANO_ADA_SERVICE + " failure at " + errorTime.toString()  + " error=" + e.getMessage();
            message = String.format("%s failure at %s error=%s response code=%d",
                CARDANO_ADA_SERVICE , errorTime.toString(),  e.getMessage(),  response.getStatusCodeValue());
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

