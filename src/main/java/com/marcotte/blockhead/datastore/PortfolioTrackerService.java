package com.marcotte.blockhead.datastore;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioTrackerService
{
    private static final Logger log = LoggerFactory.getLogger(PortfolioTrackerService.class);

    @Autowired
    private PortfolioTrackerRepository portfolioTrackerRepository;


    public void save(PortfolioTracker portfolioTracker)
    {
        portfolioTrackerRepository.save(portfolioTracker);
    }

    public PortfolioTracker findByID( Long id)
    {
        Optional<PortfolioTracker> foundPortfolioTracker =  portfolioTrackerRepository.findById(id);
        if ( foundPortfolioTracker.isPresent())
        {
            return foundPortfolioTracker.get();
        }
        return null;
    }

    public List<PortfolioTracker> findAll()
    {
        List<PortfolioTracker> foundAll = new ArrayList<>();
        for (PortfolioTracker portfolioTracker: portfolioTrackerRepository.findAll())
        {
           foundAll.add(portfolioTracker);
        }
        return foundAll;
    }

    public List<PortfolioTracker> findAllByDateID(long dateTrackerID)
    {
        return portfolioTrackerRepository.findAllByDateTrackerID(dateTrackerID);
    }

    public List<PortfolioTracker> findAllByCurrency(String currencyName)
    {
        List<PortfolioTracker> foundAll = new ArrayList<>();
        for (PortfolioTracker portfolioTracker : portfolioTrackerRepository.findAllByFiatCurrency(currencyName) )
        {
            foundAll.add(portfolioTracker);
        }
        return foundAll;
    }

}
