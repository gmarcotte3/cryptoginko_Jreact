package com.marcotte.blockhead.portfolio;


import com.marcotte.blockhead.datastore.PortfolioTracker;
import com.marcotte.blockhead.model.CoinDTO;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Portfolio reports", tags = "portfolio")
@RestController
@RequestMapping("/portfolio")
public class PortfolioController
{
    private static final Logger log = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired PortfolioService portfolioService;

    @PutMapping("")
    public ResponseEntity<List<PortfolioTracker>> portfolio()
    {
        List<PortfolioTracker> portfolioTrackerList = portfolioService.portfolioCheck(false);

        return new ResponseEntity<List<PortfolioTracker>>(portfolioTrackerList, HttpStatus.OK);
    }

    @PutMapping("/bycoins")
    public ResponseEntity<List<CoinDTO>> portfolioByCoins()
    {
        List<CoinDTO> portfolioByCoinList = portfolioService.portfolioByCoins();

        return new ResponseEntity<List<CoinDTO>>(portfolioByCoinList, HttpStatus.OK);
    }

    @PutMapping("/refreshcache")
    public ResponseEntity<List<PortfolioTracker>> portfolio_refresh_cache()
    {
        List<PortfolioTracker> portfolioResult = portfolioService.portfolioCheck(true);

        return new ResponseEntity<List<PortfolioTracker>>(portfolioResult, HttpStatus.OK);
    }

    @PutMapping("/refreshcache/{cryptoname}")
    public ResponseEntity<PortfolioCheckResults> portfolio_refresh_cache(@PathVariable String cryptoname)
    {
        PortfolioCheckResults results =  portfolioService.portfolioCheck(true, cryptoname.toUpperCase());
        return new ResponseEntity<PortfolioCheckResults>(results, HttpStatus.OK);
    }

    @PutMapping("/refreshcache/{cryptoname}/{walletName}")
    public ResponseEntity<PortfolioCheckResults> portfolio_refresh_cache(@PathVariable String cryptoname, @PathVariable String walletName)
    {
        PortfolioCheckResults results =  portfolioService.portfolioCheck(true, cryptoname.toUpperCase(), walletName.toUpperCase());
        return new ResponseEntity<PortfolioCheckResults>(results, HttpStatus.OK);
    }
}
