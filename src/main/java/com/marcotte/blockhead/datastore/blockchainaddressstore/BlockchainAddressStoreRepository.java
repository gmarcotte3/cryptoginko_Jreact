package com.marcotte.blockhead.datastore.blockchainaddressstore;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlockchainAddressStoreRepository extends CrudRepository< BlockchainAddressStore, Long>
{
    List<BlockchainAddressStore> findBlockchainAddressStoreByAddressAndTicker(String address, String ticker);
    List<BlockchainAddressStore> findBlockchainAddressStoreByTicker(String ticker);
    List<BlockchainAddressStore> findBlockchainAddressStoreByTickerAndWalletName(String ticker, String walletName);

    @Query(nativeQuery = true, value =
            "SELECT currency, sum(lastBalance) as lastBalance " +
                    "FROM BlockchainAddressStore " +
                    "GROUP BY ticker")
    List<BlockchainAddressStore> findAllLatestSumBalanceGroupByTicker( );
}
