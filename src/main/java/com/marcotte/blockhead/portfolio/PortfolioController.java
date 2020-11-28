package com.marcotte.blockhead.portfolio;


import com.marcotte.blockhead.datastore.PortfolioTracker;
import com.marcotte.blockhead.model.CoinDTO;
import com.marcotte.blockhead.model.WalletDTO;
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

    /**
     * This one should go update the coin balances and calculate the total portfolio.
     * not using this now because the get coin balances depends on REST APIs to block chain explorers which
     * are unreliable/expensive and security holes. Will be replaceing this function with direct calls to local
     * full node wallets or trusted nodes on the networks.
     * @return
     */
    @PutMapping("")
    public ResponseEntity<List<PortfolioTracker>> portfolio()
    {
        List<PortfolioTracker> portfolioTrackerList = portfolioService.portfolioCheck(false);
        return new ResponseEntity<List<PortfolioTracker>>(portfolioTrackerList, HttpStatus.OK);
    }

    @PutMapping("/total")
    public ResponseEntity<List<PortfolioTracker>> getPortfolioTotal() {
        List<PortfolioTracker> portfolioTrackerList = portfolioService.portfolioGetTotalValue();
        return new ResponseEntity<List<PortfolioTracker>>(portfolioTrackerList, HttpStatus.OK);
    }


    @GetMapping("/bycoins")
    public ResponseEntity<List<CoinDTO>> portfolioByCoins()
    {
        List<CoinDTO> portfolioByCoinList = portfolioService.portfolioByCoins();

        return new ResponseEntity<List<CoinDTO>>(portfolioByCoinList, HttpStatus.OK);
    }

    @GetMapping("/byWallet")
    public ResponseEntity<List<WalletDTO>> portfolioByWalletAndCoins()
    {
        List<WalletDTO> portfolioByWalletCoinList = portfolioService.portfolioByWalletCoins();

        return new ResponseEntity<List<WalletDTO>>(portfolioByWalletCoinList, HttpStatus.OK);
    }

}
