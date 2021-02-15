package com.marcotte.blockhead.controllers.portfolio;

import com.marcotte.blockhead.datastore.portfolio.PortfolioValueTracker;
import com.marcotte.blockhead.services.portfolio.PortfolioValueTrackerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(value = "Portfolio reports", tags = "portfolio")
@RestController
@RequestMapping("/portfolio/history2")
public class PortfolioValueTrackerController
{
    @Autowired
    private PortfolioValueTrackerService portfolioValueTrackerService;


    @GetMapping("")
    public ResponseEntity<List<PortfolioValueTracker>> getAllPortfolioSummary()
    {
        List<PortfolioValueTracker> portfolioSummary = portfolioValueTrackerService.findAll();
        return new ResponseEntity<List<PortfolioValueTracker>>(portfolioSummary, HttpStatus.OK);
    }

}
