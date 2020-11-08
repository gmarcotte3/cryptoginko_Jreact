package com.marcotte.blockhead.datastore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoinService {

    @Autowired
    private CoinRepository coinRepository;

    public void save( Coin coin) {
        coinRepository.save(coin);
    }

    public List<Coin> findAll()
    {
        List<Coin> results = new ArrayList<Coin>();
        for (Coin coin : coinRepository.findAll())
        {
            results.add(coin);
        }
        return results;
    }

    public List<Coin>  findByTicker(String ticker) {
        List<Coin> results = new ArrayList<>();
        for ( Coin coin : coinRepository.findByTicker( ticker))
        {
            results.add(coin);
        }
        return results;
    }
}
