package com.marcotte.blockhead.services.explorerServices;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BitcoinExplorerServices
{
    @Autowired
    private BlockCypherComService blockCypherComService;

    public boolean addressInfo( List<BlockchainAddressStore> blockchainAddressStores)
    {
        return blockCypherComService.addressInfo(blockchainAddressStores);
    }

    public boolean addressInfo( BlockchainAddressStore blockchainAddressStore)
    {
        return blockCypherComService.addressInfo(blockchainAddressStore);
    }
}
