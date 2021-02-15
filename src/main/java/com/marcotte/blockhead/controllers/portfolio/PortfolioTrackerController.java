package com.marcotte.blockhead.controllers.portfolio;

import com.marcotte.blockhead.datastore.portfolio.PortfolioTracker;
import com.marcotte.blockhead.services.portfolio.PortfolioTrackerService;
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
@RequestMapping("/portfolio/history")
public class PortfolioTrackerController
{
    @Autowired
    private PortfolioTrackerService portfolioTrackerService;


    @GetMapping("")
    public ResponseEntity<List<PortfolioTracker>> getAllPortfolioSummary()
    {
        List<PortfolioTracker> portfoliosummary = portfolioTrackerService.findAll();
        return new ResponseEntity<List<PortfolioTracker>>(portfoliosummary, HttpStatus.OK);
    }


    @GetMapping("/{currency}")
    public ResponseEntity<List<PortfolioTracker>> getAllPortfolioSummaryByCurrency(@PathVariable String currency)
    {
        List<PortfolioTracker> portfoliosummary = portfolioTrackerService.findAllByCurrency(currency);
        return new ResponseEntity<List<PortfolioTracker>>(portfoliosummary, HttpStatus.OK);
    }
}
