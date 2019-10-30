package com.marcotte.blockhead.datastore;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "Portfolio reports", tags = "portfolio")
@RestController
@RequestMapping("/portfolio/history/detail")

public class PortfolioTrackerDetailController
{
    @Autowired
    private PortfolioTrackerDetailService portfolioTrackerDetailService;

    @GetMapping("")
    public ResponseEntity<List<PortfolioTrackerDetail>> getAllPortfolioSummary()
    {
        List<PortfolioTrackerDetail> portfolioDetail = portfolioTrackerDetailService.findAll();
        return new ResponseEntity<List<PortfolioTrackerDetail>>(portfolioDetail, HttpStatus.OK);
    }


    @GetMapping("/{currency}")
    public ResponseEntity<List<PortfolioTrackerDetail>> getAllPortfolioDetailByCurrency(@PathVariable String currency)
    {
        List<PortfolioTrackerDetail> portfoliosummary = portfolioTrackerDetailService.findAllByFiatCurrency(currency);
        return new ResponseEntity<List<PortfolioTrackerDetail>>(portfoliosummary, HttpStatus.OK);
    }

    @GetMapping("/{currency}/{cryptoName}")
    public ResponseEntity<List<PortfolioTrackerDetail>> getAllPortfolioSummaryByCurrency(
            @PathVariable String currency,
            @PathVariable String cryptoName)
    {
        List<PortfolioTrackerDetail> portfolioDetail = portfolioTrackerDetailService.findAllByCoinNameAnndFiatCurrency(cryptoName, currency);
        return new ResponseEntity<List<PortfolioTrackerDetail>>(portfolioDetail, HttpStatus.OK);
    }

}
