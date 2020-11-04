package com.marcotte.blockhead.explorerServices.pricequote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcotte.blockhead.model.CoinDTO;
import com.marcotte.blockhead.model.FiatCurrency;
import com.marcotte.blockhead.model.QuoteGeneric;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@Api(value = "Currency General Rest API", tags = "currency")
@RestController
@RequestMapping("/currency/coingeko")
public class  CoinGeckoController
{
    private static final Logger log = LoggerFactory.getLogger(CoinGeckoController.class);

    private CoinGeckoService coinGeckoService;

    @Autowired
    public CoinGeckoController(CoinGeckoService coinGeckoService)
    {
        this.coinGeckoService = coinGeckoService;
    }
    @GetMapping("/quote")
    public ResponseEntity<QuoteGeneric> getQuoteLocal(
            @RequestParam(value = "coin", required = true) final String coinName  )
    {
        QuoteGeneric quoteGeneric = coinGeckoService.getQuote(coinName.toUpperCase());
        return new ResponseEntity<QuoteGeneric>(quoteGeneric, HttpStatus.OK);
    }

    @GetMapping("/quote/all")
    public ResponseEntity<List<CoinDTO>> getQuoteAllCoinsNow() {
        List<CoinDTO> result = coinGeckoService.getPriceAllCoinsNow();
        
        return new ResponseEntity<List<CoinDTO>>(result, HttpStatus.OK);
    }

    @GetMapping("/quote/raw")
    public ResponseEntity<List<FiatCurrency>> getQuoteRaw(
            @RequestParam(value = "coin", required = true) final String coinID,
            @RequestParam(value = "quotedate", required = false) final String date_ddmmyyyy
    )
    {
        List<FiatCurrency> currencyList = coinGeckoService.getPriceByCoinAndDate(coinID, date_ddmmyyyy);
        return new ResponseEntity<List<FiatCurrency>>(currencyList, HttpStatus.OK);
    }



}