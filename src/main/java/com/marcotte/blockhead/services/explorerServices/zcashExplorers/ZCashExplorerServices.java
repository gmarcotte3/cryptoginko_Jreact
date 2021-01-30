package com.marcotte.blockhead.services.explorerServices.zcashExplorers;


import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZCashExplorerServices
{
    @Autowired
    private ZchaInService zchaInService;

    public boolean addressInfo( List<BlockchainAddressStore> blockchainAddressStores)
    {
        return zchaInService.addressInfo(blockchainAddressStores);
    }

    public boolean addressInfo( BlockchainAddressStore blockchainAddressStore)
    {
        return zchaInService.addressInfo(blockchainAddressStore);
    }
}
