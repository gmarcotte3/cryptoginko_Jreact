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
        List<BlockchainAddressStore> latestList =  blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndCurrencyAndNextId(address, currency, null);
        if (latestList != null && !latestList.isEmpty())
        {
            return latestList.get(0);
        }
        return null;
    }

    public List<BlockchainAddressStore> findByAddressAndCoin(String address, String ticker, Long nextId )
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndCurrencyAndNextId(address,ticker, nextId);
    }


    /**
     * returns the latest addresses ordered by the currency. we see all the addresses with coin balance and ordered by the
     * currency name.
     *
     * @return
     */
    public List<BlockchainAddressStore> findAllLatestOrderByCoin( )
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByNextIdOrderByCurrency(null);
    }

    public List<BlockchainAddressStore> findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc()
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByNextIdOrderByWalletNameAscCurrencyAsc(null);
    }

    /**
     * clears the table
     *
     */
    public void deleteAll() {
        blockchainAddressStoreRepository.deleteAll();
    }
}
