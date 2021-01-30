package com.marcotte.blockhead.controllers.csv;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.BlockchainAddressStoreService;
import com.marcotte.blockhead.services.ExportCsvService;
import com.marcotte.blockhead.util.BlockHeadException;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "Export CSV file", tags = "export")
@RestController
@RequestMapping("/export/csv")
public class ExportCsvController
{
  private static final Logger log = LoggerFactory.getLogger(ExportCsvController.class);
  @Autowired
  private BlockchainAddressStoreService blockchainAddressStoreService;

  @Autowired
  private ExportCsvService exportCsvService;


  @GetMapping( value = "/addresses", produces = "application/json")
  public ResponseEntity<List<BlockchainAddressStore>> dumpAddresses(HttpServletResponse response,
                                                                    @RequestParam String fileName)
  {
    List<BlockchainAddressStore> blockchainAddressStores = new ArrayList<>();

    try
    {
      blockchainAddressStores = blockchainAddressStoreService.findAll();
      exportCsvService.writeAddressesByCurencyWalletDetailToCSVStream(fileName, blockchainAddressStores);
    } catch (BlockHeadException e) {
      log.error("failed to export file (%s) error==%s",fileName, e.getMessage());
      return new ResponseEntity<List<BlockchainAddressStore>> (blockchainAddressStores, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<BlockchainAddressStore>> (blockchainAddressStores, HttpStatus.OK);
  }

  @GetMapping( value = "/addresses/summary", produces = "application/json")
  public ResponseEntity<List<BlockchainAddressStore>> dumpAddressesSummary(HttpServletResponse response,
                                                                           @RequestParam String fileName)
  {
    List<BlockchainAddressStore> blockchainAddressStores = new ArrayList<>();
    List<BlockchainAddressStore> summary = new ArrayList<>();

    try
    {
      blockchainAddressStores = blockchainAddressStoreService.findAll();
      summary = exportCsvService.writeAddressesByCurencyWalletSummaryToCSVStream(fileName, blockchainAddressStores);
    } catch (BlockHeadException e) {
      log.error("failed to export file (%s) error==%s",fileName, e.getMessage());
      return new ResponseEntity<List<BlockchainAddressStore>> (summary, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<BlockchainAddressStore>> (summary, HttpStatus.OK);
  }
}
