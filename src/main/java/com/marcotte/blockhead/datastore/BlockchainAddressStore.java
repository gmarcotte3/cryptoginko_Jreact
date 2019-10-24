package com.marcotte.blockhead.datastore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class BlockchainAddressStore
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String address;
    private String currency;
    private Double lastBalance;
    private Integer numTransactions;
    private Timestamp lastUpdated;
    private String message;
    private String memo;
    private Boolean updatedViaBlockChainExplorer;

    public BlockchainAddressStore()
    {
    }

    public BlockchainAddressStore(String address, String currency, Double lastBalance, Integer numTransactions, Timestamp lastUpdated, String message)
    {
        this.address = address;
        this.currency = currency;
        this.lastBalance = lastBalance;
        this.numTransactions = numTransactions;
        this.lastUpdated = lastUpdated;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(Double lastBalance) {
        this.lastBalance = lastBalance;
    }

    public Integer getNumTransactions() {
        return numTransactions;
    }

    public void setNumTransactions(Integer numTransactions) {
        this.numTransactions = numTransactions;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getUpdatedViaBlockChainExplorer() {
        return updatedViaBlockChainExplorer;
    }

    public void setUpdatedViaBlockChainExplorer(Boolean updatedViaBlockChainExplorer) {
        this.updatedViaBlockChainExplorer = updatedViaBlockChainExplorer;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}