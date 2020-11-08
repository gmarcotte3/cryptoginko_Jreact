package com.marcotte.blockhead.datastore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@TestPropertySource("BlockchainAddressStoreServiceTest.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class CoinServiceTest {

    @Autowired
    public CoinService coinService;

    @Autowired
    private CoinRepository coinRepository;

    @Test
    public void save() {
        List<Coin> coins = getCoins1();
        for (Coin coin : coins ) {
            coinService.save( coin);
        }
        List<Coin> coins2 = coinService.findAll();
        assertTrue( (coins2.get(0)).getId() > 0 );
        coinRepository.deleteAll();
    }

    @Test
    public void findAll() {
        coinRepository.deleteAll();
    }

    @Test
    public void findByTicker() {
        List<Coin> coins = getCoins2();
        for (Coin coin : coins ) {
            coinService.save( coin);
        }

        List<Coin> coins2 = coinService.findByTicker("ETH");

        //assertTrue(coins2.get(0).getTicker().compareTo("ETH") == 0).

        coinRepository.deleteAll();
    }

    private List<Coin> getCoins1() {
        List<Coin> coins = new ArrayList<>();
        Coin coin1 = new Coin();
        coin1.setCoinName("Bitcoin");
        coin1.setTicker("BTC");
        coin1.setDescription("Bitcoin core");

        coins.add(coin1);
        return  coins;
    }
    private List<Coin> getCoins2() {
        List<Coin> coins = new ArrayList<>();
        Coin coin1 = new Coin();
        coin1.setCoinName("Bitcoin");
        coin1.setTicker("BTC");
        coin1.setDescription("Bitcoin core");
        coins.add(coin1);

        Coin coin2 = new Coin();
        coin2.setCoinName("Ether");
        coin2.setTicker("ETH");
        coin2.setDescription("Oshirium");
        coins.add(coin2);

        Coin coin3 = new Coin();
        coin3.setCoinName("DASH");
        coin3.setTicker("DASH");
        coin3.setDescription("Digital Cash");
        coins.add(coin3);
        return  coins;
    }
}