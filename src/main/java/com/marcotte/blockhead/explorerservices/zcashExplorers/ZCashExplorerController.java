package com.marcotte.blockhead.explorerservices.zcashExplorers;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.CryptoNames;
import com.marcotte.blockhead.explorerservices.litecoinexplorers.LiteCoinExplorerServices;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Api(value = "BlockChain Explorer", tags = "blockChainExplorer")
@RestController
@RequestMapping("/explorer")
public class ZCashExplorerController
{
    private static final Logger log = LoggerFactory.getLogger(ZCashExplorerController.class);

    @Autowired
    private ZCashExplorerServices zCashExplorerServices;

    @GetMapping("/ZEC")
    public @ResponseBody
    ResponseEntity<BlockchainAddressStore> getAddressInfo(
            @RequestParam(value = "address", required = false) final String address
    )
    {
        List<String> messages = new ArrayList<>();

        BlockchainAddressStore blockchainAddressStore = new BlockchainAddressStore();
        blockchainAddressStore.setAddress(address);
        blockchainAddressStore.setCurrency(CryptoNames.ZCASH.code);

        zCashExplorerServices.addressInfo(blockchainAddressStore);

        return new ResponseEntity<BlockchainAddressStore>(blockchainAddressStore, HttpStatus.OK);
    }

}