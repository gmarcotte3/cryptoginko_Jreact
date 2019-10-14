package com.marcotte.blockhead.datastore;

import javax.persistence.*;
import java.time.Instant;


@Entity
public class DateTrackerDetail
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "DateTrackerID", nullable = false)
    private long dateTrackerID;

    @Column(name = "Currency", nullable = false)
    private String currency;

    @Column(name = "Crypto", nullable = false)
    private String crypto;

    @Column(name = "Price")
    private double price;

    @Column(name = "DateUpdated")
    private Instant dateUpdated;

    public DateTrackerDetail()
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCrypto() {
        return crypto;
    }

    public void setCrypto(String crypto) {
        this.crypto = crypto;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}