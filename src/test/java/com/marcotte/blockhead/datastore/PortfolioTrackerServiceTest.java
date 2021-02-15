package com.marcotte.blockhead.datastore;

import com.marcotte.blockhead.controllers.portfolio.PortfolioTracker;
import com.marcotte.blockhead.datastore.datetraker.DateTracker;
import com.marcotte.blockhead.datastore.portfolio.PortfolioTrackerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


@TestPropertySource("BlockchainAddressStoreServiceTest.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class PortfolioTrackerServiceTest
{

    @Autowired
    private PortfolioTrackerService portfolioTrackerService;

    @Test
    public void save_test()
    {
        Date date = new Date();
        System.out.println(new Timestamp(date.getTime()));
        DateTracker dateTracker = new DateTracker();
        dateTracker.setId(1L);

        PortfolioTracker portfolioTracker = new PortfolioTracker();
        portfolioTracker.setId(0);
        portfolioTracker.setDateTrackerID(dateTracker.getId());
        portfolioTracker.setDateUpdated(dateTracker.getDateUpdated());
        portfolioTracker.setCoinValue(1000000.0);
        portfolioTracker.setFiatCurrency("USD");

        try {
            portfolioTrackerService.save(portfolioTracker);
            assertTrue(portfolioTracker.getId() > 0 );
        } catch (Exception e) {
            fail("save_test error=" + e.getMessage());
        }
    }

    @Test
    public void findAll_test()
    {
        try {
            DateTracker dateTracker = new DateTracker();
            dateTracker.setId(2L);

            PortfolioTracker portfolioTracker = new PortfolioTracker();
            portfolioTracker.setId(0);
            portfolioTracker.setDateTrackerID(dateTracker.getId());
            portfolioTracker.setDateUpdated(dateTracker.getDateUpdated());
            portfolioTracker.setCoinValue(1000000.0);
            portfolioTracker.setFiatCurrency("USD");

            portfolioTrackerService.save(portfolioTracker);
            assertTrue(portfolioTracker.getId() > 0 );

            PortfolioTracker portfolioTracker2 = new PortfolioTracker();
            portfolioTracker2.setId(0);
            portfolioTracker2.setDateTrackerID(dateTracker.getId());
            portfolioTracker2.setDateUpdated(dateTracker.getDateUpdated());
            portfolioTracker2.setCoinValue(1600000.0);
            portfolioTracker2.setFiatCurrency("NZD");

            portfolioTrackerService.save(portfolioTracker2);
            assertTrue(portfolioTracker2.getId() > 0 );

            List<PortfolioTracker> foundallDetails = portfolioTrackerService.findAll();
            assertTrue(foundallDetails != null);
            assertTrue(foundallDetails.size() >= 2);


        } catch (Exception e) {
            fail("save_test error=" + e.getMessage());
        }
    }

    @Test
    public void findByID_test()
    {
        DateTracker dateTracker = new DateTracker();
        dateTracker.setId(3L);


        PortfolioTracker portfolioTracker = new PortfolioTracker();
        portfolioTracker.setId(0);
        portfolioTracker.setDateTrackerID(dateTracker.getId());
        portfolioTracker.setDateUpdated(dateTracker.getDateUpdated());
        portfolioTracker.setCoinValue(1000000.0);
        portfolioTracker.setFiatCurrency("USD");

        try {
            portfolioTrackerService.save(portfolioTracker);
            assertTrue(portfolioTracker.getId() > 0 );

            PortfolioTracker foundPortfolioTracker = portfolioTrackerService.findByID(portfolioTracker.getId());

            assertEquals(portfolioTracker.getId(), foundPortfolioTracker.getId());
            assertEquals(portfolioTracker.getDateTrackerID(), foundPortfolioTracker.getDateTrackerID());
            assertEquals(portfolioTracker.getFiatCurrency(), foundPortfolioTracker.getFiatCurrency());
            assertEquals(portfolioTracker.getDateUpdated(), foundPortfolioTracker.getDateUpdated());
        } catch (Exception e) {
            fail("save_test error=" + e.getMessage());
        }
    }

    /**
     * test getting the portfolio list on a dateid.
     */
    @Test
    public void findAllByDateID_test()
    {
        try {

            DateTracker dateTracker = new DateTracker();
            dateTracker.setId(5L);


            PortfolioTracker portfolioTracker1 = new PortfolioTracker();
            portfolioTracker1.setId(0);
            portfolioTracker1.setDateTrackerID(dateTracker.getId());
            portfolioTracker1.setDateUpdated(dateTracker.getDateUpdated());
            portfolioTracker1.setCoinValue(1000000.0);
            portfolioTracker1.setFiatCurrency("USD");

            portfolioTrackerService.save(portfolioTracker1);
            assertTrue(portfolioTracker1.getId() > 0 );

            PortfolioTracker portfolioTracker2 = new PortfolioTracker();
            portfolioTracker2.setId(0);
            portfolioTracker2.setDateTrackerID(dateTracker.getId());
            portfolioTracker2.setDateUpdated(dateTracker.getDateUpdated());
            portfolioTracker2.setCoinValue(1600000.0);
            portfolioTracker2.setFiatCurrency("NZD");

            portfolioTrackerService.save(portfolioTracker2);
            assertTrue(portfolioTracker2.getId() > 0 );

            // oh no the market crashed...

            DateTracker dateTracker2 = new DateTracker();
            dateTracker2.setId(6L);

            PortfolioTracker portfolioTracker3 = new PortfolioTracker();
            portfolioTracker3.setId(0);
            portfolioTracker3.setDateTrackerID(dateTracker2.getId());
            portfolioTracker3.setDateUpdated(dateTracker2.getDateUpdated());
            portfolioTracker3.setCoinValue(100.0);
            portfolioTracker3.setFiatCurrency("USD");

            portfolioTrackerService.save(portfolioTracker3);
            assertTrue(portfolioTracker3.getId() > 0 );

            PortfolioTracker portfolioTracker4 = new PortfolioTracker();
            portfolioTracker4.setId(0);
            portfolioTracker4.setDateTrackerID(dateTracker2.getId());
            portfolioTracker4.setDateUpdated(dateTracker2.getDateUpdated());
            portfolioTracker4.setCoinValue(160.0);
            portfolioTracker4.setFiatCurrency("NZD");

            portfolioTrackerService.save(portfolioTracker4);
            assertTrue(portfolioTracker4.getId() > 0 );

            List<PortfolioTracker> foundallDetailsByDateID = portfolioTrackerService.findAllByDateID(dateTracker2.getId());
            assertTrue(foundallDetailsByDateID != null);
            assertEquals(2, foundallDetailsByDateID.size());


        } catch (Exception e) {
            fail("save_test error=" + e.getMessage());
        }
    }
}