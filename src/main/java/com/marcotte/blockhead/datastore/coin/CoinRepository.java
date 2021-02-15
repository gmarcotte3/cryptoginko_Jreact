package com.marcotte.blockhead.datastore.coin;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CoinRepository extends CrudRepository<Coin, Long> {
    List<Coin> findAll();
    List<Coin> findByTicker( String ticker);
}
