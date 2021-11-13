package com.marcotte.blockhead.datastore.blockchainaddressstore;

import com.marcotte.blockhead.model.coin.CoinSumDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface BlockchainAddressStoreRepository extends CrudRepository< BlockchainAddressStore, Long>
{
    List<BlockchainAddressStore> findBlockchainAddressStoreByAddressAndTicker(String address, String ticker);
    List<BlockchainAddressStore> findBlockchainAddressStoreByTicker(String ticker);
    List<BlockchainAddressStore> findBlockchainAddressStoreByTickerAndWalletName(String ticker, String walletName);

    @Query(nativeQuery = true, value =
		"SELECT bas.ticker as ticker, sum(bas.last_Balance) as coinBalance " +
                    "FROM Blockchain_Address_Store bas " +
                    "GROUP BY ticker")
    List<Object> findAllLatestSumBalanceGroupByTicker( );

    @Query(nativeQuery = true, value =
            "SELECT  wallet_Name as walletName, ticker, sum(last_Balance) as lastBalance " +
                    "FROM Blockchain_Address_Store " +
                    "GROUP BY wallet_name,ticker")
    List<BlockchainAddressStore> findAllLatestSumBalanceGroupByWalletTicker( );

    @Query(nativeQuery = true, value =
            "SELECT ticker, sum(last_Balance) as lastBalance, wallet_Name as walletName " +
                    "FROM Blockchain_Address_Store " +
                    "GROUP BY wallet_name,ticker " +
                    "Order by Wallet_name, ticker")
    List<Object> findAllLatestSumBalanceGroupByWalletTicker2( );

    @Query(nativeQuery = true, value = "select distinct ticker from blockchain_address_store order by ticker")
    List<String> findAllTickers();
}
