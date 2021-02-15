package com.marcotte.blockhead.controllers.explorers;

import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.model.coin.CoinCodes;
import com.marcotte.blockhead.services.explorerServices.etheriumexplorers.EtheriumExplorerServices;
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
public class EtheriumExplorerController
{
    private static final Logger log = LoggerFactory.getLogger(EtheriumExplorerController.class);

    @Autowired
    private EtheriumExplorerServices etheriumExplorerServices;

    @GetMapping("/ETH")
    public @ResponseBody
    ResponseEntity<BlockchainAddressStore> getAddressInfo(
            @RequestParam(value = "address", required = false) final String address
    )
    {
        List<String> messages = new ArrayList<>();

        BlockchainAddressStore blockchainAddressStore = new BlockchainAddressStore();
        blockchainAddressStore.setAddress(address);
        blockchainAddressStore.setTicker(CoinCodes.ETHEREUM_TICKER);

        etheriumExplorerServices.addressInfo(blockchainAddressStore);

        return new ResponseEntity<BlockchainAddressStore>(blockchainAddressStore, HttpStatus.OK);
    }

}
