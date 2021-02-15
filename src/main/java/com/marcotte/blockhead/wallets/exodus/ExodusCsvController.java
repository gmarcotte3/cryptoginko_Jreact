package com.marcotte.blockhead.wallets.exodus;


import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


import static com.marcotte.blockhead.util.ReadCSV.readCsv;


@Api(value = "Import CSV file", tags = "Import")
@RestController
@RequestMapping("/import")
public class ExodusCsvController
{
  private static final Logger log = LoggerFactory.getLogger(ExodusCsvController.class);

  @Autowired
  ExodosCsvService exodosCsvService;


  /**
   * controller to handle the reading and processing of an Exodus wallet address csv file
   *
   * This controller will accept the filename of csv file dump from exodus wallet with addresses, path,  balances
   * will check for existing address. update/insert the address including balance and save.
   *
   * each address can associated with a wallet.
   *
   * @param filename      full path filename of the csv file exported from exodus wallet (addresses)
   * @param walletName    name of the exodus wallet the addresses are associated with.
   * @return
   */
  @PutMapping("/exodus/address")
  public ResponseEntity<List<List<String>>> readAddressesFromCSVfile(String filename, String currency, String walletName)
  {
    List<List<String>> csvFileArray;

    try
    {
      csvFileArray = readCsv(filename);
      exodosCsvService.processExodusAddressDump(csvFileArray, currency.toUpperCase(), walletName.toUpperCase());

    } catch (FileNotFoundException e) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    } catch (IOException e2) {
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<List<String>>> (csvFileArray, HttpStatus.OK);
  }

}
