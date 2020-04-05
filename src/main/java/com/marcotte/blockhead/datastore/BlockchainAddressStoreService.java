package com.marcotte.blockhead.datastore;

import com.marcotte.blockhead.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlockchainAddressStoreService
{
    private static final Logger log = LoggerFactory.getLogger(BlockchainAddressStoreService.class);

    @Autowired
    private BlockchainAddressStoreRepository blockchainAddressStoreRepository;

    public void save( BlockchainAddressStore blockchainAddressStore)
    {
        blockchainAddressStoreRepository.save(blockchainAddressStore);
    }

    public void saveWithHistory( BlockchainAddressStore blockchainAddressStore)
    {
        BlockchainAddressStore lastAddressStore = hasCoinBalanceChangedSinceLastSave( blockchainAddressStore );
        if ( lastAddressStore != null ) {
            blockchainAddressStore.setId(null);
            blockchainAddressStore.setNextId(null);
            blockchainAddressStoreRepository.save(blockchainAddressStore);

            lastAddressStore.setNextId(blockchainAddressStore.getId() );
            blockchainAddressStoreRepository.save(lastAddressStore);
        } else {
            blockchainAddressStoreRepository.save(blockchainAddressStore);
        }
    }

    private BlockchainAddressStore hasCoinBalanceChangedSinceLastSave(BlockchainAddressStore newAddressStore )
    {
        BlockchainAddressStore lastAddressStore = findLatestByAddress( newAddressStore.getAddress() );
        if ( lastAddressStore != null )
        {
            if (!Utils.almostEqual(lastAddressStore.getLastBalance(), newAddressStore.getLastBalance())) {
                return lastAddressStore;
            }
        }
        return null;
    }

    public List<BlockchainAddressStore> findAll()
    {
        List<BlockchainAddressStore> results = new ArrayList<>();
        for (BlockchainAddressStore blockchainAddressStore : blockchainAddressStoreRepository.findAll())
        {
            results.add(blockchainAddressStore);
        }
        return results;
    }

    public List<BlockchainAddressStore> findAllByCoinName( String coinname)
    {
        List<BlockchainAddressStore> results = blockchainAddressStoreRepository.findBlockchainAddressStoreBycurrencyAndNextId(coinname, null);
        if ( results != null) {
            return results;
        }
        return  new ArrayList<BlockchainAddressStore>();
    }

    public List<BlockchainAddressStore> findAllByCoinNameAndWalletName( String coinname, String walletName)
    {
        List<BlockchainAddressStore> results = blockchainAddressStoreRepository
            .findBlockchainAddressStoreBycurrencyAndWalletNameAndNextId(coinname, walletName, null);
        if ( results != null) {
            return results;
        }
        return  new ArrayList<BlockchainAddressStore>();
    }

    public List<BlockchainAddressStore> findByAddress(String address)
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByAddress(address);
    }

    public void delete( BlockchainAddressStore blockchainAddressStore)
    {
        blockchainAddressStoreRepository.delete(blockchainAddressStore);
    }

    public BlockchainAddressStore findLatestByAddress( String address )
    {
        List<BlockchainAddressStore> latestList =  blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndNextId(address, null);
        if (latestList != null && !latestList.isEmpty())
        {
            return latestList.get(0);
        }
        return null;
    }

    public List<BlockchainAddressStore> findByAddressAndNextId( String address, Long nextId )
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndNextId(address, nextId);
    }

    public List<BlockchainAddressStore> findAllLatest( String address, Long nextId )
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByNextId(null);
    }

    public CoinList findAllByCoinNameAndWalletNameAndSummerize(String cryptoName, String walletName)
    {
        List<BlockchainAddressStore> blockchainAddressStores;
        blockchainAddressStores = findAllByCoinNameAndWalletName(cryptoName.toUpperCase(), walletName.toUpperCase());

        CoinList coinlist = new CoinList();
        coinlist.setCoinName(cryptoName);
        coinlist.setCoins(blockchainAddressStores);
        coinlist.calculateCoinBalance();
        return coinlist;
    }
}
