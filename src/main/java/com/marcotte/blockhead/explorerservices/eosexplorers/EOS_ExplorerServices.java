package com.marcotte.blockhead.explorerservices.eosexplorers;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EOS_ExplorerServices
{
    @Autowired
    private Eos_eosnewyork_io_Service eos_eosnewyork_io_service;

    public boolean addressInfo( List<BlockchainAddressStore> blockchainAddressStores)
    {
        return eos_eosnewyork_io_service.addressInfo(blockchainAddressStores);
    }

    public boolean addressInfo( BlockchainAddressStore blockchainAddressStore)
    {
        return eos_eosnewyork_io_service.addressInfo(blockchainAddressStore);
    }
}
