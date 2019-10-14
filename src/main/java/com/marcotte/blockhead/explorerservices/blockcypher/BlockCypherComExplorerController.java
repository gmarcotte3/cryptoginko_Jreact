package com.marcotte.blockhead.explorerservices.blockcypher;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.CryptoNames;
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
public class BlockCypherComExplorerController
{
    private static final Logger log = LoggerFactory.getLogger(BlockCypherComExplorerController.class);

    @Autowired
    private BlockCypherComService blockCypherComService;

    @GetMapping("/blockcyphercom/{crypto}")
    public @ResponseBody
    ResponseEntity<BlockchainAddressStore> getAddressInfo( @PathVariable String crypto,
            @RequestParam(value = "address", required = false) final String address
    )
    {
        List<String> messages = new ArrayList<>();

        BlockchainAddressStore blockchainAddressStore = new BlockchainAddressStore();
        blockchainAddressStore.setAddress(address);
        blockchainAddressStore.setCurrency(crypto.toUpperCase());

        blockCypherComService.addressInfo(blockchainAddressStore);

        return new ResponseEntity<BlockchainAddressStore>(blockchainAddressStore, HttpStatus.OK);
    }

}
