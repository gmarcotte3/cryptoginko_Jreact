package com.marcotte.blockhead.datastore;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CoinRepository extends CrudRepository<Coin, Long> {
    List<Coin> findAll();
    List<Coin> findByTicker( String ticker);
}
