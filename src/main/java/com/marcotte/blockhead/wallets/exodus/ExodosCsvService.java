package com.marcotte.blockhead.wallets.exodus;

import com.marcotte.blockhead.datastore.BlockchainAddressCsvService;
import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.BlockchainAddressStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ExodosCsvService
{
  private static final Logger log = LoggerFactory.getLogger(ExodosCsvService.class);
  @Autowired
  private BlockchainAddressStoreService blockchainAddressStoreService;
  @Autowired
  private BlockchainAddressCsvService blockchainAddressCsvService;

  /**
   * Exodus wallet address csv dump is in the format
   * @param csvFileArray
   */
  public void processExodusAddressDump(List<List<String>> csvFileArray, String currency, String walletname)
  {
    if ( csvFileArray == null || csvFileArray.size() == 0)
    {
      return;
    }

    List<String> header = csvFileArray.get(0);
    if ( header.size() != 3 ) {
      log.error("file format is invalid  ");
      return;
    }

    Date rightNow = new Date();

    for( int j = 1 ; j < csvFileArray.size(); j++)
    {
      List<String> row = csvFileArray.get(j);

      String address = row.get(0);
      Double balance = 0.0;

      try
      {
        balance = Double.valueOf(row.get(2));
      } catch ( Exception e) {
        log.error("faild to convert balance input:" + row.get(2));
        balance = 0.0;
      }


      BlockchainAddressStore lastAddress = blockchainAddressStoreService.findLatestByAddressAndCurrency( address , currency);
      if (lastAddress == null )
      {
        BlockchainAddressStore newAddress = new BlockchainAddressStore();
        newAddress.setAddress(address);
        newAddress.setLastBalance(balance);
        newAddress.setCurrency(currency);
        newAddress.setLastUpdated( new Timestamp(rightNow.getTime()));
        newAddress.setMessage("Updated via Exodus address dump");
        newAddress.setMemo("Exodus update");
        newAddress.setWalletName(walletname);

        blockchainAddressStoreService.saveWithHistory(newAddress);
      } else {
        lastAddress.setLastUpdated( new Timestamp(rightNow.getTime()));
        lastAddress.setMessage("Updated via Exodus address dump");
        lastAddress.setMemo("Exodus update");
        lastAddress.setWalletName(walletname);
        lastAddress.setLastBalance(balance);
        lastAddress.setCurrency(currency);
        blockchainAddressStoreService.saveWithHistory(lastAddress);
      }

      balance = Double.valueOf(row.get(2));
    }

  }

}
