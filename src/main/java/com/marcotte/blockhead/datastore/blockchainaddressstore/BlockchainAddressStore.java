package com.marcotte.blockhead.datastore.blockchainaddressstore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * address store with a pointer to the last address balance.
 * address where nextID == null is the latest
 */
@Entity
public class BlockchainAddressStore
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String address;
    private String ticker;
    private Double lastBalance;
    private Integer numTransactions;
    private Timestamp lastUpdated;
    private String message;
    private String memo;
    private Boolean updatedViaBlockChainExplorer;
    private String walletName;

    public BlockchainAddressStore()
    {
        this.walletName = "";
        this.ticker = "";
    }

    public BlockchainAddressStore( BlockchainAddressStore old) {
        setBlockChainAddressStore( old );
    }

    public void setBlockChainAddressStore( BlockchainAddressStore old ) {
        this.address = old.getAddress();
        this.ticker = old.getTicker();
        this.lastBalance = old.getLastBalance();
        this.numTransactions = old.getNumTransactions();
        this.lastUpdated = old.getLastUpdated();
        this.message = old.getMessage();
        this.memo = old.getMemo();
        this.walletName = old.getWalletName();
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

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker.toUpperCase();
    }

    public Double getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(Double lastBalance) {
        this.lastBalance = lastBalance;
    }

    public Double addToLastBalance(Double newamount)
    {
        this.lastBalance += newamount;
        return this.lastBalance;
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

    public String getWalletName()
    {
        return this.walletName;
    }

    public void setWalletName(String walletName)
    {
        this.walletName = walletName.toUpperCase();
    }
}