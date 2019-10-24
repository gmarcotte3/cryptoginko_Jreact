package com.marcotte.blockhead.explorerservices.etheriumexplorers;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.explorerservices.blockcypher.BlockCypherComService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtheriumExplorerServices
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
