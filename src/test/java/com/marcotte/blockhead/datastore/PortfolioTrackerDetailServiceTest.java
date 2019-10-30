package com.marcotte.blockhead.datastore;

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
public class PortfolioTrackerDetailServiceTest
{

    @Autowired
    private PortfolioTrackerDetailService portfolioTrackerDetailService;
    @Autowired
    private PortfolioTrackerDetailRepository portfolioTrackerDetailRepository;



    @Test
    public void save_test()
    {
        Date date = new Date();
        System.out.println(new Timestamp(date.getTime()));
        DateTracker dateTracker = new DateTracker();
        dateTracker.setId(1L);

        PortfolioTrackerDetail portfolioTrackerDetail = new PortfolioTrackerDetail();
        portfolioTrackerDetail.setId(0);
        portfolioTrackerDetail.setDateTrackerID(dateTracker.getId());
        portfolioTrackerDetail.setDateUpdated(dateTracker.getDateUpdated());
        portfolioTrackerDetail.setCoinValue(1000000.0);
        portfolioTrackerDetail.setFiatCurrency("USD");
        portfolioTrackerDetail.setCoinName("BTC");
        portfolioTrackerDetail.setCoinBalance(1000.00);

        try {
            portfolioTrackerDetailService.save(portfolioTrackerDetail);
            assertTrue(portfolioTrackerDetail.getId() > 0 );

            portfolioTrackerDetailRepository.deleteAll();
        } catch (Exception e) {
            fail("save_test error=" + e.getMessage());
        }
    }

