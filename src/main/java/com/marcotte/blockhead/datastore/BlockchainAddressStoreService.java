package com.marcotte.blockhead.datastore;

import com.marcotte.blockhead.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
     * gets all the addresstores of a given crypto currency sorted by wallet
     * accumilate balance by wallet and over all and returns this summary
     *
     * @param cryptoName
     * @return
     */
    public WalletList summarizeAddressStoreByCoinNameAndWalletName(String cryptoName)
    {
        WalletList walletList = new WalletList();
        walletList.setCryptoName(cryptoName);

        List<BlockchainAddressStore> addressStores =
            blockchainAddressStoreRepository.findBlockchainAddressStoreBycurrencyAndNextId(cryptoName, null);

        addressStores.sort( new Comparator<BlockchainAddressStore>() {
            @Override
            public int compare(BlockchainAddressStore lhs, BlockchainAddressStore rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getWalletName().compareToIgnoreCase(rhs.getWalletName());
            }
        });

        if ( addressStores != null) {
            Wallet currentWallet = new Wallet();
            currentWallet.setCryptoName(cryptoName);
            currentWallet.setWalletName( addressStores.get(0).getWalletName());
            walletList.addWallet(currentWallet);

            for (BlockchainAddressStore addr: addressStores )
            {
                if ( currentWallet.getWalletName().compareToIgnoreCase(addr.getWalletName()) != 0)
                {
                    walletList.addBalance(currentWallet.getBalance());
                    currentWallet = new Wallet();
                    currentWallet.setCryptoName(cryptoName);
                    currentWallet.setWalletName( addr.getWalletName());
                    walletList.addWallet(currentWallet);
                }
                currentWallet.addAddressStores(addr);
                currentWallet.addBalance(addr.getLastBalance());
            }
            // save the last wallet.
            walletList.addBalance(currentWallet.getBalance());

        }
        return walletList;
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

    public List<BlockchainAddressStore> findByAddressAndNextId( String address, String currency, Long nextId )
    {
        return blockchainAddressStoreRepository.findBlockchainAddressStoreByAddressAndCurrencyAndNextId(address,currency, nextId);
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
