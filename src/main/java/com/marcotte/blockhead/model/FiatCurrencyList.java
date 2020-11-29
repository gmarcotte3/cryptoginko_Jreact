package com.marcotte.blockhead.model;

import java.util.ArrayList;
import java.util.List;

public class FiatCurrencyList {

    List<FiatCurrency> fiat_values;


    public FiatCurrencyList() {
        this.fiat_values = new ArrayList<>();
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.USD));  // 0
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.NZD));  // 1
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.JPY));  // 2
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.JPM));  // 3
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.AUD));  // 4
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.GBP));  // 5
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.EUR));  // 6
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.INR));  // 7
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.KRW));  // 8
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.BTC));  // 9
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.ETH));  //10
    }

    public FiatCurrency findFiat( String fiatCode ) {
        for( int j = 0; j < this.fiat_values.size(); j++) {
            if ( this.fiat_values.get(j).getCode().equals(fiatCode)) {
                return this.fiat_values.get(j);
            }
        }
        return new FiatCurrency(); // return a dummy
    }

    public void setFiat( FiatCurrency fiatPrice) {
        for( int j = 0; j < this.fiat_values.size(); j++) {
            if ( this.fiat_values.get(j).getCode().equals(fiatPrice.getCode())) {
                this.fiat_values.get(j).setValue(fiatPrice.getValue());
                return;
            }
        }
        return;
    }

    public void addToFiat( FiatCurrency fiatCurrency) {
        for( int j = 0; j < this.fiat_values.size(); j++) {
            if ( this.fiat_values.get(j).getCode().equals(fiatCurrency.getCode())) {
                this.fiat_values.get(j).setValue(this.fiat_values.get(j).getValue() + fiatCurrency.getValue());
                return;
            }
        }
        return;
    }

    public List<FiatCurrency>  getFiat_values() {
        return fiat_values;
    }

    public void setFiat_values(List<FiatCurrency>  fiat_values) {
        this.fiat_values = fiat_values;
    }

}
