package com.marcotte.blockhead.controllers.pricedata;

import com.marcotte.blockhead.model.QuoteGeneric;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;
import com.marcotte.blockhead.services.coin.CoinService;
import com.marcotte.blockhead.services.explorerServices.pricequote.CoinPaprikaService;
import com.marcotte.blockhead.services.explorerServices.pricequote.PriceServiceInterface;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Currency General Rest API", tags = "currency")
@RestController
@RequestMapping("/currency/coinpaprika")
public class CoinPaprikaController {
    private static final Logger log = LoggerFactory.getLogger(CoinPaprikaController.class);

    private PriceServiceInterface coinPaparikaService;

    @Autowired
    private CoinService coinService;


    @Autowired
    public CoinPaprikaController(CoinPaprikaService coinPaparikaService)
    {
        this.coinPaparikaService = coinPaparikaService;
    }
    @GetMapping("/quote")
    public ResponseEntity<QuoteGeneric> getQuoteLocal(
            @RequestParam(value = "coin", required = true) final String coinName  )
    {
        QuoteGeneric quoteGeneric = coinPaparikaService.getQuote(coinName.toUpperCase());
        return new ResponseEntity<QuoteGeneric>(quoteGeneric, HttpStatus.OK);
    }


    @PutMapping("/quote")
    public ResponseEntity<QuoteGeneric> putgQuoteLocal(
            @RequestParam(value = "coin", required = true) final String coinName  )
    {
        QuoteGeneric quoteGeneric = coinPaparikaService.getQuote(coinName.toUpperCase());
        coinService.updateCoins(quoteGeneric);
        return new ResponseEntity<QuoteGeneric>(quoteGeneric, HttpStatus.OK);
    }


    @GetMapping("/quote/all")
    public ResponseEntity<List<CoinDTO>> getQuoteAllCoinsNow() {
        List<CoinDTO> result = coinPaparikaService.getPriceAllCoinsNow();

        return new ResponseEntity<List<CoinDTO>>(result, HttpStatus.OK);
    }

    @PutMapping("/quote/all")
    public ResponseEntity<List<CoinDTO>> getQuoteAllCoinsNowSave() {
        List<CoinDTO> result = coinPaparikaService.getPriceAllCoinsNow();
        coinService.updateCoins(result);

        return new ResponseEntity<List<CoinDTO>>(result, HttpStatus.OK);
    }

    /**
     *
     * @param coinID
     * @param date_ddmmyyyy
     * @return
     */
    @GetMapping("/quote/raw")
    public ResponseEntity<FiatCurrencyList> getQuoteRaw(
            @RequestParam(value = "coin", required = true) final String coinTicker,
            @RequestParam(value = "quotedate", required = false) final String date_ddmmyyyy
    )
    {
        FiatCurrencyList currencyList = coinPaparikaService.getPriceByCoinAndDate(coinTicker, date_ddmmyyyy);
        return new ResponseEntity<FiatCurrencyList>(currencyList, HttpStatus.OK);
    }

}
