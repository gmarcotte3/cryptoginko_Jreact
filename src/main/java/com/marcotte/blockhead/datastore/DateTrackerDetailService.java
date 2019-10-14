package com.marcotte.blockhead.datastore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class DateTrackerDetailService
{
    private static final Logger log = LoggerFactory.getLogger(DateTrackerDetailService.class);

    @Autowired
    DateTrackerDetailRepository dateTrackerDetailRepository;

    public void save(DateTrackerDetail dateTrackerDetail)
    {
        dateTrackerDetailRepository.save(dateTrackerDetail);
    }

    public DateTrackerDetail findByID(Long dateTrackerDetailID)
    {
        Optional<DateTrackerDetail>  foundDateDetail = dateTrackerDetailRepository.findById(dateTrackerDetailID);
        if ( foundDateDetail.isPresent())
        {
            return foundDateDetail.get();
        }

        log.error("DateTrackerDetail not found id={}", dateTrackerDetailID);
        return null;
    }

    public List<DateTrackerDetail> findAll()
    {
        List<DateTrackerDetail> foundDateTrackers = new ArrayList<>();
        for (DateTrackerDetail dateTrackerDetail : dateTrackerDetailRepository.findAll() )
        {
           foundDateTrackers.add( dateTrackerDetail);
        }
        return foundDateTrackers;
    }


    public List<DateTrackerDetail> findByDateTrackerID(Long dateTrackerID)
    {
        return dateTrackerDetailRepository.findAllByDateTrackerID(dateTrackerID);
    }

    public List<DateTrackerDetail> findByDateTrackerIDAndCurrency(Long dateTrackerID, String currency)
    {
        return dateTrackerDetailRepository.findAllByDateTrackerIDAndCurrency(dateTrackerID, currency);
    }

    public List<DateTrackerDetail> findByDateTrackerIDAndCurrencyAndCrypto(Long dateTrackerID, String currency, String crypto)
    {
        return dateTrackerDetailRepository.findAllByDateTrackerIDAndCurrencyAndCrypto(dateTrackerID, currency, crypto);
    }
}
