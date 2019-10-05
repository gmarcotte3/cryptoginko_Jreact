package com.marcotte.blockhead.datastore;

import com.marcotte.blockhead.about.AboutInfo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Api(value = "Block data store", tags = "datastore")
@RestController
@RequestMapping("/datastore")
public class BlockchainAddressStoreController
{

    private static final Logger log = LoggerFactory.getLogger(BlockchainAddressStoreController.class);
    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;

    @GetMapping("")
    public ResponseEntity<List<BlockchainAddressStore>> findAll()
    {
        List<BlockchainAddressStore> blockchainAddressStores = new ArrayList<>();
        blockchainAddressStores = blockchainAddressStoreService.findAll();
        return new ResponseEntity<List<BlockchainAddressStore>>(blockchainAddressStores, HttpStatus.OK);
    }

    @PutMapping("/address")
    public @ResponseBody
    ResponseEntity<BlockchainAddressStore> putCryptoAddressesById(
            @RequestBody BlockchainAddressStore cryptoAddress) throws Exception
    {
        blockchainAddressStoreService.save(cryptoAddress);
        return new ResponseEntity<BlockchainAddressStore>(cryptoAddress, HttpStatus.OK);
    }

    @DeleteMapping("/address")
    public @ResponseBody  ResponseEntity<BlockchainAddressStore> deleteAddresses(
            @RequestBody BlockchainAddressStore addressStore) throws Exception
    {
        blockchainAddressStoreService.delete(addressStore);
        return new ResponseEntity<BlockchainAddressStore>(addressStore, HttpStatus.OK);
    }
}
