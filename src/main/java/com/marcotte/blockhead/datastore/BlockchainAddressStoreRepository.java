package com.marcotte.blockhead.datastore;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlockchainAddressStoreRepository extends CrudRepository< BlockchainAddressStore, Long>
{
    List<BlockchainAddressStore> findBlockchainAddressStoreByAddress(String address);
    List<BlockchainAddressStore> findBlockchainAddressStoreByAddressAndNextId(String address, Long nextId);
    List<BlockchainAddressStore> findBlockchainAddressStoreByNextId(Long nextId);
    List<BlockchainAddressStore> findBlockchainAddressStoreBycurrencyAndNextId( String currency, Long nextID);
}
