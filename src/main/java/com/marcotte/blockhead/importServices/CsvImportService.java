package com.marcotte.blockhead.importServices;

import com.marcotte.blockhead.datastore.BlockchainAddressCsvService;
import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.BlockchainAddressStoreService;
import com.marcotte.blockhead.datastore.CoinService;
import com.marcotte.blockhead.model.CoinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * General csv file import service.
 */
@Service
public class CsvImportService
{
  private static final Logger log = LoggerFactory.getLogger(com.marcotte.blockhead.wallets.exodus.ExodosCsvService.class);
  @Autowired
  private BlockchainAddressStoreService blockchainAddressStoreService;
  @Autowired
  private BlockchainAddressCsvService blockchainAddressCsvService;

  @Autowired
  private CoinService coinService;

  private static final int COL_CSV_ROW_TYPE = 0;
  private static final int COL_ADDRESS = 1;
  private static final int COL_BALANCE = 2;
  private static final int COL_CURRENCY = 3;
  private static final int COL_WALLET_NAME = 4;

  private static final int ROWTYPE_HEADER = 0;
  private static final int ROWTYPE_ADDRESS_DETAIL = 1;

  private static final String IMPORT_MESSAGE = "Updated via Import csv address dump";
  private static final String IMPORT_MEMO = "Import Updated";

  /**
   * Processes csv file input of the form:
   *
   * ROW_TYPE (0|1), ADDRESS,   Balance,  Crypto,   WalletName, Date
   *
   * if row type is 0 then its a header and is skipped,
   * if row type is 1 then its an address detail line with fields address, crypto(BTC,BCH,ETH...), WalletName, Date
   *
   * currently Date field is not used.
   *
   * any columns after the DATE field can be anything and is currently not porcessed.
   *
   * @param csvFileArray  An array of Array of String
   */
  public List<BlockchainAddressStore> processCsvAddressDump(List<List<String>> csvFileArray)
  {
    List<BlockchainAddressStore> savedList = new ArrayList<>();

    if ( csvFileArray == null || csvFileArray.size() == 0)
    {
      return savedList;
    }

    Date rightNow = new Date();
    HashMap<String, CoinDTO> coinMap = coinService.findAllReturnTickerCoinDTOMap();


    for( int j = 0 ; j < csvFileArray.size(); j++)
    {
      List<String> row = csvFileArray.get(j);

      int rowType = Integer.valueOf(row.get(COL_CSV_ROW_TYPE));
      if ( rowType == ROWTYPE_HEADER) {
        continue;
      }

      String address = row.get(COL_ADDRESS);
      Double balance = 0.0;

      try
      {
        balance = Double.valueOf(row.get(COL_BALANCE));
      } catch ( Exception e) {
        //TODO throw exception here.
        log.error("faild to convert balance input:" + row.get(COL_BALANCE));
        balance = 0.0;
      }

      String currency = row.get(COL_CURRENCY);
      String walletname = row.get(COL_WALLET_NAME);

      BlockchainAddressStore lastAddress = blockchainAddressStoreService.findLatestByAddressAndCurrency( address, currency );
      if (lastAddress == null )
      {
        BlockchainAddressStore newAddress = new BlockchainAddressStore();
        newAddress.setAddress(address);
        newAddress.setLastBalance(balance);
        newAddress.setTicker(currency);
        newAddress.setLastUpdated( new Timestamp(rightNow.getTime()));
        newAddress.setMessage(IMPORT_MESSAGE);
        newAddress.setMemo("Exodus update");
        newAddress.setWalletName(walletname);
        if ( validateAddress( newAddress )) {
          blockchainAddressStoreService.save(newAddress);
          savedList.add(newAddress);
        } else {
          // TODO throw exception here
          log.error("Bad Coin import=" + newAddress.toString());
        }
      } else {
        lastAddress.setLastUpdated( new Timestamp(rightNow.getTime()));
        lastAddress.setMessage(IMPORT_MESSAGE);
        lastAddress.setMemo(IMPORT_MEMO);
        lastAddress.setWalletName(walletname);
        lastAddress.setLastBalance(balance);
        lastAddress.setTicker(currency);
        blockchainAddressStoreService.save(lastAddress);
        savedList.add(lastAddress);
      }
      //balance = Double.valueOf(row.get(2));
    } //endfor
    return savedList;

  }
  private boolean validateAddress( BlockchainAddressStore addressToTest ) {
    if ( isNullOrBlank(addressToTest.getAddress()) ) return false;
    if ( isNullOrBlank(addressToTest.getWalletName()) ) return false;
    if ( isNullOrBlank(addressToTest.getTicker()) ) return false;

    return true;

  }
  private boolean isNullOrBlank(String testStr) {
    if ( testStr == null ) return true;
    if ( testStr.length() == 0 ) return true;
    return false;
  }

}
