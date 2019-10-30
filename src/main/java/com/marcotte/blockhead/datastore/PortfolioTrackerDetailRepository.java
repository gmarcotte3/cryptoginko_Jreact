package com.marcotte.blockhead.datastore;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PortfolioTrackerDetailRepository extends CrudRepository< PortfolioTrackerDetail, Long>
{
    public List<PortfolioTrackerDetail> findAllByDateTrackerID(Long dateTrackerID);
    public List<PortfolioTrackerDetail> findAllByCoinName(String coinName);
    public List<PortfolioTrackerDetail> findAllByCoinNameAndFiatCurrency(String coinName, String faitCurrency);
    public List<PortfolioTrackerDetail> findAllByFiatCurrency(String faitCurrency);
}
