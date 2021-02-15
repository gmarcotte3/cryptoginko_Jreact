package com.marcotte.blockhead.datastore.blockchainaddressstore;

import java.util.Comparator;

public class BlockchainAddressstoreComparatorCoin implements Comparator<BlockchainAddressStore> {
    @Override
    public int compare(BlockchainAddressStore o1, BlockchainAddressStore o2) {

        return o1.getTicker().compareToIgnoreCase(o2.getTicker());
    }
}
