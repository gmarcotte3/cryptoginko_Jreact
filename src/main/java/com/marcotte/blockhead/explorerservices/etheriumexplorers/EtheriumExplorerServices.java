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

    @Autowired
    private EtherscanExplorerService etherscanExplorerService;


    /**
     * get address info from block explorers. if the first one fails try the next one.
     *
     * @param blockchainAddressStores
     * @return
     */
    public boolean addressInfo( List<BlockchainAddressStore> blockchainAddressStores)
    {
        Boolean result = etherscanExplorerService.addressInfo(blockchainAddressStores);

        if ( !result ) {
            result = blockCypherComService.addressInfo(blockchainAddressStores);
        }
        return result;
    }

    public boolean addressInfo( BlockchainAddressStore blockchainAddressStore)
    {
        return blockCypherComService.addressInfo(blockchainAddressStore);
    }
}
