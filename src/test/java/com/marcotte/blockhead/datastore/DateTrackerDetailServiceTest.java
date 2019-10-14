package com.marcotte.blockhead.datastore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;


@TestPropertySource("BlockchainAddressStoreServiceTest.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class DateTrackerDetailServiceTest
{
    @Autowired
    public DateTrackerService dateTrackerService;
    @Autowired
    public DateTrackerRepository dateTrackerRepository;
    @Autowired
    public DateTrackerDetailService dateTrackerDetailService;
    @Autowired
    private DateTrackerDetailRepository dateTrackerDetailRepository;

    @Test
    public void save()
    {
        createRecords();

        List<DateTrackerDetail> foundDateTrackerDetails = dateTrackerDetailService.findAll();
        assertEquals(3, foundDateTrackerDetails.size());

        dateTrackerDetailRepository.deleteAll();
        dateTrackerRepository.deleteAll();
    }


    @Test
    public void findByDateTrackerIDAndCurrencyAndCrypto()
    {
        DateTracker dateTracker = createRecords();
        List<DateTrackerDetail> foundDateTrackerDetails = dateTrackerDetailService.findByDateTrackerIDAndCurrencyAndCrypto(dateTracker.getId(), "JPY", "BTC");
        assertEquals(1, foundDateTrackerDetails.size() );

        List<DateTrackerDetail> foundDateTrackerDetail2s = dateTrackerDetailService.findByDateTrackerIDAndCurrency(dateTracker.getId(), "NZD");
        assertEquals(2, foundDateTrackerDetail2s.size() );

        dateTrackerDetailRepository.deleteAll();
        dateTrackerRepository.deleteAll();
    }

    private DateTracker createRecords()
    {
        DateTracker dateTracker = new DateTracker();
        dateTrackerService.save(dateTracker);
        assertTrue( dateTracker.getId() != null && dateTracker.getId() > 0);

        DateTrackerDetail dateTrackerDetail = new DateTrackerDetail();
        dateTrackerDetail.setDateTrackerID(dateTracker.getId());
        dateTrackerDetail.setCrypto("BTC");
        dateTrackerDetail.setCurrency("NZD");
        dateTrackerDetail.setPrice(100.45);
        dateTrackerDetailService.save(dateTrackerDetail);

        dateTrackerDetail = new DateTrackerDetail();
        dateTrackerDetail.setDateTrackerID(dateTracker.getId());
        dateTrackerDetail.setCrypto("BTC");
        dateTrackerDetail.setCurrency("JPY");
        dateTrackerDetail.setPrice(6780.22);
        dateTrackerDetailService.save(dateTrackerDetail);

        dateTrackerDetail = new DateTrackerDetail();
        dateTrackerDetail.setDateTrackerID(dateTracker.getId());
        dateTrackerDetail.setCrypto("BCH");
        dateTrackerDetail.setCurrency("NZD");
        dateTrackerDetail.setPrice(100.0);
        dateTrackerDetailService.save(dateTrackerDetail);

        return dateTracker;
    }
}