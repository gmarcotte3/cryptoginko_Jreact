package com.marcotte.blockhead.model.wallet;

import java.util.Comparator;

/**
 * returns the compare between two WalletDTO based on the comparison of fiat value between the two.
 * since we are only interested in the order based on value any of the fiat values can be used, so
 * we are arbitrarily using the USD values for comparison.
 */
public class WalletDTOCompareByFiatValue implements Comparator<WalletDTO> {
    @Override
    public int compare(WalletDTO o1, WalletDTO o2) {
        int result = 0;
        Double o1value = o1.getFiat_balances().findFiat("USD").getValue();
        Double o2value = o2.getFiat_balances().findFiat("USD").getValue();
        if ( o1value < o2value ) result = -1;
        if ( o1value > o2value ) result = 1;

        return  result;
    }
}
