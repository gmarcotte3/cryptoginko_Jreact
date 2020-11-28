package com.marcotte.blockhead.explorerServices.pricequote;

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
     * @param coinID          coindID (from https://api.coingecko.com/api/v3/coins/list)
     * @param date_ddmmyyyy  date in this format "dd-mm-yyy"
     * @return List<Currency>
     */
    public FiatCurrencyList getPriceByCoinAndDate(String coin, String date_ddmmyyyy)
    {
        RestTemplate restTemplate = new RestTemplate();
        String url;

        // convert the coin ticker to a coin geko coinID
        String coinID = this.cryptoCodeToCoinGekoCoinID.get(coin);
        if (coinID == null) { // this code is not recoignized so return a blank
            log.error("Coin (" + coin + ") not recoignized by goin geko service so no currency quote is possible");
            return new FiatCurrencyList();
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

        // TODO fix this by implemengint reverting it back the way it was then do the conversion raw to fiatList here.
        quote.setCurrency(getPriceByCoinAndDate(crypto, null));

//        FiatCurrencyList currencyList = new FiatCurrencyList();

//        // usa dollar
//        if ( currencies.contains(FiatNames.USD.code))
//        {
//            FiatCurrency usd = findCurrencyByName(currencyList_raw, FiatNames.USD.code);
//            if ( usd != null )
//            {
//                currencyList.add(usd);
//            }
//        }
//
//        // euro
//        if ( currencies.contains(FiatNames.EUR.code))
//        {
//            FiatCurrency eur = findCurrencyByName(currencyList_raw, FiatNames.EUR.code);
//            if ( eur != null )
//            {
//                currencyList.add(eur);
//            }
//        }
//
//        // kiwi dollar
//        if ( currencies.contains(FiatNames.NZD.code))
//        {
//            FiatCurrency nzd = findCurrencyByName(currencyList_raw, FiatNames.NZD.code);
//            if ( nzd != null )
//            {
//                currencyList.add(nzd);
//            }
//        }
//
//        // aussie dollar
//        if ( currencies.contains(FiatNames.AUD.code))
//        {
//            FiatCurrency aud = findCurrencyByName(currencyList_raw, FiatNames.AUD.code);
//            if ( aud != null )
//            {
//                currencyList.add(aud);
//            }
//        }
//
//        // british pound
//        if ( currencies.contains(FiatNames.GBP.code))
//        {
//            FiatCurrency gbp = findCurrencyByName(currencyList_raw, FiatNames.GBP.code);
//            if ( gbp != null )
//            {
//                currencyList.add(gbp);
//            }
//        }
//
//        // korian won
//        if ( currencies.contains(FiatNames.KRW.code))
//        {
//            FiatCurrency krw = findCurrencyByName(currencyList_raw, FiatNames.KRW.code);
//            if ( krw != null )
//            {
//                currencyList.add(krw);
//            }
//        }
//
//        // Indian Rupee
//        if ( currencies.contains(FiatNames.INR.code))
//        {
//            FiatCurrency inr = findCurrencyByName(currencyList_raw, FiatNames.INR.code);
//            if ( inr != null )
//            {
//                currencyList.add(inr);
//            }
//        }
//
//        // Japanese Yen
//        if ( currencies.contains(FiatNames.JPY.code)) {
//            FiatCurrency jpy = findCurrencyByName(currencyList_raw, FiatNames.JPY.code);
//            if ( jpy != null )
//            {
//                currencyList.add(jpy);
//            }
//        }
//
//        if ( currencies.contains(FiatNames.JPM.code)) {
//            FiatCurrency jpm = findCurrencyByName(currencyList_raw, FiatNames.JPM.code);
//            if ( jpm != null )
//            {
//                currencyList.add(jpm);
//            }
//        }
//        quote.setCurrency(currencyList);

        return quote;
    }

    private FiatCurrency findCurrencyByName(List<FiatCurrency> inputCurrencyList, String currencyName)
    {
        FiatCurrency result;

        for ( FiatCurrency curency : inputCurrencyList)
        {
            if ( curency.getCode().compareToIgnoreCase( currencyName) == 0)
            {
                return curency;
            }
        }
        return null;
    }
}
