package com.marcotte.blockhead.services.blockchainaddressstore;

import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStoreRepository;
import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressstoreComparatorCoin;
import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressstoreComparatorWalletCoin;
import com.marcotte.blockhead.model.coin.CoinSumDTO;
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
        BlockchainAddressStore lastAddressStore = findLatestByAddressAndCurrency( blockchainAddressStore.getAddress(), blockchainAddressStore.getTicker() );
        if ( lastAddressStore != null ) {
            lastAddressStore.setBlockChainAddressStore(blockchainAddressStore);
            blockchainAddressStoreRepository.save(lastAddressStore);
        } else {
            blockchainAddressStoreRepository.save(blockchainAddressStore);
        }
    }

    private BlockchainAddressStore hasCoinBalanceChangedSinceLastSave(BlockchainAddressStore newAddressStore )
    {
        BlockchainAddressStore lastAddressStore = findLatestByAddressAndCurrency( newAddressStore.getAddress(), newAddressStore.getTicker() );
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
        List<BlockchainAddressStore> results = blockchainAddressStoreRepository.findBlockchainAddressStoreByTicker(coinname);
        if ( results != null) {
            return results;
        }
        return  new ArrayList<BlockchainAddressStore>();
    }

    public List<BlockchainAddressStore> findAllByCoinNameAndWalletName( String coinname, String walletName)
    {
        List<BlockchainAddressStore> results = blockchainAddressStoreRepository
            .findBlockchainAddressStoreByTickerAndWalletName(coinname, walletName);
        if ( results != null) {
            return results;
        }
        return  new ArrayList<BlockchainAddressStore>();
    }

    public List<BlockchainAddressStore> findByAddressAndCurrency(String address, String ticker)
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndTicker(address, ticker);
    }

    public void delete( BlockchainAddressStore blockchainAddressStore)
    {
        blockchainAddressStoreRepository.delete(blockchainAddressStore);
    }

    public BlockchainAddressStore findLatestByAddressAndCurrency( String address, String ticker )
    {
        List<BlockchainAddressStore> latestList =  blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndTicker(address, ticker);
        if (latestList != null && !latestList.isEmpty())
        {
            return latestList.get(0);
        }
        return null;
    }

    public List<BlockchainAddressStore> findByAddressAndCoin(String address, String ticker )
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndTicker(address,ticker);
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


    public List<BlockchainAddressStore> findAllLatestSumBalanceGroupByWalletTicker() {
        return  blockchainAddressStoreRepository.findAllLatestSumBalanceGroupByWalletTicker( );
    }

    /**
     * find the latest coin balance sums by ticker (sum up by ticker)
     * @return
     */
    public  List<CoinSumDTO> findAllLatestSumBalanceGroupByTicker() {
        List<Object> rawObjects = blockchainAddressStoreRepository.findAllLatestSumBalanceGroupByTicker( );
        List<CoinSumDTO> coinSumDTOS = new ArrayList<>();
        for ( Object rawcolumns : rawObjects ) {
            CoinSumDTO coin = new CoinSumDTO((Object[]) rawcolumns);
            coinSumDTOS.add(coin);
        }
        return coinSumDTOS;
    }

}
