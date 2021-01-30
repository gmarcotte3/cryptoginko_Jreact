package com.marcotte.blockhead.services.explorerServices.dashExplorers;


import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashExplorerServices
{
    @Autowired
    private ExplorerDashOrg explorerDashOrg;

    public boolean addressInfo( List<BlockchainAddressStore> blockchainAddressStores)
    {
        return explorerDashOrg.addressInfo(blockchainAddressStores);
    }

    public boolean addressInfo( BlockchainAddressStore blockchainAddressStore)
    {
        return explorerDashOrg.addressInfo(blockchainAddressStore);
    }
}
