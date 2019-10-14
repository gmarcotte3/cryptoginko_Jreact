package com.marcotte.blockhead.datastore;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DateTrackerDetailRepository extends CrudRepository< DateTrackerDetail, Long>
{
    public List<DateTrackerDetail> findAllByDateTrackerID(Long dateTrackerID);
    public List<DateTrackerDetail> findAllByDateTrackerIDAndCurrency(Long dateTrackerID, String currency);
    public List<DateTrackerDetail> findAllByDateTrackerIDAndCurrencyAndCrypto(Long dateTrackerID, String currency, String crypto);
}
