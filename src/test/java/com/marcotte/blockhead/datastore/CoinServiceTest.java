package com.marcotte.blockhead.datastore;

import com.marcotte.blockhead.model.CoinDTO;
import com.marcotte.blockhead.model.FiatCurrency;
import com.marcotte.blockhead.model.FiatNames;
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
        // TODO use find and check equall to input.
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

        assertTrue(coins2.get(0).getTicker().compareTo("ETH") == 0);
        // TODO go deep

        coinRepository.deleteAll();
    }

    @Test
    public void updateCoins() {
        List<CoinDTO> coinDTOS = getCoinDTOs();
        List<Coin>  updatedCoins = coinService.updateCoins(coinDTOS);
        assertEquals(2, updatedCoins.size());
        //TODO go deep
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

    private List<CoinDTO> getCoinDTOs() {
        List<CoinDTO> coinDTOList = new ArrayList<CoinDTO>();
        List<FiatCurrency> fiat_prices;

        CoinDTO coinDTO1 = new CoinDTO();
        coinDTO1.setCoinName("Bitcoin");
        coinDTO1.setTicker("BTC");
        fiat_prices = new ArrayList<FiatCurrency>();
        fiat_prices.add( new FiatCurrency(15000.00, FiatNames.USD));
        fiat_prices.add( new FiatCurrency(20000.00, FiatNames.NZD));
        fiat_prices.add( new FiatCurrency(19500.00, FiatNames.AUD));
        fiat_prices.add( new FiatCurrency(1500000.00, FiatNames.JPY));
        fiat_prices.add( new FiatCurrency(150.00, FiatNames.JPM));
        fiat_prices.add( new FiatCurrency(25000.00, FiatNames.EUR));
        fiat_prices.add( new FiatCurrency(22000.00, FiatNames.GBP));
        fiat_prices.add( new FiatCurrency(22000.00, FiatNames.KRW));
        fiat_prices.add( new FiatCurrency(22000.00, FiatNames.INR));
        fiat_prices.add( new FiatCurrency(1.0, FiatNames.BTC));
        fiat_prices.add( new FiatCurrency(37.0, FiatNames.ETH));
        coinDTO1.setFiat_prices(fiat_prices);
        coinDTOList.add(coinDTO1);


        CoinDTO coinDTO2 = new CoinDTO();
        coinDTO2.setCoinName("Cardano");
        coinDTO2.setTicker("ADA");
        fiat_prices = new ArrayList<FiatCurrency>();
        fiat_prices.add( new FiatCurrency(0.10, FiatNames.USD));
        fiat_prices.add( new FiatCurrency(0.15, FiatNames.NZD));
        fiat_prices.add( new FiatCurrency(0.14, FiatNames.AUD));
        fiat_prices.add( new FiatCurrency(11.0, FiatNames.JPY));
        fiat_prices.add( new FiatCurrency(0.00011, FiatNames.JPM));
        fiat_prices.add( new FiatCurrency(0.25, FiatNames.EUR));
        fiat_prices.add( new FiatCurrency(0.22, FiatNames.GBP));
        fiat_prices.add( new FiatCurrency(22.0, FiatNames.KRW));
        fiat_prices.add( new FiatCurrency(10.0, FiatNames.INR));
        fiat_prices.add( new FiatCurrency(0.00000001, FiatNames.BTC));
        fiat_prices.add( new FiatCurrency(0.00000037, FiatNames.ETH));
        coinDTO2.setFiat_prices(fiat_prices);
        coinDTOList.add(coinDTO2);

        return coinDTOList;
    }
}