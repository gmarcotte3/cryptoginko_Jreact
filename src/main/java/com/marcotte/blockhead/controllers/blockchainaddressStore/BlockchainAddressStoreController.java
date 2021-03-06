package com.marcotte.blockhead.controllers.blockchainaddressStore;

import com.marcotte.blockhead.services.blockchainaddressstore.BlockchainAddressCsvService;
import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.services.blockchainaddressstore.BlockchainAddressStoreService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    @Autowired
    private BlockchainAddressCsvService blockchainAddressCsvService;

    @GetMapping("")
    public ResponseEntity<List<BlockchainAddressStore>> findAll()
    {
        List<BlockchainAddressStore> blockchainAddressStores;
        blockchainAddressStores = blockchainAddressStoreService.findAll();
        return new ResponseEntity<List<BlockchainAddressStore>>(blockchainAddressStores, HttpStatus.OK);
    }

    @GetMapping( value = "/csv", produces = "text/csv")
    public void dumpAddresses(HttpServletResponse response ) throws IOException
    {
        List<BlockchainAddressStore> blockchainAddressStores = new ArrayList<>();
        blockchainAddressStores = blockchainAddressStoreService.findAll();
        blockchainAddressCsvService.writeAddressesToCSVStream(response.getWriter(), blockchainAddressStores);
    }

    @PutMapping("/address")
    public @ResponseBody
    ResponseEntity<BlockchainAddressStore> putCryptoAddress(
            @RequestBody BlockchainAddressStore cryptoAddress) throws Exception
    {
        blockchainAddressStoreService.save(cryptoAddress);
        return new ResponseEntity<BlockchainAddressStore>(cryptoAddress, HttpStatus.OK);
    }

    @PutMapping("/address/{cryptoName}")
    public @ResponseBody
    ResponseEntity<List<BlockchainAddressStore>> putCryptoAddressList(

            @PathVariable String cryptoName,
            @RequestParam String addressList) throws Exception
    {
        String[] addresses = addressList.split(",");
        BlockchainAddressStore cryptoAddress;
        List<BlockchainAddressStore> addressStores = new ArrayList<>();

        for (String address : addresses )
        {
            cryptoAddress = new BlockchainAddressStore();
            cryptoAddress.setAddress( address);
            cryptoAddress.setTicker(cryptoName.toUpperCase());
            cryptoAddress.setLastBalance(0.0);
            blockchainAddressStoreService.save(cryptoAddress);
            addressStores.add(cryptoAddress);
        }

        return new ResponseEntity<List<BlockchainAddressStore>>(addressStores, HttpStatus.OK);
    }

    @DeleteMapping("/address")
    public @ResponseBody  ResponseEntity<BlockchainAddressStore> deleteAddress(
            @RequestBody BlockchainAddressStore addressStore) throws Exception
    {
        blockchainAddressStoreService.delete(addressStore);
        return new ResponseEntity<BlockchainAddressStore>(addressStore, HttpStatus.OK);
    }

    @DeleteMapping("/address/all")
    public @ResponseBody  ResponseEntity<String> deleteAddresses() throws Exception
    {
        blockchainAddressStoreService.deleteAll();
        return new ResponseEntity<String>("address have been cleared", HttpStatus.OK);
    }
}
