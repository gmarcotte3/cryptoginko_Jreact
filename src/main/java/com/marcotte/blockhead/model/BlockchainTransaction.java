package com.marcotte.blockhead.model;

import java.util.Date;

public class BlockchainTransaction
{
    private float transactionAmount;
    private Date transactionDate;
    private String transactionID;

    public BlockchainTransaction() {}
    public BlockchainTransaction(String transactionID, float transactionAmount, Date transactionDate)
    {
        this.transactionID = transactionID;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
    }

    public String getTransactionID()
    {
        return transactionID;
    }

    public BlockchainTransaction setTransactionID(String transactionID)
    {
        this.transactionID = transactionID;
        return this;
    }

    public float getTransactionAmount()
    {
        return transactionAmount;
    }

    public BlockchainTransaction setTransactionAmount(float transactionAmount)
    {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public Date getTransactionDate()
    {
        return transactionDate;
    }

    public BlockchainTransaction setTransactionDate(Date transactionDate)
    {
        this.transactionDate = transactionDate;
        return this;
    }
}

