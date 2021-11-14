package com.marcotte.blockhead.model.coin;

import java.util.Comparator;

public class CoinSortByCoinValue implements Comparator<CoinDTO>{
    public int compare( CoinDTO a, CoinDTO b) {
        return a.getFiat_balances().getFiat_values().get(0).getValue().compareTo( b.getFiat_balances().getFiat_values().get(0).getValue());
    }
}