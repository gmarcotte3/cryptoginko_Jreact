package com.marcotte.blockhead.datastore;

import java.util.Comparator;

public class BlockchainAddressstoreComparatorWalletCoin implements Comparator<BlockchainAddressStore> {
    @Override
    public int compare(BlockchainAddressStore o1, BlockchainAddressStore o2) {
        String o2WalletCoin = o2.getWalletName() + o2.getTicker();
        String o1WalletCoin = o1.getWalletName() + o1.getTicker();
        return o1WalletCoin.compareToIgnoreCase(o2WalletCoin);
    }
}
