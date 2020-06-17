package com.marcotte.blockhead.explorerServices.blockchair;

import com.marcotte.blockhead.config.BlockheadConfig;
import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.CryptoNames;
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
 * This service supports Rest apis from Blockchair.com
 *
 * API documentation:
 * https://blockchair.com/api/docs
 *
 * API key plans
 * https://api.blockchair.com/premium/plans
 *
 *  TODO support for api is needed.
 *
 * cardano example:
 * https://api.blockchair.com/cardano/raw/address/Ae2tdPwUPEYyVbtDKGrjyypbaJaRJowh5t4f2qKeNiZXQszY69pXWcaFKXq
 *
 * {
 *   "data": {
 *     "Ae2tdPwUPEYyVbtDKGrjyypbaJaRJowh5t4f2qKeNiZXQszY69pXWcaFKXq": {
 *       "address": {
 *         "caAddress": "Ae2tdPwUPEYyVbtDKGrjyypbaJaRJowh5t4f2qKeNiZXQszY69pXWcaFKXq",
 *         "caType": "CPubKeyAddress",
 *         "caChainTip": {
 *           "ctBlockNo": 4105925,
 *           "ctSlotNo": 4108030,
 *           "ctBlockHash": "2516ad9e63f68e225da23dd8a8030a478a360ec5b0df175aa4f4731fcfe9d88c"
 *         },
 *         "caTxNum": 1,
 *         "caBalance": {
 *           "getCoin": "5000000000"     <-- this is the one we want
 *         },
 *         "caTotalInput": {
 *           "getCoin": "5000000000"    <-- TODO
 *         },
 *         "caTotalOutput": {
 *           "getCoin": "0"             <-- TODO
 *         },
 *         "caTotalFee": {
 *           "getCoin": "200000"
 *         },
 *         "caTxList": [
 *           {
 *             "ctbId": "3e04456b7103eac1dec3b9cd8b33f8d04b74fcf14e324b410fb7e59f7a164546",
 *             "ctbTimeIssued": 1544604631,
 *             "ctbInputs": [
 *               {
 *                 "ctaAddress": "DdzFFzCqrht9BJjQK6tFTJnr7xvNTor6VcXe3Kumy1Hm4cKS5d7Fpts8A3Jxpg4jLiZbgVfUjKNRT9K74dBiKpG3RiecscFYXkFH12ir",
 *                 "ctaAmount": {
 *                   "getCoin": "1000000000"
 *                 },
 *                 "ctaTxHash": "5f2d3c2d848e40a4e15ff637496de6e62a05140e1cf624301b21346656b2b036",
 *                 "ctaTxIndex": 1
 *               },
 *     ...
 *             ],
 *             "ctbInputSum": {
 *               "getCoin": "7191537872"
 *             },
 *             "ctbOutputSum": {
 *               "getCoin": "7191337872"
 *             },
 *             "ctbFees": {
 *               "getCoin": "200000"
 *             }
 *           }
 *         ]
 *       }
 *     }
 *   },
 *   "context": {
 *     "code": 200,                        <-- response code
 *     "source": "D",
 *     "time": 1.649765968322754,
 *     "results": 1,
 *     "state": 4105924,
 *     "cache": {
 *       "live": true,
 *       "duration": 60,
 *       "since": "2020-05-01 20:08:24",
 *       "until": "2020-05-01 20:09:24",
 *       "time": null
 *     },
 *     "api": {
 *       "version": "2.0.49",                        <-- TODO
 *       "last_major_update": "2019-07-19 18:07:19",
 *       "next_major_update": null,
 *       "documentation": "https:\/\/blockchair.com\/api\/docs",
 *       "notice": "Beginning July 19th, 2019 all applications using Blockchair API on a constant basis should apply for an API key. Discover our plans here: https:\/\/api.blockchair.com\/premium"
 *     },
 *     "ftime": 1.6885461807250977,
 *     "rtime": 0.03878021240234375
 *   }
 * }
 */
@Service
public class BlockchairComExplorerService
{
  private static final Logger log = LoggerFactory.getLogger(BlockchairComExplorerService.class);
  public  final String BLOCKCHAIR_SERVICE = "blockchairCom";
  static public final String BLOCKCHAIR_ROOT_API = "https://api.blockchair.com";


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


