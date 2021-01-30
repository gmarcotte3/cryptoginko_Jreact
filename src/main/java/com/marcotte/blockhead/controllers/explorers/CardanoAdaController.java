package com.marcotte.blockhead.controllers.explorers;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.model.CoinCodes;
import com.marcotte.blockhead.services.explorerServices.CardanoAdaService;
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
public class CardanoAdaController
{
    private static final Logger log = LoggerFactory.getLogger(CardanoAdaController.class);



    @Autowired
    private CardanoAdaService cardanoAdaService;

    @GetMapping("/ada")
    public @ResponseBody
    ResponseEntity<BlockchainAddressStore> getBitcoinCachAddressInfo(
            @RequestParam(value = "address", required = true) final String address
    )
    {
        BlockchainAddressStore blockchainAddressStore = new BlockchainAddressStore();
        blockchainAddressStore.setAddress(address);
        blockchainAddressStore.setTicker(CoinCodes.CARDANO_ADA_TICKER);

        cardanoAdaService.addressInfo(blockchainAddressStore);

        return new ResponseEntity<BlockchainAddressStore>(blockchainAddressStore, HttpStatus.OK);
    }


}
