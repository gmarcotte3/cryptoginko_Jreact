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

    /**
     * save new record. insert if new update if existing.
     * @param blockchainAddressStore
     */
    public void save( BlockchainAddressStore blockchainAddressStore)
    {
        BlockchainAddressStore lastAddressStore = findLatestByAddressAndCurrency( blockchainAddressStore.getAddress(), blockchainAddressStore.getCurrency() );
        if ( lastAddressStore != null ) {
            lastAddressStore.setBlockChainAddressStore(blockchainAddressStore);
            blockchainAddressStoreRepository.save(lastAddressStore);
        } else {
            blockchainAddressStoreRepository.save(blockchainAddressStore);
        }
    }

    private BlockchainAddressStore hasCoinBalanceChangedSinceLastSave(BlockchainAddressStore newAddressStore )
    {
        BlockchainAddressStore lastAddressStore = findLatestByAddressAndCurrency( newAddressStore.getAddress(), newAddressStore.getCurrency() );
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

    /**
     * find latest addreses of the specified coin
     * @param coinname
     * @return
     */
    public List<BlockchainAddressStore> findAllByCoinName( String coinname)
    {
        List<BlockchainAddressStore> results = blockchainAddressStoreRepository.findBlockchainAddressStoreBycurrency(coinname);
        if ( results != null) {
            return results;
        }
        return  new ArrayList<BlockchainAddressStore>();
    }

    public List<BlockchainAddressStore> findAllByCoinNameAndWalletName( String coinname, String walletName)
    {
        List<BlockchainAddressStore> results = blockchainAddressStoreRepository
            .findBlockchainAddressStoreBycurrencyAndWalletName(coinname, walletName);
        if ( results != null) {
            return results;
        }
        return  new ArrayList<BlockchainAddressStore>();
    }

    public List<BlockchainAddressStore> findByAddressAndCurrency(String address, String currency)
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndCurrency(address, currency);
    }

    public void delete( BlockchainAddressStore blockchainAddressStore)
    {
        blockchainAddressStoreRepository.delete(blockchainAddressStore);
    }

    public BlockchainAddressStore findLatestByAddressAndCurrency( String address, String currency )
    {
        List<BlockchainAddressStore> latestList =  blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndCurrency(address, currency);
        if (latestList != null && !latestList.isEmpty())
        {
            return latestList.get(0);
        }
        return null;
    }

    public List<BlockchainAddressStore> findByAddressAndCoin(String address, String ticker, Long nextId )
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndCurrency(address,ticker);
    }


    /**
     * returns the  addresses ordered by the currency. we see all the addresses with coin balance and ordered by the
     * currency name.
     *
     * @return
     */
    public List<BlockchainAddressStore> findAllLatestOrderByCoin( )
    {
        List<BlockchainAddressStore> addressStores = new ArrayList<>();
        for (BlockchainAddressStore blockchainAddressStore : blockchainAddressStoreRepository.findAll())
        {
            addressStores.add(blockchainAddressStore);
        }
        addressStores.sort(new BlockchainAddressstoreComparatorCoin() );
        return addressStores;
    }

    public List<BlockchainAddressStore> findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc()
    {
        List<BlockchainAddressStore> addressStores = new ArrayList<>();
        for (BlockchainAddressStore blockchainAddressStore : blockchainAddressStoreRepository.findAll())
        {
            addressStores.add(blockchainAddressStore);
        }
        addressStores.sort(new BlockchainAddressstoreComparatorWalletCoin() );


        return addressStores;
    }

    /**
     * clears the table
     *
     */
    public void deleteAll() {
        blockchainAddressStoreRepository.deleteAll();
    }
}
