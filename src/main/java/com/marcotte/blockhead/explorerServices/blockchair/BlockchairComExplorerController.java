package com.marcotte.blockhead.explorerServices.blockchair;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



/**
 * supports BTC, ETH, DASH, LTC
 * BCH, ZEC does not seem to work.
 */
@Api(value = "BlockChain Explorer", tags = "blockChainExplorer")
@RestController
@RequestMapping("/explorer")
public class BlockchairComExplorerController
{
  private static final Logger log = LoggerFactory.getLogger(com.marcotte.blockhead.explorerServices.blockcypher.BlockCypherComExplorerController.class);

  @Autowired
  private BlockchairComExplorerService blockchairComExplorerService;

  @GetMapping("/blockchaircom/{crypto}")
  public @ResponseBody
  ResponseEntity<BlockchainAddressStore> getAddressInfo(@PathVariable String crypto,
                                                        @RequestParam(value = "address", required = false) final String address
  )
  {
    List<String> messages = new ArrayList<>();

    BlockchainAddressStore blockchainAddressStore = new BlockchainAddressStore();
    blockchainAddressStore.setAddress(address);
    blockchainAddressStore.setCurrency(crypto.toUpperCase());

    blockchairComExplorerService.addressInfo(blockchainAddressStore);

    return new ResponseEntity<BlockchainAddressStore>(blockchainAddressStore, HttpStatus.OK);
  }

}



