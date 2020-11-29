package com.marcotte.blockhead.portfolio;

import com.marcotte.blockhead.datastore.*;
import com.marcotte.blockhead.model.CoinDTO;
import com.marcotte.blockhead.model.WalletDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@TestPropertySource("PortfolioTest.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class PortfolioServiceTest {

    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;


    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private CoinService coinService;

    @Test
    public void portfolioGetTotalValue() {
        initPortfolio1();

        List<PortfolioTracker> portfolioTrackers = portfolioService.portfolioGetTotalValue();
        assertEquals(4, portfolioTrackers.size());
        assertEquals("USD", portfolioTrackers.get(2).getFiatCurrency());
        assertTrue(Math.abs(portfolioTrackers.get(2).getCoinValue() - 4.0) < 0.001); // total for USD should be $4.0

        // clean up
        blockchainAddressStoreService.deleteAll();
        coinService.deleteAll();
    }

    /**
     * setup:
     *   coin bal   price       total
     *   BTC  1.0   $1.0 USD    $1.0 USD
     *   DASH 1.0   $1.0 USD    $1.0 USD
     *   ETH  1.0   $1.0 USD    $1.0 USD
     *   ADA  10.0  $0.10 USD   $1.0 USD
     */
    @Test
    public void portfolioByCoins() {
        initPortfolio1();

        List<CoinDTO> portfolioByCoins = portfolioService.portfolioByCoins();
        assertEquals(4, portfolioByCoins.size());
        assertEquals("ADA", portfolioByCoins.get(0).getTicker());
        assertEquals("BTC", portfolioByCoins.get(1).getTicker());
        assertEquals("DASH", portfolioByCoins.get(2).getTicker());
        assertEquals("ETH", portfolioByCoins.get(3).getTicker());

        assertTrue( Math.abs(portfolioByCoins.get(0).getFiat_balances().findFiat("USD").getValue() - 1.0) < 0.0001 );

        // clean up
        blockchainAddressStoreService.deleteAll();
        coinService.deleteAll();
    }

    /**
     *  BTC  1.0
     *  DASH 1.0
     *  ETH  1.0
     *  ADA  10.0
     *
     *  wallets:
     *  allan:
     *   BTC
     *   DASH
     *
     *  peter:
     *   ADA
     *   BTC
     *   ETH
     */
    @Test
    public void portfolioByWalletCoins() {
        initPortfolio1();

        List<WalletDTO> walletValues = portfolioService.portfolioByWalletCoins();
        assertEquals(2, walletValues.size());

        assertEquals( "ALLAN", walletValues.get(0).getWalletName());
        assertEquals( 2, walletValues.get(0).getCoinDTOs().size());
        assertEquals( "BTC", walletValues.get(0).getCoinDTOs().get(0).getTicker());

        assertEquals("PETER", walletValues.get(1).getWalletName());
        assertEquals( 3, walletValues.get(1).getCoinDTOs().size());
        assertEquals( "ADA", walletValues.get(1).getCoinDTOs().get(0).getTicker());

        // clean up
        blockchainAddressStoreService.deleteAll();
        coinService.deleteAll();
    }

    private void initPortfolio1() {
        List<BlockchainAddressStore> addresses6 = getAddresses6a();

        for (BlockchainAddressStore addressStore : addresses6 ) {
            blockchainAddressStoreService.save(addressStore);
        }
        List<Coin> coins = getCoines5();

        for( Coin coin : coins) {
            coinService.save(coin);
        }
    }

    /**
     * BTC  1.0
     * DASH 1.0
     * ETH  1.0
     * ADA  10.0
     *
     * wallets:
     * allan:
     *  BTC
     *  DASH
     *
     * peter:
     *  ADA
     *  BTC
     *  ETH
     *
     * @return
     */
    private List<BlockchainAddressStore> getAddresses6a() {
        Date rightNow = new Date();
        List<BlockchainAddressStore> addressList = new ArrayList<>();

        BlockchainAddressStore  addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("peter");
        addressStore.setTicker("BTC");
        addressStore.setLastBalance( 0.5);
        addressStore.setAddress("0bbbbbbbbbbbbbbbb2");
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Bitcoin test2");
        addressStore.setMemo("memo BTC");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("allan");
        addressStore.setTicker("BTC");
        addressStore.setLastBalance( 0.5);
        addressStore.setAddress("0bbbbbbbbbbbbbbbb1");
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Bitcoin test1");
        addressStore.setMemo("memo BTC");
        addressStore.setNumTransactions(3);
        addressList.add(addressStore);

        addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("allan");
        addressStore.setTicker("DASH");
        addressStore.setAddress("Xaaaaaaaaaaaaaaaaaaaaa1");
        addressStore.setLastBalance( 0.5);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("peter");
        addressStore.setAddress("0edddddddddddddddddddog1");
        addressStore.setTicker("ADA");
        addressStore.setLastBalance( 10.0);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Cardanocoin test1");
        addressStore.setMemo("memo Cardano ADA");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        addressStore = new BlockchainAddressStore();
        addressStore.setAddress("0ethethewthewthwthethhhhhh1");
        addressStore.setWalletName("peter");
        addressStore.setTicker("ETH");
        addressStore.setLastBalance( 1.0);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Oshirium test1");
        addressStore.setMemo("memo ETH");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("allan");
        addressStore.setTicker("DASH");
        addressStore.setAddress("Xaaaaaaaaaaaaaaaaaaaaa2");
        addressStore.setLastBalance( 0.50);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        return addressList;
    }

    /**
     * create fake prices
     * BTC $1.0 USD
     * ETH $1.0 USD
     * DASH $1.0 USD
     * ADA  $0.10 USD
     * @return
     */
    private List<Coin> getCoines5() {
        List<Coin> coinList = new ArrayList<>();

        Coin coin = new Coin();
        coin.setCoinName("Cardano ADA");
        coin.setTicker("ADA");
        coin.setPriceNZD( 0.13290);
        coin.setPriceUSD( 0.10 );
        coin.setPriceJPM( 0.0001329);
        coin.setPriceJPY( 1.32900);
        coin.setPriceEUR( 0.10090 );
        coin.setPriceGBP( 0.1500 );
        coinList.add(coin);

        coin = new Coin();
        coin.setCoinName("Bitcoin");
        coin.setTicker("BTC");
        coin.setPriceNZD( 27000.90);
        coin.setPriceUSD( 1.0 );
        coin.setPriceJPM( 19.2);
        coin.setPriceJPY( 13290.0);
        coin.setPriceEUR( 10000.90 );
        coin.setPriceGBP( 15000.0 );
        coinList.add(coin);

        coin = new Coin();
        coin.setCoinName("BCash");
        coin.setTicker("BCH");
        coin.setPriceNZD( 270.90);
        coin.setPriceUSD( 1.0 );
        coin.setPriceJPM( 1.2);
        coin.setPriceJPY( 27090.0);
        coin.setPriceEUR( 100.90 );
        coin.setPriceGBP( 15000.0 );
        coinList.add(coin);


        coin = new Coin();
        coin.setCoinName("Dash");
        coin.setTicker("DASH");
        coin.setPriceNZD( 132.90);
        coin.setPriceUSD( 1.0 );
        coin.setPriceJPM( 1.329);
        coin.setPriceJPY( 13290.0);
        coin.setPriceEUR( 100.90 );
        coin.setPriceGBP( 150.0 );
        coinList.add(coin);

        coin = new Coin();
        coin.setCoinName("Oshireum");
        coin.setTicker("ETH");
        coin.setPriceNZD( 751.15);
        coin.setPriceUSD( 1.0 );
        coin.setPriceJPM( 6.509);
        coin.setPriceJPY( 65090.0);
        coin.setPriceEUR( 800.90 );
        coin.setPriceGBP( 799.0 );
        coinList.add(coin);
        return coinList;
    }

}