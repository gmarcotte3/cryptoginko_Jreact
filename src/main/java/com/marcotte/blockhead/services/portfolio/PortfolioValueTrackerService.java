package com.marcotte.blockhead.services.portfolio;

import com.marcotte.blockhead.datastore.portfolio.PortfolioValueTracker;
import com.marcotte.blockhead.datastore.portfolio.PortfolioValueTrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioValueTrackerService
{
    private static final Logger log = LoggerFactory.getLogger(PortfolioTrackerService.class);

    @Autowired
    private PortfolioValueTrackerRepository portfolioValueTrackerRepository;


    public void save(PortfolioValueTracker portfolioValueTracker)
    {
        portfolioValueTrackerRepository.save(portfolioValueTracker);
    }

    public List<PortfolioValueTracker> findAll()
    {
        List<PortfolioValueTracker> foundAll = new ArrayList<>();
        for (PortfolioValueTracker portfolioValueTracker: portfolioValueTrackerRepository.findAll())
        {
            foundAll.add(portfolioValueTracker);
        }
        return foundAll;
    }

    public List<PortfolioValueTracker> findAllByDateID(long dateTrackerID)
    {
        return portfolioValueTrackerRepository.findAllByDateTrackerID(dateTrackerID);
    }
}
