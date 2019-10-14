package com.marcotte.blockhead.explorerservices.pricequote;

import com.marcotte.blockhead.config.BlockheadConfig;
import com.marcotte.blockhead.datastore.Currency;
import com.marcotte.blockhead.datastore.QuoteGeneric;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * coingecko service. this site can return a historical price in a million different currencies
 *
 * https://www.coingecko.com/api
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

    private static final Logger log = LoggerFactory.getLogger(CoinGeckoService.class);

    private BlockheadConfig blocktestConfig;

    private Map<String, String> cryptoCodeToCoinGekoCoinID;


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
        this.cryptoCodeToCoinGekoCoinID.put( "MONERO", "monero" );
        this.cryptoCodeToCoinGekoCoinID.put( "XLM", "Stellar" );
        this.cryptoCodeToCoinGekoCoinID.put( "ZEC", "zcash" );

    }

    /**
     *
     * @param coinID          coindID (from https://api.coingecko.com/api/v3/coins/list)
     * @param date_ddmmyyyy  date in this format "dd-mm-yyy"
     * @return List<Currency>
     */
    public List<Currency> getPriceByCoinAndDate(String coin, String date_ddmmyyyy)
    {
        RestTemplate restTemplate = new RestTemplate();
        String url;

        // convert the coin ticker symbole to a coin geko coinID
        String coinID = this.cryptoCodeToCoinGekoCoinID.get(coin);
        if (coinID == null) { // this code is not recoignized so return a blank
            log.error("Coin (" + coin + ") not recoignized by goin geko service so no currency quote is possible");
            return new ArrayList<Currency>();
        }

        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        if ( date_ddmmyyyy != null)
        {
            String date_ddmmyyyy_clean = verifyFormatDate(date_ddmmyyyy);

            url = "https://api.coingecko.com/api/v3/coins/" +
                    coinID.toLowerCase() + "/history?date=" + date_ddmmyyyy_clean;
            log.info("url=" + url);
        } else {
            url = "https://api.coingecko.com/api/v3/coins/" +
                    coinID.toLowerCase();
            log.info("url=" + url);
        }

        String theRawJsonQuote = restTemplate.getForObject(url, String.class);
        log.debug("raw json=" + theRawJsonQuote);

        return parseJsonRawQuote(coin, theRawJsonQuote);
    }

    private List<Currency> parseJsonRawQuote(String coin, String theRawJsonQuote )
    {
        JSONObject jsonObj;
        JSONObject marketDataObj;
        JSONObject current_price_DataObj;
        List<Currency> currencyList = new ArrayList<Currency>();

        try {
            jsonObj = new JSONObject(theRawJsonQuote);
            marketDataObj = jsonObj.getJSONObject("market_data");
            current_price_DataObj = marketDataObj.getJSONObject("current_price");
        } catch ( Exception e)
        {
            log.error("missing or bad price and date format coin {} error={}", coin, e.getMessage());
            return currencyList;
        }

        Float nzd_price = getDoubleFromJsonObject(current_price_DataObj, "nzd");
        currencyList.add(new Currency().setCode("NZD").setRate(nzd_price).setDescription("New Zeland Dollar").setSymbol("$"));

        Float usd_price = getDoubleFromJsonObject(current_price_DataObj, "usd");
        currencyList.add(new Currency().setCode("USD").setRate(usd_price).setDescription("US Dollar").setSymbol("$"));

        Float aud_price = getDoubleFromJsonObject(current_price_DataObj, "aud");
        currencyList.add(new Currency().setCode("AUD").setRate(aud_price).setDescription("Aussie Dollar").setSymbol("$"));

        Float jpy_price = getDoubleFromJsonObject(current_price_DataObj, "jpy");
        currencyList.add(new Currency().setCode("JPY").setRate(jpy_price).setDescription("Japanese yen").setSymbol("¥"));

        Float eur_price = getDoubleFromJsonObject(current_price_DataObj, "eur");
        currencyList.add(new Currency().setCode("EUR").setRate(eur_price).setDescription("Euros").setSymbol("€"));

        Float gbp_price = getDoubleFromJsonObject(current_price_DataObj, "gbp");
        currencyList.add(new Currency().setCode("GBP").setRate(gbp_price).setDescription("Pound sterling").setSymbol("£"));

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

    /**
     * check the string date pattern
     * @param date_ddmmyyyy
     * @return
     */
    private String verifyFormatDate( String date_ddmmyyyy)
    {
        String cleanDateString;
        if ( date_ddmmyyyy == null || date_ddmmyyyy.length() == 0)
        {
            Instant nowUtc = Instant.now();
            ZoneId usaLosAngels = ZoneId.of("America/Los_Angeles");
            ZonedDateTime nowUSA = ZonedDateTime.ofInstant(nowUtc, usaLosAngels);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            cleanDateString = nowUSA.format(formatter);

        } else {
            cleanDateString = date_ddmmyyyy;
        }
        return cleanDateString;
    }

    public QuoteGeneric getQuote(String crypto)
    {
        String currencies = blocktestConfig.getCryptoCompareCurrencyList().toUpperCase();

        QuoteGeneric quote = new QuoteGeneric()
                .setTimeISO("latest")
                .setCoinName(crypto)
                .setSymbol(crypto);

        List<Currency> currencyList_raw = getPriceByCoinAndDate(crypto, null);

        List<Currency> currencyList = new ArrayList<Currency>();
        if ( currencies.contains("USD"))
        {
            Currency usd = findCurrencyByName(currencyList_raw, "USD");
            if ( usd != null )
            {
                currencyList.add(usd);
            }
        }

        if ( currencies.contains("EUR"))
        {
            Currency eur = findCurrencyByName(currencyList_raw, "EUR");
            if ( eur != null )
            {
                currencyList.add(eur);
            }
        }


        if ( currencies.contains("NZD"))
        {
            Currency nzd = findCurrencyByName(currencyList_raw, "NZD");
            if ( nzd != null )
            {
                currencyList.add(nzd);
            }
        }

        if ( currencies.contains("AUD"))
        {
            Currency aud = findCurrencyByName(currencyList_raw, "AUD");
            if ( aud != null )
            {
                currencyList.add(aud);
            }
        }

        //TODO british pound
        //TODO chinese
        //TODO korian

        if ( currencies.contains("JPY")) {
            Currency jpy = findCurrencyByName(currencyList_raw, "JPY");
            if ( jpy != null )
            {
                currencyList.add(jpy);
            }
        }

        quote.setCurrency(currencyList);

        return quote;
    }

    private Currency findCurrencyByName(List<Currency> inputCurrencyList, String currencyName)
    {
        Currency result;

        for ( Currency curency : inputCurrencyList )
        {
            if ( curency.getCode().compareToIgnoreCase( currencyName) == 0)
            {
                return curency;
            }
        }
        return null;
    }
}
