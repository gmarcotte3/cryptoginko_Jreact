package com.marcotte.blockhead.services.explorerServices.pricequote;

import com.marcotte.blockhead.config.BlockheadConfig;
import com.marcotte.blockhead.datastore.CoinService;
import com.marcotte.blockhead.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * coingecko service. this site can return a historical price in a million different currencies
 *
 * https://www.coingecko.com/api
 *
 * docs
 * https://www.coingecko.com/en/api#explore-api
 */
@Service
public class CoinGeckoService
{
    public static final String ADA_ID =  "cardano";
    public static final String DASH_ID = "dash";
    public static final String BITCOIN_ID = "bitcoin";
    public static final String BITCOIN_CASH = "bitcoin-cash";
    public static final String EOS_ID = "eos";
    public static final String ETH_ID = "ethereum";
    public static final String IOTA_ID = "iota";
    public static final String LITECOIN_ID = "litecoin";
    public static final String NEM_ID = "nem";
    public static final String NEO_ID = "neo";
    public static final String MONERO_ID = "monero";
    public static final String STELLAR_ID = "Stellar";
    public static final String ZCASH_ID = "zcash";
    public static final String LINK_ID = "chainlink";
    public static final String MAKER_ID = "maker";

    private static final Logger log = LoggerFactory.getLogger(CoinGeckoService.class);

    private BlockheadConfig blocktestConfig;

    private Map<String, String> cryptoCodeToCoinGekoCoinID;

    @Autowired
    private CoinService coinService;


    @Autowired
    public CoinGeckoService(BlockheadConfig blocktestConfig)
    {
        this.blocktestConfig = blocktestConfig;
        this.cryptoCodeToCoinGekoCoinID = new HashMap<String, String>();

        this.cryptoCodeToCoinGekoCoinID.put( "ADA", "cardano" );
        this.cryptoCodeToCoinGekoCoinID.put( "DASH", "dash" );
        this.cryptoCodeToCoinGekoCoinID.put( "BTC", "bitcoin" );
        this.cryptoCodeToCoinGekoCoinID.put( "BCH", "bitcoin-cash" );
        this.cryptoCodeToCoinGekoCoinID.put( "EOS", "eos" );
        this.cryptoCodeToCoinGekoCoinID.put( "ETH", "ethereum" );
        this.cryptoCodeToCoinGekoCoinID.put( "IOT", "iota" );
        this.cryptoCodeToCoinGekoCoinID.put( "LTC", "litecoin" );
        this.cryptoCodeToCoinGekoCoinID.put( "NEM", "nem" );
        this.cryptoCodeToCoinGekoCoinID.put( "NEO", "neo" );
        this.cryptoCodeToCoinGekoCoinID.put( "XMR", "monero" );
        this.cryptoCodeToCoinGekoCoinID.put( "XLM", "Stellar" );
        this.cryptoCodeToCoinGekoCoinID.put( "ZEC", "zcash" );
        this.cryptoCodeToCoinGekoCoinID.put( "LINK", "chainlink" );
        this.cryptoCodeToCoinGekoCoinID.put( "MKR", "maker" );
    }

    public List<CoinDTO> getPriceAllCoinsNow() {
        return getPriceAllCoinsNow(false);
    }

    public List<CoinDTO> getPriceAllCoinsNow(boolean saveit) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.coingecko.com/api/v3/coins";
        String theRawJsonQuotes = restTemplate.getForObject(url, String.class);