    @Test
    public void findByID_test()
    {
        DateTracker dateTracker = new DateTracker();
        dateTracker.setId(3L);


        PortfolioTrackerDetail portfolioTrackerDetail = new PortfolioTrackerDetail();
        portfolioTrackerDetail.setId(0);
        portfolioTrackerDetail.setDateTrackerID(dateTracker.getId());
        portfolioTrackerDetail.setDateUpdated(dateTracker.getDateUpdated());
        portfolioTrackerDetail.setCoinValue(1000000.0);
        portfolioTrackerDetail.setFiatCurrency("USD");
        portfolioTrackerDetail.setCoinBalance(2000.0);
        portfolioTrackerDetail.setCoinName("BTC");

        try {
            portfolioTrackerDetailService.save(portfolioTrackerDetail);
            assertTrue(portfolioTrackerDetail.getId() > 0 );

            PortfolioTrackerDetail foundPortfolioTracker = portfolioTrackerDetailService.findByID(portfolioTrackerDetail.getId());

            assertEquals(portfolioTrackerDetail.getId(), foundPortfolioTracker.getId());
            assertEquals(portfolioTrackerDetail.getDateTrackerID(), foundPortfolioTracker.getDateTrackerID());
            assertEquals(portfolioTrackerDetail.getFiatCurrency(), foundPortfolioTracker.getFiatCurrency());
            assertEquals(portfolioTrackerDetail.getDateUpdated(), foundPortfolioTracker.getDateUpdated());
            assertEquals(portfolioTrackerDetail.getCoinName(), foundPortfolioTracker.getCoinName());
            assertTrue(foundPortfolioTracker.getCoinBalance() > 1999.0 && foundPortfolioTracker.getCoinBalance() < 2001.0);

            portfolioTrackerDetailRepository.deleteAll();
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

            PortfolioTrackerDetail portfolioTrackerDetail = new PortfolioTrackerDetail();
            portfolioTrackerDetail.setId(0);
            portfolioTrackerDetail.setDateTrackerID(dateTracker.getId());
            portfolioTrackerDetail.setDateUpdated(dateTracker.getDateUpdated());
            portfolioTrackerDetail.setCoinValue(1000000.0);
            portfolioTrackerDetail.setFiatCurrency("RMB");
            portfolioTrackerDetail.setCoinBalance(20.0);
            portfolioTrackerDetail.setCoinName("LTE");

            portfolioTrackerDetailService.save(portfolioTrackerDetail);
            assertTrue(portfolioTrackerDetail.getId() > 0 );

            PortfolioTrackerDetail portfolioTrackerDetail2 = new PortfolioTrackerDetail();
            portfolioTrackerDetail2.setId(0);
            portfolioTrackerDetail2.setDateTrackerID(dateTracker.getId());
            portfolioTrackerDetail2.setDateUpdated(dateTracker.getDateUpdated());
            portfolioTrackerDetail2.setCoinValue(160.0);
            portfolioTrackerDetail2.setFiatCurrency("INR");
            portfolioTrackerDetail2.setCoinBalance(20.0);
            portfolioTrackerDetail2.setCoinName("LTE");

            portfolioTrackerDetailService.save(portfolioTrackerDetail2);
            assertTrue(portfolioTrackerDetail2.getId() > 0 );

            List<PortfolioTrackerDetail> foundallDetails = portfolioTrackerDetailService.findAll();
            assertTrue(foundallDetails != null);
            assertEquals(2, foundallDetails.size());

            portfolioTrackerDetailRepository.deleteAll();
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


            PortfolioTrackerDetail portfolioTrackerDetail1 = new PortfolioTrackerDetail();
            portfolioTrackerDetail1.setId(0);
            portfolioTrackerDetail1.setDateTrackerID(dateTracker.getId());
            portfolioTrackerDetail1.setDateUpdated(dateTracker.getDateUpdated());
            portfolioTrackerDetail1.setCoinValue(1000000.0);
            portfolioTrackerDetail1.setFiatCurrency("USD");
            portfolioTrackerDetail1.setCoinName("ZEC");
            portfolioTrackerDetail1.setCoinBalance(4.1);

            portfolioTrackerDetailService.save(portfolioTrackerDetail1);
            assertTrue(portfolioTrackerDetail1.getId() > 0 );

            PortfolioTrackerDetail portfolioTrackerDetail2 = new PortfolioTrackerDetail();
            portfolioTrackerDetail2.setId(0);
            portfolioTrackerDetail2.setDateTrackerID(dateTracker.getId());
            portfolioTrackerDetail2.setDateUpdated(dateTracker.getDateUpdated());
            portfolioTrackerDetail2.setCoinValue(1600000.0);
            portfolioTrackerDetail2.setFiatCurrency("NZD");
            portfolioTrackerDetail2.setCoinName("ADA");
            portfolioTrackerDetail2.setCoinBalance(500000.0);

            portfolioTrackerDetailService.save(portfolioTrackerDetail2);
            assertTrue(portfolioTrackerDetail2.getId() > 0 );

            // oh no the market crashed...

            DateTracker dateTracker2 = new DateTracker();
            dateTracker2.setId(6L);

            PortfolioTrackerDetail portfolioTrackerDetail3 = new PortfolioTrackerDetail();
            portfolioTrackerDetail3.setId(0);
            portfolioTrackerDetail3.setDateTrackerID(dateTracker2.getId());
            portfolioTrackerDetail3.setDateUpdated(dateTracker2.getDateUpdated());
            portfolioTrackerDetail3.setCoinValue(100.0);
            portfolioTrackerDetail3.setFiatCurrency("USD");
            portfolioTrackerDetail3.setCoinName("ETH");
            portfolioTrackerDetail3.setCoinBalance(10003);

            portfolioTrackerDetailService.save(portfolioTrackerDetail3);
            assertTrue(portfolioTrackerDetail3.getId() > 0 );

            PortfolioTrackerDetail portfolioTrackerDetail4 = new PortfolioTrackerDetail();
            portfolioTrackerDetail4.setId(0);
            portfolioTrackerDetail4.setDateTrackerID(dateTracker2.getId());
            portfolioTrackerDetail4.setDateUpdated(dateTracker2.getDateUpdated());
            portfolioTrackerDetail4.setCoinValue(160.0);
            portfolioTrackerDetail4.setFiatCurrency("NZD");
            portfolioTrackerDetail4.setCoinName("IOT");
            portfolioTrackerDetail4.setCoinBalance(0.2);

            portfolioTrackerDetailService.save(portfolioTrackerDetail4);
            assertTrue(portfolioTrackerDetail4.getId() > 0 );

            List<PortfolioTrackerDetail> foundallDetailsByDateID = portfolioTrackerDetailService.findAllByDateID(dateTracker2.getId());
            assertTrue(foundallDetailsByDateID != null);
            assertEquals(2, foundallDetailsByDateID.size());


            portfolioTrackerDetailRepository.deleteAll();
        } catch (Exception e) {
            fail("save_test error=" + e.getMessage());
        }
    }

