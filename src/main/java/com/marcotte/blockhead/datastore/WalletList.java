package com.marcotte.blockhead.datastore;

import java.util.ArrayList;
import java.util.List;

public class WalletList
{
  private String cryptoName;
  List<Wallet> wallets;
  private double balance;

  public WalletList()
  {
    setBalance(0.0);
    setCryptoName("");
    this.wallets = new ArrayList<Wallet>();
  }


  public String getCryptoName()
  {
    return cryptoName;
  }

  public void setCryptoName(String cryptoName)
  {
    this.cryptoName = cryptoName;
  }

  public void addBalance( double balance)
  {
    this.balance += balance;
  }

  public double getBalance()
  {
    return balance;
  }

  public void setBalance(double balance)
  {
    this.balance = balance;
  }

  public List<Wallet> getWallets()
  {
    return wallets;
  }

  public void setWallets(List<Wallet> wallets)
  {
    this.wallets = wallets;
  }

  public void addWallet( Wallet wallet)
  {
    if ( this.wallets == null ) {
      this.wallets = new ArrayList<Wallet>();
    }
    this.wallets.add(wallet);
  }
}
