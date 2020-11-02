package com.marcotte.blockhead.model;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;

import java.util.List;


public class BlockchainTransactionList
{
    private BlockchainAddressStore addressStore;
    private List<BlockchainTransaction> transactionList;
    private boolean transactionsFound;

    public BlockchainTransactionList()
    {
        this.transactionsFound = false;
    }

    public BlockchainTransactionList(BlockchainAddressStore addressStore, List<BlockchainTransaction> transactionList, boolean transactionsFound)
    {
        this.addressStore = addressStore;
        this.transactionList = transactionList;
        this.transactionsFound = transactionsFound;
    }

    public BlockchainAddressStore getAddressStore()
    {
        return addressStore;
    }

    public BlockchainTransactionList setAddressStore(BlockchainAddressStore addressStore)
    {
        this.addressStore = addressStore;
        return this;
    }

    public List<BlockchainTransaction> getTransactionList()
    {
        return transactionList;
    }

    public BlockchainTransactionList setTransactionList(List<BlockchainTransaction> transactionList)
    {
        this.transactionList = transactionList;
        return this;
    }

    public boolean isTransactionsFound()
    {
        return transactionsFound;
    }

    public BlockchainTransactionList setTransactionsFound(boolean transactionsFound)
    {
        this.transactionsFound = transactionsFound;
        return this;
    }
}
