package com.marcotte.blockhead.importServices;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.model.WalletTransaction;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.marcotte.blockhead.util.ReadCSV.readCsv;
import static com.marcotte.blockhead.util.ReadCSV.readFileCsv;


@Api(value = "Import CSV file", tags = "Import")
@RestController
@RequestMapping("/import")
public class CsvImportController
{
  private static final Logger log = LoggerFactory.getLogger(com.marcotte.blockhead.wallets.exodus.ExodusCsvController.class);

  @Autowired
  private ExodusCSVservice exodusCSVservice;
  @Autowired
  private CsvImportService importCsvService;

  /**
   * This reads a csv file and dumps it in json format. used for testing and development
   * for looking at a csv file export from wallet software.
   *
   * @param filename
   * @return
   */
  @GetMapping("/csvfile")
  public ResponseEntity<List<List<String>>> importGeneralCSVfile(String filename)
  {
    List<List<String>> csvFileArray;

    try
    {
      csvFileArray = readCsv(filename);
    } catch (FileNotFoundException e) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    } catch (IOException e2) {
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<List<String>>> (csvFileArray, HttpStatus.OK);
  }

  /**
   * read csv file of addresses with the format:
   *
   * ROW_TYPE (0|1), ADDRESS,   Balance,  Crypto,   WalletName
   *
   * if row type is 0 then its a header and is skipped,
   * if row type is 1 then its an address detail line with fields address, crypto(BTC,BCH,ETH...), WalletName
   *
   * csv file should be comma delimited, and the strings should all be quoted.
   *
   *
   * @param filename
   * @return
   */
  @PutMapping("/addresses")
  public ResponseEntity<List<BlockchainAddressStore>> importAddressesFromCSVfile(String filename)
  {
    List<List<String>> csvFileArray;
    List<BlockchainAddressStore> savedList;

    try
    {
      csvFileArray = readCsv(filename);
      savedList = importCsvService.processCsvAddressDump(csvFileArray);

    } catch (FileNotFoundException e) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    } catch (IOException e2) {
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<BlockchainAddressStore>> (savedList, HttpStatus.OK);
  }

  @PostMapping("/addressescsv")
  public ResponseEntity<List<BlockchainAddressStore>> importAddressesPostFromCSVfile(
          @RequestParam("file") MultipartFile file,  RedirectAttributes redirectAttributes) {

    List<List<String>> csvFileArray;
    List<BlockchainAddressStore> savedList = new ArrayList<BlockchainAddressStore>();

    if (file.isEmpty()) {
      redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    try {
      csvFileArray = readFileCsv(file);
      savedList = importCsvService.processCsvAddressDump(csvFileArray);

      redirectAttributes.addFlashAttribute("message",
              "You successfully uploaded '" + file.getOriginalFilename() + "'");

    } catch (FileNotFoundException e) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    } catch (IOException e2) {
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity(savedList, HttpStatus.OK);
  }

  @PostMapping("/exodus/transactions")
  public ResponseEntity<List<WalletTransaction>> importExodusTransactionsCSVfile(
          @RequestParam("file") MultipartFile file,
          RedirectAttributes redirectAttributes,
          @RequestParam(value = "coin", required = true) final String coinTicker,
          @RequestParam(value = "fiat", required = true) final String fiatCode ) {

    List<List<String>> csvFileArray;
    List<WalletTransaction> walletTransactions = new ArrayList<>();

    if (file.isEmpty()) {
      redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    try {
      csvFileArray = readFileCsv(file);
      walletTransactions = exodusCSVservice.parseTransactionCsv(csvFileArray);

      exodusCSVservice.calculateGainLostTransactions( walletTransactions, fiatCode, coinTicker);

      redirectAttributes.addFlashAttribute("message",
              "You successfully uploaded '" + file.getOriginalFilename() + "'");

    } catch (FileNotFoundException e) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    } catch (IOException e2) {
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity(walletTransactions, HttpStatus.OK);
  }

//  @GetMapping("/exodus/transactions")
//  public ResponseEntity<List<WalletTransaction>> calculateGainLostTransactions(
//          @RequestParam(value = "coin", required = true) final String coinTicker,
//          @RequestParam(value = "fiat", required = true) final String fiatCode,
//          @RequestParam(value = "transactions", required = true) final List<WalletTransaction> walletTransactions  )
//  {
//    exodusCSVservice.calculateGainLostTransactions( walletTransactions, fiatCode, coinTicker);
//
//    return new ResponseEntity<List<WalletTransaction>>(walletTransactions, HttpStatus.OK);
//  }

}
