package com.marcotte.blockhead.datastore;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioTrackerDetailService
{
    private static final Logger log = LoggerFactory.getLogger(PortfolioTrackerDetailService.class);

    @Autowired
    private PortfolioTrackerDetailRepository portfolioTrackerDetailRepository;


    public void save(PortfolioTrackerDetail portfolioTrackerDetail)
    {
        portfolioTrackerDetailRepository.save(portfolioTrackerDetail);
    }

    public PortfolioTrackerDetail findByID( Long id)
    {
        Optional<PortfolioTrackerDetail> foundPortfolioTracker =  portfolioTrackerDetailRepository.findById(id);
        if ( foundPortfolioTracker.isPresent())
        {
            return foundPortfolioTracker.get();
        }
        return null;
    }

    public List<PortfolioTrackerDetail> findAll()
    {
        List<PortfolioTrackerDetail> foundAll = new ArrayList<>();
        for (PortfolioTrackerDetail portfolioTracker: portfolioTrackerDetailRepository.findAll())
        {
            foundAll.add(portfolioTracker);
        }
        return foundAll;
    }

    public List<PortfolioTrackerDetail> findAllByDateID(long dateTrackerID)
    {
        return portfolioTrackerDetailRepository.findAllByDateTrackerID(dateTrackerID);
    }

    public List<PortfolioTrackerDetail> findAllByCoinNameAnndFiatCurrency(String coinName, String faitCurrency)
    {
        List<PortfolioTrackerDetail> foundAll = new ArrayList<>();
        for (PortfolioTrackerDetail portfolioTrackerDetail : portfolioTrackerDetailRepository.findAllByCoinNameAndFiatCurrency(coinName, faitCurrency) )
        {
            foundAll.add(portfolioTrackerDetail);
        }
        return foundAll;
    }

    public List<PortfolioTrackerDetail> findAllByFiatCurrency(String faitCurrency)
    {
        List<PortfolioTrackerDetail> foundAll = new ArrayList<>();
        for (PortfolioTrackerDetail portfolioTrackerDetail : portfolioTrackerDetailRepository.findAllByFiatCurrency(faitCurrency) )
        {
            foundAll.add(portfolioTrackerDetail);
        }
        return foundAll;
    }
}
