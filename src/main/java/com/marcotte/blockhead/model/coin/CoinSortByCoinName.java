package com.marcotte.blockhead.model.coin;

import java.util.Comparator;

public class CoinSortByCoinName implements Comparator<CoinDTO>{
    public int compare( CoinDTO a, CoinDTO b) {
        return a.getCoinName().compareToIgnoreCase(b.getCoinName());
    }
}