        List<CoinDTO> coinDTOList = parseCoingekoPriceDump(theRawJsonQuotes);
        if ( saveit) {
            coinService.updateCoins(coinDTOList);
        }
        return coinDTOList;
    }

    private List<CoinDTO> parseCoingekoPriceDump(String theRawJsonQuotes) {
        JSONObject jsonObj;
        List<CoinDTO> coinDTOList = new ArrayList<CoinDTO>();

        try {
            jsonObj = new JSONObject("{ coindata:" + theRawJsonQuotes + "}");
            JSONArray coinJArray = jsonObj.getJSONArray("coindata");
            for (int i = 0; i < coinJArray.length(); i++) {
                JSONObject coinJObj = (JSONObject) coinJArray.get(i);
                String ticker = (coinJObj.getString("symbol")).toUpperCase();
                String coinName = coinJObj.getString("name");
                FiatCurrencyList fiatCurrencies = parseJsonRawQuote(ticker, coinJObj );

                CoinDTO coinDTO = new CoinDTO();
                coinDTO.setTicker(ticker);
                coinDTO.setCoinName(coinName);
                coinDTO.setFiat_prices(fiatCurrencies);
                coinDTOList.add(coinDTO);
            }
            //return parseJsonRawQuote(coin, jsonObj );
        } catch (Exception e) {
            log.error("missing or bad price and date format coin {} error={}", e.getMessage());
            return coinDTOList;
        }

        return coinDTOList;
    }
    /**
     *
     * @param coinTicker     coin ticker symbol (BTC, BCH, ETH ...)
     * @param date_ddmmyyyy  date in this format "dd-mm-yyyy"
     * @return List<Currency>
     */
    public FiatCurrencyList getPriceByCoinAndDate(String coinTicker, String date_ddmmyyyy)
    {
        RestTemplate restTemplate = new RestTemplate();
        String url;

        // convert the coin ticker to a coin geko coinID
        String coinID = this.cryptoCodeToCoinGekoCoinID.get(coinTicker);
        if (coinID == null) { // this code is not recoignized so return a blank
            log.error("Coin (" + coinTicker + ") not recoignized by goin geko service so no currency quote is possible");
            return new FiatCurrencyList();
        }

        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        if ( date_ddmmyyyy != null)
        {
            url = "https://api.coingecko.com/api/v3/coins/" +
                    coinID.toLowerCase() + "/history?date=" + date_ddmmyyyy;
            log.info("url=" + url);
        } else {
            url = "https://api.coingecko.com/api/v3/coins/" +
                    coinID.toLowerCase();
            log.info("url=" + url);
        }

        String theRawJsonQuote = restTemplate.getForObject(url, String.class);
        log.debug("raw json=" + theRawJsonQuote);

        return parseJsonRawQuote(coinTicker, theRawJsonQuote);
    }

    /**
     * takes the raw currnecy list and picks out the top ones used. see currently supported currencies.
     *
     * supported currency: USD, NZD, AUD, JPY, EUR, GBP, IND, KRW
     * special case JPM which is JPY/10,000 or 万 ( expressed in units of 10k yen)
     *
     * @param coin
     * @param theRawJsonQuote
     * @return
     */
    private FiatCurrencyList parseJsonRawQuote(String coin, String theRawJsonQuote ) {
        JSONObject jsonObj;
        FiatCurrencyList currencyList = new FiatCurrencyList();

        try {
            jsonObj = new JSONObject(theRawJsonQuote);
            return parseJsonRawQuote(coin, jsonObj );
        } catch (Exception e) {
            log.error("missing or bad price and date format coin {} error={}", coin, e.getMessage());
            return currencyList;
        }
    }

    private FiatCurrencyList parseJsonRawQuote(String coin, JSONObject jsonObj )
    {
        JSONObject marketDataObj;
        JSONObject current_price_DataObj;
        FiatCurrencyList currencyList = new FiatCurrencyList();

        try {
            marketDataObj = jsonObj.getJSONObject("market_data");
            current_price_DataObj = marketDataObj.getJSONObject("current_price");
        } catch ( Exception e)
        {
            log.error("missing or bad price and date format coin {} error={}", coin, e.getMessage());
            return currencyList;
        }

        Float nzd_price = getDoubleFromJsonObject(current_price_DataObj, "nzd");
        currencyList.setFiat(new FiatCurrency().setCode("NZD").setValue(nzd_price).setDescription("New Zeland Dollar").setSymbol("$"));

        Float usd_price = getDoubleFromJsonObject(current_price_DataObj, "usd");
        currencyList.setFiat(new FiatCurrency().setCode("USD").setValue(usd_price).setDescription("US Dollar").setSymbol("$"));

        Float aud_price = getDoubleFromJsonObject(current_price_DataObj, "aud");
        currencyList.setFiat(new FiatCurrency().setCode("AUD").setValue(aud_price).setDescription("Aussie Dollar").setSymbol("$"));

        Float jpy_price = getDoubleFromJsonObject(current_price_DataObj, "jpy");
        currencyList.setFiat(new FiatCurrency().setCode("JPY").setValue(jpy_price).setDescription("Japanese yen").setSymbol("¥"));
        currencyList.setFiat(new FiatCurrency().setCode("JPM").setValue(jpy_price/10000).setDescription("Japanese 万 (10,000 Yen)").setSymbol("万"));

        Float eur_price = getDoubleFromJsonObject(current_price_DataObj, "eur");
        currencyList.setFiat(new FiatCurrency().setCode("EUR").setValue(eur_price).setDescription("Euros").setSymbol("€"));

        Float gbp_price = getDoubleFromJsonObject(current_price_DataObj, "gbp");
        currencyList.setFiat(new FiatCurrency().setCode("GBP").setValue(gbp_price).setDescription("Pound sterling").setSymbol("£"));

        Float krw_price = getDoubleFromJsonObject(current_price_DataObj, "krw");
        currencyList.setFiat(new FiatCurrency().setCode("KRW").setValue(krw_price).setDescription("South Koria won").setSymbol("₩"));

        Float inr_price = getDoubleFromJsonObject(current_price_DataObj, "inr");
        currencyList.setFiat(new FiatCurrency().setCode("INR").setValue(inr_price).setDescription("Indian Rupee").setSymbol("₹"));


        return currencyList;
    }

    private float getDoubleFromJsonObject(JSONObject jsonObj, String key)
    {
        float returnfloat = 0.0F;

        try {
            returnfloat = (float) jsonObj.getDouble(key);
        } catch ( Exception e)
        {
            log.error("missing or bad currency ({}) value  error={}", key, e.getMessage());
        }

        return returnfloat;
    }

    public QuoteGeneric getQuote(String crypto)
    {
        String currencies = blocktestConfig.getCryptoCompareCurrencyList().toUpperCase();

        QuoteGeneric quote = new QuoteGeneric()
                .setTimeISO("latest")
                .setCoinName(crypto)
                .setSymbol(crypto);

        quote.setCurrency(getPriceByCoinAndDate(crypto, null));

        return quote;
    }
}
