package com.marcotte.blockhead.explorerServices.eosexplorers;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.model.BlockchainTransactionList;
import com.marcotte.blockhead.util.ShellScript;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


@Service
public class Eos_eosnewyork_io_Service
{
    private static final Logger log = LoggerFactory.getLogger(Eos_eosnewyork_io_Service.class);

    private final String EOSNEWYORK_IO_SERVICE = "Eosnewyork_io_Service";

    public String getServiceName() { return EOSNEWYORK_IO_SERVICE; }
    public String getServiceNameLong() { return EOSNEWYORK_IO_SERVICE + " (api.eosnewyork.io)"; }

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
     * curl --request POST \
     *   --url https://api.eosnewyork.io/v1/chain/get_account \
     *   --data '{"account_name":"YOUR_ACCOUNT"}'
     *
     * {
     *   "account_name": "YOUR_ACCOUNT",
     *   "head_block_num": 25689087,
     *   "head_block_time": "2018-11-07T11:59:19.000",
     *   "privileged": false,
     *   "last_code_update": "1970-01-01T00:00:00.000",
     *   "created": "2018-11-07T08:02:05.500",
     *   "core_liquid_balance": "3.6588 EOS",
     *   "ram_quota": 5474,
     *   "net_weight": 400,service
     *   "cpu_weight": 19600,
     *   "net_limit": {
     *     "used": 0,
     *     "available": 27987,
     *     "max": 27987
     *   },
     *   "cpu_limit": {
     *     "used": 0,
     *     "available": 2749,
     *     "max": 2749
     *   },
     *   "ram_usage": 3446,
     *   "permissions": [
     *     {
     *       "perm_name": "active",
     *       "parent": "owner",
     *       "required_auth": {
     *         "threshold": 1,
     *         "keys": [
     *           {
     *             "key": "EOS5utwo2ZEPMhkFjkdQ7AX98be6jkfebNP8b4hLVkjonSzCRCBTU",
     *             "weight": 1
     *           }
     *         ],
     *         "accounts": [
     *
     *         ],
     *         "waits": [
     *
     *         ]
     *       }
     *     },
     *     {
     *       "perm_name": "owner",
     *       "parent": "",
     *       "required_auth": {
     *         "threshold": 1,
     *         "keys": [
     *           {
     *             "key": "EOS5utwo2ZEPMhkFjkdQ7AX98be6jkfebNP8b4hLVkjonSzCRCBTU",
     *             "weight": 1
     *           }
     *         ],
     *         "accounts": [
     *
     *         ],
     *         "waits": [
     *
     *         ]
     *       }
     *     }
     *   ],
     *   "total_resources": {
     *     "owner": "qrvw1yycaubk",
     *     "net_weight": "0.0400 EOS",
     *     "cpu_weight": "1.9600 EOS",
     *     "ram_bytes": 4074
     *   },
     *   "self_delegated_bandwidth": {
     *     "from": "qrvw1yycaubk",
     *     "to": "qrvw1yycaubk",
     *     "net_weight": "0.0400 EOS",
     *     "cpu_weight": "1.9600 EOS"
     *   },
     *   "refund_request": null,
     *   "voter_info": {
     *     "owner": "qrvw1yycaubk",
     *     "proxy": "",
     *     "producers": [
     *
     *     ],
     *     "staked": 20000,
     *     "last_vote_weight": "0.00000000000000000",
     *     "proxied_vote_weight": "0.00000000000000000",
     *     "is_proxy": 0
     *   }
     * }
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

        double balance_calculated;

        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");


        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("account_name", address );

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);


        try {
            // TODO convert to proper RestCall when reliable service is found
            // hack starts here ---
            String[] command = new String[7];
            command[0] = "curl";
            command[1] = "--request";
            command[2] = "POST";
            command[3] = "--url";
            command[4] = "https://api.eosnewyork.io/v1/chain/get_account";
            command[5] = "--data";
            command[6] = "{\"account_name\":\"" + address + "\"}";
            theRawJsonQuote = ShellScript.executeCommand(command);
            // hack ends here ----

            log.info(EOSNEWYORK_IO_SERVICE + "/addrs=" + theRawJsonQuote);
            lastupdated = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            JSONObject obj = new JSONObject(theRawJsonQuote);

            // get the available balance (does not include the staked value)
            String core_liquid_balance = obj.getString("core_liquid_balance");
            String core_liquid_balance_clean = core_liquid_balance.substring(0, core_liquid_balance.indexOf(" EOS"));
            balance_calculated = Double.valueOf(core_liquid_balance_clean);

            // TODO total_resources - get the net and cpu weight here for the staking amount

            // save results back to database
            blockchainAddressStore.setLastBalance(balance_calculated);
            blockchainAddressStore.setMessage("Balance update " + lastupdated.toString() + " via " + EOSNEWYORK_IO_SERVICE);
            blockchainAddressStore.setLastUpdated(lastupdated);

            log.info(blockchainAddressStore.getMessage());
            result = true;

        } catch ( Exception e) {
            Timestamp errorTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            message = EOSNEWYORK_IO_SERVICE + " failure at " + errorTime.toString()  + " error=" + e.getMessage();
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

