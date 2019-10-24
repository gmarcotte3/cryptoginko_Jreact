package com.marcotte.blockhead.portfolio;


import com.marcotte.blockhead.datastore.CoinList;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "Portfolio reports", tags = "portfolio")
@RestController
@RequestMapping("/portfolio")
public class PortfolioController
{
    private static final Logger log = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired PortfolioService portfolioService;

    @PutMapping("")
    public ResponseEntity<List<CoinList>> portfolio()
    {
        List<CoinList> portfolioResult = portfolioService.portfolioCheck(false);

        return new ResponseEntity<List<CoinList>>(portfolioResult, HttpStatus.OK);
    }

    @PutMapping("/refreshcache")
    public ResponseEntity<List<CoinList>> portfolio_refresh_cache()
    {
        List<CoinList> portfolioResult = portfolioService.portfolioCheck(true);

        return new ResponseEntity<List<CoinList>>(portfolioResult, HttpStatus.OK);
    }

    @PutMapping("/refreshcache/{cryptoname}")
    public ResponseEntity<List<CoinList>> portfolio_refresh_cache(@PathVariable String cryptoname)
    {
        List<CoinList> portfolioResult = portfolioService.portfolioCheck(true, cryptoname);
        return new ResponseEntity<List<CoinList>>(portfolioResult, HttpStatus.OK);
    }
}
