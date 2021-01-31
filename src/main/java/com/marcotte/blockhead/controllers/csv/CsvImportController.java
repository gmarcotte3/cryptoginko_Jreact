package com.marcotte.blockhead.controllers.csv;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.services.ExportCsvService;
import com.marcotte.blockhead.model.WalletTransaction;
import com.marcotte.blockhead.services.GinkoCsvService;
import com.marcotte.blockhead.services.TransationAnalysisService;
import com.marcotte.blockhead.services.cardano.CardanoCSVservice;
import com.marcotte.blockhead.services.exodus.ExodusCSVservice;
import com.marcotte.blockhead.util.BlockHeadException;
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
  private CardanoCSVservice cardanoCSVservice;

  @Autowired
  private GinkoCsvService ginkoCsvService;

  @Autowired
  private ExportCsvService exportCsvService;

  @Autowired
  private TransationAnalysisService transationAnalysisService;

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
      savedList = ginkoCsvService.processCsvAddressDump(csvFileArray);

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
      savedList = ginkoCsvService.processCsvAddressDump(csvFileArray);

      redirectAttributes.addFlashAttribute("message",
              "You successfully uploaded '" + file.getOriginalFilename() + "'");

    } catch (FileNotFoundException e) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    } catch (IOException e2) {
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity(savedList, HttpStatus.OK);
  }

  /**
   * Read Exodus transactions csv file
   *
   * This routine reads an Exodus wallet transaction export csv file. parses it into
   * a WalletTransaction structure, then process the structure to include gain/loss
   * information, saves the structure as a new csv file.
   *
   * @param file                input csv file
   * @param redirectAttributes
   * @param coinTicker          coin ticker code
   * @param fiatCode            fiat currency code
   * @param outCsvfileName      output csv file
   * @return
   */
  @PostMapping("/exodus/transactions")
  public ResponseEntity<List<WalletTransaction>> importExodusTransactionsCSVfile(
          @RequestParam("file") MultipartFile file,
          RedirectAttributes redirectAttributes,
          @RequestParam(value = "coin", required = true) final String coinTicker,
          @RequestParam(value = "fiat", required = true) final String fiatCode,
          @RequestParam String outCsvfileName) {

    List<List<String>> csvFileArray;
    List<WalletTransaction> walletTransactions = new ArrayList<>();

    if (file.isEmpty()) {
      redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    try {
      csvFileArray = readFileCsv(file);
      walletTransactions = exodusCSVservice.parseTransactionCsv(csvFileArray);
      transationAnalysisService.calculateGainLossTransactions( walletTransactions, fiatCode, coinTicker);
      exportCsvService.writeWalletTransactionsToCsv( outCsvfileName, walletTransactions);
      redirectAttributes.addFlashAttribute("message",
              "You successfully uploaded '" + file.getOriginalFilename() + "'");

    } catch (FileNotFoundException e) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    } catch (IOException e2) {
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (BlockHeadException e) {
      log.error("failed to export file (%s) error==%s",outCsvfileName, e.getMessage());
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity(walletTransactions, HttpStatus.OK);
  }

  @PostMapping("/cardano/transactions")
  public ResponseEntity<List<WalletTransaction>> importCardanoExodusTransactionsCSVfile(
          @RequestParam("file") MultipartFile file,
          RedirectAttributes redirectAttributes,
          @RequestParam(value = "coin", required = true) final String coinTicker,
          @RequestParam(value = "fiat", required = true) final String fiatCode,
          @RequestParam String outCsvfileName) {

    List<List<String>> csvFileArray;
    List<WalletTransaction> walletTransactions = new ArrayList<>();

    if (file.isEmpty()) {
      redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    try {
      csvFileArray = readFileCsv(file);
      walletTransactions = cardanoCSVservice.parseTransactionCsv(csvFileArray);
      transationAnalysisService.calculateGainLossTransactions( walletTransactions, fiatCode, coinTicker);
      exportCsvService.writeWalletTransactionsToCsv( outCsvfileName, walletTransactions);
      redirectAttributes.addFlashAttribute("message",
              "You successfully uploaded '" + file.getOriginalFilename() + "'");

    } catch (FileNotFoundException e) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    } catch (IOException e2) {
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (BlockHeadException e) {
      log.error("failed to export file (%s) error==%s",outCsvfileName, e.getMessage());
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity(walletTransactions, HttpStatus.OK);
  }

}