    @Test
    public void findAllByCurrency()
    {
        try {

            DateTracker dateTracker = new DateTracker();
            dateTracker.setId(5L);

            PortfolioTrackerDetail portfolioTrackerDetail1 = new PortfolioTrackerDetail();
            portfolioTrackerDetail1.setId(0);
            portfolioTrackerDetail1.setDateTrackerID(dateTracker.getId());
            portfolioTrackerDetail1.setDateUpdated(dateTracker.getDateUpdated());
            portfolioTrackerDetail1.setCoinValue(1002000.0);
            portfolioTrackerDetail1.setFiatCurrency("USD");
            portfolioTrackerDetail1.setCoinName("ETH");
            portfolioTrackerDetail1.setCoinBalance(4.1);

            portfolioTrackerDetailService.save(portfolioTrackerDetail1);
            assertTrue(portfolioTrackerDetail1.getId() > 0 );

            PortfolioTrackerDetail portfolioTrackerDetail2 = new PortfolioTrackerDetail();
            portfolioTrackerDetail2.setId(0);
            portfolioTrackerDetail2.setDateTrackerID(dateTracker.getId());
            portfolioTrackerDetail2.setDateUpdated(dateTracker.getDateUpdated());
            portfolioTrackerDetail2.setCoinValue(1603000.0);
            portfolioTrackerDetail2.setFiatCurrency("NZD");
            portfolioTrackerDetail2.setCoinName("ADA");
            portfolioTrackerDetail2.setCoinBalance(400000.0);

            portfolioTrackerDetailService.save(portfolioTrackerDetail2);
            assertTrue(portfolioTrackerDetail2.getId() > 0 );

            // oh no the market crashed...

            DateTracker dateTracker2 = new DateTracker();
            dateTracker2.setId(6L);

            PortfolioTrackerDetail portfolioTrackerDetail3 = new PortfolioTrackerDetail();
            portfolioTrackerDetail3.setId(0);
            portfolioTrackerDetail3.setDateTrackerID(dateTracker2.getId());
            portfolioTrackerDetail3.setDateUpdated(dateTracker2.getDateUpdated());
            portfolioTrackerDetail3.setCoinValue(100.0);
            portfolioTrackerDetail3.setFiatCurrency("NZD");
            portfolioTrackerDetail3.setCoinName("ETH");
            portfolioTrackerDetail3.setCoinBalance(10003);

            portfolioTrackerDetailService.save(portfolioTrackerDetail3);
            assertTrue(portfolioTrackerDetail3.getId() > 0 );

            PortfolioTrackerDetail portfolioTrackerDetail4 = new PortfolioTrackerDetail();
            portfolioTrackerDetail4.setId(0);
            portfolioTrackerDetail4.setDateTrackerID(dateTracker2.getId());
            portfolioTrackerDetail4.setDateUpdated(dateTracker2.getDateUpdated());
            portfolioTrackerDetail4.setCoinValue(160.0);
            portfolioTrackerDetail4.setFiatCurrency("NZD");
            portfolioTrackerDetail4.setCoinName("ETH");
            portfolioTrackerDetail4.setCoinBalance(0.2);

            portfolioTrackerDetailService.save(portfolioTrackerDetail4);
            assertTrue(portfolioTrackerDetail4.getId() > 0 );

            List<PortfolioTrackerDetail> foundallAdaNZD = portfolioTrackerDetailService.findAllByCoinNameAnndFiatCurrency( "ADA", "NZD");
            assertTrue(foundallAdaNZD != null);
            assertEquals(1, foundallAdaNZD.size());

            List<PortfolioTrackerDetail> foundallDetailsByETH_NZD = portfolioTrackerDetailService.findAllByCoinNameAnndFiatCurrency( "ETH", "NZD");
            assertTrue(foundallDetailsByETH_NZD != null);
            assertEquals(2, foundallDetailsByETH_NZD.size());


            portfolioTrackerDetailRepository.deleteAll();
        } catch (Exception e) {
            fail("save_test error=" + e.getMessage());
        }
    }
}