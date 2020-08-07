package com.marcotte.blockhead.datastore;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlockchainAddressStoreRepository extends CrudRepository< BlockchainAddressStore, Long>
{
    List<BlockchainAddressStore> findBlockchainAddressStoreByAddressAndCurrency(String address, String currency);
    List<BlockchainAddressStore> findBlockchainAddressStoreByAddressAndCurrencyAndNextId(String address, String currency, Long nextId);
    List<BlockchainAddressStore> findBlockchainAddressStoreByNextId(Long nextId);
    List<BlockchainAddressStore> findBlockchainAddressStoreBycurrencyAndNextId( String currency, Long nextID);
    List<BlockchainAddressStore> findBlockchainAddressStoreBycurrencyAndWalletNameAndNextId( String currency, String walletName, Long nextID);
}
