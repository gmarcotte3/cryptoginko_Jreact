package com.marcotte.blockhead.export;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.model.WalletTransaction;
import com.marcotte.blockhead.util.BlockHeadException;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * handles all the report exporting to csv file.
 */
@Service
public class ExportCsvService
{
  private static final Logger logger = LoggerFactory.getLogger(ExportCsvService.class);

  /**
   * creates detail list of the addresses sorted by crypto, walletName.
   * all the address are listed.
   *
   * @param fileName
   * @param addresses
   * @throws BlockHeadException
   */
  public void writeAddressesByCurencyWalletDetailToCSVStream(String fileName, List<BlockchainAddressStore> addresses) throws BlockHeadException
  {
    PrintWriter printWriter;
    List<BlockchainAddressStore> sortedAddresses = sortAddressByCurrencyAndWallet( addresses);
    try {
      printWriter = new PrintWriter(new File(fileName));
    } catch (FileNotFoundException e) {

      logger.error("failed to open export file (%s) error==%s",fileName, e.getMessage());
      e.printStackTrace();
      throw new BlockHeadException(e.getMessage(), e);
    }

    try {
      ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
      mappingStrategy.setType(BlockchainAddressStore.class );
      mappingStrategy.generateHeader();

      String[] columrns = new String[] {
          "currency",
          "walletName",
          "lastBalance",
          "address",
          "lastUpdated"
      };
      mappingStrategy.setColumnMapping(columrns);

      StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(printWriter)
          .withQuotechar(CSVWriter.NO_ESCAPE_CHARACTER)
          .withMappingStrategy(mappingStrategy)
          .withSeparator(',')
          .build();
      btcsv.write(sortedAddresses);

      printWriter.close();

    } catch ( CsvException ex )
    {
      logger.error("Error mapping addressStore bean to CSV", ex);
      throw new BlockHeadException(ex.getMessage(), ex);
    }

  }

  /**
   * creates a summary of address balances sorted by currency, walletname.
   * this will show the balances of each wallet overall for the crypto currency.
   *
   * @param fileName
   * @param addresses
   * @return
   * @throws BlockHeadException
   */
  public List<BlockchainAddressStore> writeAddressesByCurencyWalletSummaryToCSVStream(String fileName, List<BlockchainAddressStore> addresses) throws BlockHeadException
  {
    PrintWriter printWriter;
    List<BlockchainAddressStore> sortedAddresses = sortAddressByCurrencyAndWallet( addresses);

    try {
      printWriter = new PrintWriter(new File(fileName));
    } catch (FileNotFoundException e) {

      logger.error("failed to open export file (%s) error==%s",fileName, e.getMessage());
      e.printStackTrace();
      throw new BlockHeadException(e.getMessage(), e);
    }

    List<BlockchainAddressStore> summary = new ArrayList<>();

    BlockchainAddressStore currentAddr = null;

    for ( BlockchainAddressStore addr : sortedAddresses )
    {
      // only happens the first time though
      if ( currentAddr == null ) {
        currentAddr = new BlockchainAddressStore();
        currentAddr.setTicker(addr.getTicker());
        currentAddr.setWalletName(addr.getWalletName());
        currentAddr.setLastBalance(0.0);
        summary.add(currentAddr);
      }
      // happens every time the currency or walletname changes
      if (
          addr.getTicker().compareToIgnoreCase(currentAddr.getTicker()) != 0 ||
          addr.getWalletName().compareToIgnoreCase(currentAddr.getWalletName()) != 0)
      {
        currentAddr = new BlockchainAddressStore();
        currentAddr.setTicker(addr.getTicker());
        currentAddr.setWalletName(addr.getWalletName());
        currentAddr.setLastBalance(0.0);
        summary.add(currentAddr);
      }

      // happens every time.
      currentAddr.addToLastBalance(addr.getLastBalance());
    }

    try {
      ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
      mappingStrategy.setType(BlockchainAddressStore.class );
      mappingStrategy.generateHeader();

      String[] columrns = new String[] {
          "currency",
          "walletName",
          "lastBalance"
      };
      mappingStrategy.setColumnMapping(columrns);

      StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(printWriter)
          .withQuotechar(CSVWriter.NO_ESCAPE_CHARACTER)
          .withMappingStrategy(mappingStrategy)
          .withSeparator(',')
          .build();
      btcsv.write(summary);

      printWriter.close();

    } catch ( CsvException ex )
    {
      logger.error("Error mapping addressStore bean to CSV", ex);
      throw new BlockHeadException(ex.getMessage(), ex);
    }

    return summary;
  }

