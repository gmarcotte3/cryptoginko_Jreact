package com.marcotte.blockhead.datastore;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlockchainAddressStoreRepository extends CrudRepository< BlockchainAddressStore, Long>
{
    List<BlockchainAddressStore> findBlockchainAddressStoreByAddressAndCurrency(String address, String currency);
    List<BlockchainAddressStore> findBlockchainAddressStoreBycurrency( String currency);
    List<BlockchainAddressStore> findBlockchainAddressStoreBycurrencyAndWalletName( String currency, String walletName);
    
    @Query(nativeQuery = true, value =
            "SELECT currency, sum(lastBalance) as lastBalance " +
                    "FROM BlockchainAddressStore " +
                    "GROUP BY currency")
    List<BlockchainAddressStore> findAllLatestSumBalanceGroupByCurrency( );
}
