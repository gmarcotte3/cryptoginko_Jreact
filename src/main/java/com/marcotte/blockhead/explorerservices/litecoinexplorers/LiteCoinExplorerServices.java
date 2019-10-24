package com.marcotte.blockhead.explorerservices.litecoinexplorers;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LiteCoinExplorerServices
{
    @Autowired
    private ChainzCryptoidInfo_blockexplorer chainzCryptoidInfo_blockexplorer;


    public boolean addressInfo( List<BlockchainAddressStore> blockchainAddressStores)
    {
        return chainzCryptoidInfo_blockexplorer.addressInfo(blockchainAddressStores);
    }

    public boolean addressInfo( BlockchainAddressStore blockchainAddressStore)
    {
        return chainzCryptoidInfo_blockexplorer.addressInfo(blockchainAddressStore);
    }
}
