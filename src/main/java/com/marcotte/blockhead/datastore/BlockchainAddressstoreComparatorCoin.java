package com.marcotte.blockhead.datastore;

import java.util.Comparator;

public class BlockchainAddressstoreComparatorCoin implements Comparator<BlockchainAddressStore> {
    @Override
    public int compare(BlockchainAddressStore o1, BlockchainAddressStore o2) {

        return o1.getCurrency().compareToIgnoreCase(o2.getCurrency());
    }
}