  public boolean addressInfo(BlockchainAddressStore blockchainAddressStore)
  {
    boolean result = false;
    String crypto = blockchainAddressStore.getCurrency();
 //   String address = blockchainAddressStore.getAddress();
    String theRawJsonQuote;
    Timestamp lastupdated;

//    String message = "";
    long satoshi_balance;
    double calculated_coin_balance;
    long total_spent;
    long total_received;
    double coin_total_spent;
    double coin_total_received;

    ResponseEntity<String> response;
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//    headers.add("user-agent",
//        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);


    restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

    String url = createURL( blockchainAddressStore );
    log.info("url =" + url);

    if ( url == null || url.length() == 0)
    {
      blockchainAddressStore.setMessage(String.format("Crypto %s is not supported", crypto));
      log.error(blockchainAddressStore.getMessage());
      return false;
    }

    try
    {
      response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          entity, String.class);
      theRawJsonQuote = response.getBody();
      log.debug(BLOCKCHAIR_SERVICE + "/addrs=" + theRawJsonQuote);



      JSONObject obj = new JSONObject(theRawJsonQuote);
      JSONObject contextObj = obj.getJSONObject("context");
      int resultStatus = contextObj.getInt("code");
      if ( resultStatus == 200 )
      {
        JSONObject dataObj = obj.getJSONObject("data");
        JSONObject addrObj = dataObj.getJSONObject(blockchainAddressStore.getAddress());
        JSONObject addrDataObj = addrObj.getJSONObject("address");
        JSONObject caBalanceObj = addrDataObj.getJSONObject("caBalance");
        satoshi_balance  = caBalanceObj.getLong("getCoin");

        double satoshiDecimals = getSatochiDecmalsForCrypto(crypto);
        calculated_coin_balance = (double) satoshi_balance / satoshiDecimals;

        JSONObject caTotalOutputObj = addrDataObj.getJSONObject("caTotalOutput");
        total_spent = caTotalOutputObj.getLong("getCoin");

        JSONObject caTotalInputObj = addrDataObj.getJSONObject("caTotalOutput");
        total_received = caTotalInputObj.getLong("getCoin");

        coin_total_spent = (double) total_spent / satoshiDecimals;
        coin_total_received = (double) total_received / satoshiDecimals;

        int no_transactions = addrDataObj.getInt("caTxNum");

        log.info("satoshi_balance=" + satoshi_balance
            + " coin_balance=" + calculated_coin_balance
            + " satoshi_balance=" + satoshi_balance
            + " coin_total_spent=" + coin_total_spent
            + " coin_total_received=" + coin_total_received
            + " No Transactions=" + no_transactions
        );

        lastupdated = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

        blockchainAddressStore.setLastBalance(calculated_coin_balance);
        blockchainAddressStore.setNumTransactions(no_transactions);
        blockchainAddressStore.setMessage(String.format("Balance updated %s via %s",lastupdated.toString(), BLOCKCHAIR_SERVICE ));
        blockchainAddressStore.setLastUpdated(lastupdated);

        log.info(blockchainAddressStore.getMessage());
        result = true;
      } else {
        blockchainAddressStore.setMessage("Failed context=" + contextObj.toString());
        log.error(blockchainAddressStore.getMessage());
        result = false;
      }
    } catch ( Exception e )
    {
      Timestamp errorTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
      blockchainAddressStore.setMessage(String.format("%s failure at %s error=%s", BLOCKCHAIR_SERVICE, errorTime.toString(), e.getMessage() ));

      result = false;
      log.error(blockchainAddressStore.getAddress());
    }

    return result;
  }

  /**
   * convert the currency and address into a url
   *
   * {:btc_chain} can be one of these: bitcoin, bitcoin-cash, litecoin, bitcoin-sv, dogecoin, dash, groestlcoin, zcash, or bitcoin/testnet
   * {:eth_chain} can be only ethereum
   * {:xrp_chain} can be only ripple
   * {:xlm_chain} can be only stellar
   * {:ton_chain} can be only ton/testnet
   * {:xmr_chain} can be only monero
   * {:ada_chain} can be only cardano
   * {:xin_chain} can be only mixin
   * @param blockchainAddressStore
   * @return
   */
  private String createURL( BlockchainAddressStore blockchainAddressStore)
  {
    String chain;
    String url ="";
    switch (blockchainAddressStore.getCurrency().toUpperCase())
    {
      case "ADA":
        //https://api.blockchair.com/{:ada_chain}/raw/address/{:address}
        chain = "cardano";
        url = String.format("%s/%s/raw/address/%s", BLOCKCHAIR_ROOT_API, chain, blockchainAddressStore.getAddress());
        break;
      case "DASH":
        chain = "dash";
        url = String.format("%s/%s/dashboards/address/%s", BLOCKCHAIR_ROOT_API, chain, blockchainAddressStore.getAddress());
        break;
      case "BTC":
        //https://api.blockchair.com/{:btc_chain}/dashboards/address/{:address}
        chain = "bitcoin";
        url = String.format("%s/%s/dashboards/address/%s", BLOCKCHAIR_ROOT_API, chain, blockchainAddressStore.getAddress());
        break;
      case "BCH":
        chain = "bitcoin-cash";
        url = String.format("%s/%s/dashboards/address/%s", BLOCKCHAIR_ROOT_API, chain, blockchainAddressStore.getAddress());
        break;
      case "ETH":
        https://api.blockchair.com/{:eth_chain}/dashboards/address/{:address}₀
        chain = "ethereum";
        url = String.format("%s/%s/raw/dashboards/address/%s", BLOCKCHAIR_ROOT_API, chain, blockchainAddressStore.getAddress());
        break;
    }
    return url;

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

    switch (crypto.toUpperCase() )
    {
      case "ETH":
        satoshiDecimals = 1E18;
        break;
      case "BCH":
      case "BTC":
      case "DASH":
        satoshiDecimals = 1E8;
        break;
      case "ADA":
        // 5000000000 => 5000.000000
        satoshiDecimals = 1E6;
        break;
      default:
        satoshiDecimals = 1.0;
    }

    return satoshiDecimals;
  }

}
