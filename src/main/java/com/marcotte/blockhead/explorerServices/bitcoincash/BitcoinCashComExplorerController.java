package com.marcotte.blockhead.explorerServices.bitcoincash;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.model.BlockchainTransactionList;
import com.marcotte.blockhead.model.CoinCodes;
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
public class BitcoinCashComExplorerController
{
    private static final Logger log = LoggerFactory.getLogger(BitcoinCashComExplorerController.class);


    @Autowired
    private BitcoinCashComExplorerService bitcoinCashComExplorerService;

    @GetMapping("/bitcoincash")
    public @ResponseBody
    ResponseEntity<BlockchainAddressStore> getBitcoinCachAddressInfo(
            @RequestParam(value = "address", required = true) final String address
    )
    {
        BlockchainAddressStore blockchainAddressStore = new BlockchainAddressStore();
        blockchainAddressStore.setAddress(address);
        blockchainAddressStore.setTicker(CoinCodes.BITCOINCASH_TICKER);

        bitcoinCashComExplorerService.addressInfo(blockchainAddressStore);

        return new ResponseEntity<BlockchainAddressStore>(blockchainAddressStore, HttpStatus.OK);
    }

    @GetMapping("/bitcoincash/transactions")
    public @ResponseBody
    ResponseEntity<BlockchainTransactionList> getBitcoinCashTransactions(
            @RequestParam(value = "address", required = false) final String address
    )
    {
        BlockchainAddressStore blockchainAddressStore = new BlockchainAddressStore();
        blockchainAddressStore.setAddress(address);
        blockchainAddressStore.setTicker(CoinCodes.BITCOINCASH_TICKER);

        BlockchainTransactionList transactionList = new BlockchainTransactionList();
        transactionList.setAddressStore(blockchainAddressStore);

        bitcoinCashComExplorerService.addressTransactions(transactionList);

        return new ResponseEntity<BlockchainTransactionList>(transactionList, HttpStatus.OK);
    }
}