  /**
   * sort address by currency and wallet name
   * This function will reorder the address list so all currency (coin tickers) are sorted
   * and then by the wallet name
   *
   * @param addressStores   list of address to sort
   * @return                returns a sorted version of the input list
   */
  public List<BlockchainAddressStore> sortAddressByCurrencyAndWallet(List<BlockchainAddressStore> addressStores)
  {
    addressStores.sort( new Comparator<BlockchainAddressStore>() {
      @Override
      public int compare(BlockchainAddressStore lhs, BlockchainAddressStore rhs) {
        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
        int compareResult = lhs.getTicker().compareToIgnoreCase(rhs.getTicker());
        if ( compareResult == 0 )
        {
          compareResult = lhs.getWalletName().compareToIgnoreCase(rhs.getWalletName());
        }
        return compareResult;
      }
    });
    return addressStores;
  }

  /**
   * write out a csv file with transactions and gain/loss info.
   *
   * Wallet export of transactions contains the deposit/withdraw of coins
   * but not any detail to calculate gain/loss. these transactions are processed
   * to have gain and loss information. this routine takes a list of those
   * processed transaction and output it as a csv file. The csv file can be used
   * for investment performance analysis and tax reporting applications.
   *
   * @param fileName            The file name of the csv file to output
   * @param transactions        list of processed transaction (with gain/loss info)
   * @throws BlockHeadException Any File IO, format conversion issues throws an
   *                            exception.
   */
  public void writeWalletTransactionsToCsv( String fileName,
                                            List<WalletTransaction> transactions)
          throws BlockHeadException
  {
    PrintWriter printWriter;


    try {
      printWriter = new PrintWriter(new File(fileName));
      printWriter.append("TransactionID, TransactionURL, Timestamp, Coin Amount, Fee, Balance, " +
              "Price, Value, FeeValue, Running Cost, Running Ave Unit Price, Gain Or Loss, " +
              "Exchange Note, Personal Note\n");
    } catch (FileNotFoundException e) {

      logger.error("failed to open export file (%s) error==%s",fileName, e.getMessage());
      e.printStackTrace();
      throw new BlockHeadException(e.getMessage(), e);
    }

    try {
      ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
      mappingStrategy.setType(WalletTransaction.class );
      mappingStrategy.generateHeader();

      String[] columns = new String[] {
              "TransactionID",
              "TransactionURL",
              "transactionTimestamp",
              "coinAmount",
              "fee",
              "balance",
              "fiatPriceAtTransactionDate",
              "fiatValueAtTransactionDate",
              "fiatFeeValueAtTransactionDate",
              "fiatRunningCost",
              "fiatRunningAverageUnitPrice",
              "fiatGainOrLossAtTransactionDate",
              "exchangeNote",
              "personalNote"
      };
      mappingStrategy.setColumnMapping(columns);

      StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(printWriter)
              .withQuotechar(CSVWriter.NO_ESCAPE_CHARACTER)
              .withMappingStrategy(mappingStrategy)
              .withSeparator(',')
              .build();
      btcsv.write(transactions);

      printWriter.close();

    } catch ( CsvException ex )
    {
      logger.error("Error mapping transaction bean to CSV", ex);
      throw new BlockHeadException(ex.getMessage(), ex);
    }

  }
}
