package com.marcotte.blockhead.datastore.portfolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface CoinPriceValueTrackerRepository extends CrudRepository<CoinPriceValueTracker, Long> {
    public List<CoinPriceValueTracker> findAllByPriceDate(LocalDate priceDate);
    public List<CoinPriceValueTracker> findAllByPriceDateAndTicker(LocalDate priceDate,String coinTticker);

    @Query(nativeQuery = true, value =
            "SELECT * FROM COIN_PRICE_VALUE_TRACKER order by price_date, ticker")
    public List<CoinPriceValueTracker> findAllOrderByPriceDateAndTicker();
    public void save(List<CoinPriceValueTracker> coinPriceValueTrackers);
}
