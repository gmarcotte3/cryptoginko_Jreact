package com.marcotte.blockhead.datastore.portfolio;

import javax.persistence.*;
import java.time.Instant;


@Entity
public class PortfolioTracker
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "DateTrackerID", nullable = false)
    private long dateTrackerID;

    @Column(name = "DateUpdated", nullable = false)
    private Instant dateUpdated;  //copy of the date in dateTracker

    @Column(name = "Currency", nullable = false)
    private String fiatCurrency;

    @Column(name = "CoinValue", nullable = false)
    private double coinValue;

    public PortfolioTracker()
    {
        rightNow();
    }

    public void rightNow()
    {
        this.dateUpdated = Instant.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDateTrackerID() {
        return dateTrackerID;
    }

    public void setDateTrackerID(long dateTrackerID) {
        this.dateTrackerID = dateTrackerID;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(String fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public double getCoinValue() {
        return coinValue;
    }

    public void setCoinValue(double coinValue) {
        this.coinValue = coinValue;
    }
}
