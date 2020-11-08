package com.marcotte.blockhead.datastore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Coin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String coinName;    // name of the coin ie Bitcoin, Dash, BitcoinCash, Cardano etc.
    private String ticker;      // trading symbol ie BTH, DASH, BCH, ADA etc
    private String description;
    // TODO: add things like current market cap, link to current news, links to coin websites up/down in last 24h etc
    // some of these could be part of the CoinDTO, have to think about what we want to track on the disc.

    public Coin() {
        this.coinName = "";
        this.ticker = "";
        this.description = "";
    }
    public Coin(String coinName, String ticker, String description)
    {
        this.coinName = coinName;
        this.ticker = ticker;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
