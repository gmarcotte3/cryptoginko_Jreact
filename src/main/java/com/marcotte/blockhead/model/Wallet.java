package com.marcotte.blockhead.model;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;

import java.util.ArrayList;
import java.util.List;

public class Wallet
{
  private String walletName;
  private String cryptoName;
  List<BlockchainAddressStore> addressStores;
  private double balance;

  public Wallet()
  {
    this.walletName = "";
    this.cryptoName = "";
    this.addressStores = new ArrayList<>();
    this.balance = 0.0;
  }

  public String getWalletName()
  {
    return walletName;
  }

  public void setWalletName(String walletName)
  {
    this.walletName = walletName;
  }

  public String getCryptoName()
  {
    return cryptoName;
  }

  public void setCryptoName(String cryptoName)
  {
    this.cryptoName = cryptoName;
  }

  public List<BlockchainAddressStore> getAddressStores()
  {
    return addressStores;
  }

  public void setAddressStores(List<BlockchainAddressStore> addressStores)
  {
    this.addressStores = addressStores;
  }

  public void addAddressStores(BlockchainAddressStore addressStore)
  {
    if ( this.addressStores == null )
    {
      this.addressStores = new ArrayList<BlockchainAddressStore>();
    }
    this.addressStores.add(addressStore);
  }


  public double getBalance()
  {
    return balance;
  }

  public void setBalance(double balance)
  {
    this.balance = balance;
  }

  public void addBalance(double balance)
  {
    this.balance += balance;
  }
}
