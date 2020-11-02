package com.marcotte.blockhead.explorerServices.bitcoinexplorers;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.model.CryptoNames;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Api(value = "BlockChain Explorer", tags = "blockChainExplorer")
@RestController
@RequestMapping("/explorer")
public class BitcoinExplorerController
{
    private static final Logger log = LoggerFactory.getLogger(BitcoinExplorerController.class);

    @Autowired
    private BitcoinExplorerServices bitcoinExplorerServices;

    @GetMapping("/bitcoin")
    public @ResponseBody
    ResponseEntity<BlockchainAddressStore> getBitcoinCachAddressInfo(
            @RequestParam(value = "address", required = true) final String address
    )
    {
        BlockchainAddressStore blockchainAddressStore = new BlockchainAddressStore();
        blockchainAddressStore.setAddress(address);
        blockchainAddressStore.setCurrency(CryptoNames.BITCOIN.code);

        bitcoinExplorerServices.addressInfo(blockchainAddressStore);

        return new ResponseEntity<BlockchainAddressStore>(blockchainAddressStore, HttpStatus.OK);
    }


}
