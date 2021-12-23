package com.marcotte.blockhead.datastore.portfolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface CoinPriceValueTrackerRepository extends CrudRepository<CoinPriceValueTracker, Long> {
    public List<CoinPriceValueTracker> findAllByPriceDate(LocalDate priceDate);
    public List<CoinPriceValueTracker> findAllByPriceDateAndTicker(LocalDate priceDate,String coinTticker);
//    public List<CoinPriceValueTracker> findAllOrderByPriceDateAndTicker();
    public void save(List<CoinPriceValueTracker> coinPriceValueTrackers);
}
