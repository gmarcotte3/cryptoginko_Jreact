package com.marcotte.blockhead.datastore;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PortfolioTrackerRepository extends CrudRepository< PortfolioTracker, Long>
{
    public List<PortfolioTracker> findAllByDateTrackerID(Long dateTrackerID);
    public List<PortfolioTracker> findAllByFiatCurrency(String fiatCurrency);
}


