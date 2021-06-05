package com.marcotte.blockhead.model.fiat;

import java.util.ArrayList;
import java.util.List;

/**
 * this structures stores a list of fiat currencies prices or values.
 * This list contains only the most popular of the fiat currencies used around the world.
 *
 * Currently support US dollar, New Zealand dollar, Aussie dollar, Japanese yet, British pound, Euro, South Korean won, Indian rupee
 * (not supporting china or russian money -- no commies here :(  )
 * and crypto currencies: BTC, ETH
 */
public class FiatCurrencyList {

    List<FiatCurrency> fiat_values;

    /**
     * constructor, initialize the list of most popular fiat currencies.
     */
    public FiatCurrencyList() {
        this.fiat_values = new ArrayList<>();
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.USD));  // 0  US dollar
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.NZD));  // 1  Kiwi dollar
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.JPY));  // 2  Japanese Yen
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.JPM));  // 3  Japanese Yen x 10000 (万 ) <- easier to read large numbers
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.AUD));  // 4  Aussie Dollar
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.GBP));  // 5  British Pound
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.EUR));  // 6  Euro
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.INR));  // 7  Indian rupee
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.KRW));  // 8  Korean wan
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.BTC));  // 9  bitcoin
        this.fiat_values.add ( new FiatCurrency(0.0, FiatNames.ETH));  //10  Etherium
    }

    /**
     * quick way to find a particular currency price/value in this class
     *
     * @param fiatCode The FiatNames.xx code
     * @return
     */
    public FiatCurrency findFiat( String fiatCode ) {
        for( int j = 0; j < this.fiat_values.size(); j++) {
            if ( this.fiat_values.get(j).getCode().equals(fiatCode)) {
                return this.fiat_values.get(j);
            }
        }
        return new FiatCurrency(); // return a dummy
    }

    /**
     * change the fiat price/value of a particular currency in this class
     * @param fiatPrice
     */
    public void setFiat( FiatCurrency fiatPrice) {
        for( int j = 0; j < this.fiat_values.size(); j++) {
            if ( this.fiat_values.get(j).getCode().equals(fiatPrice.getCode())) {
                this.fiat_values.get(j).setValue(fiatPrice.getValue());
                return;
            }
        }
        return;
    }

    /**
     * add fiat value to a particular currency in this class.
     * @param fiatCurrency
     */
    public void addToFiat( FiatCurrency fiatCurrency) {
        for( int j = 0; j < this.fiat_values.size(); j++) {
            if ( this.fiat_values.get(j).getCode().equals(fiatCurrency.getCode())) {
                this.fiat_values.get(j).setValue(this.fiat_values.get(j).getValue() + fiatCurrency.getValue());
                return;
            }
        }
        return;
    }

    /**
     * getter: returns the list of all fiat values of this class
     * @return
     */
    public List<FiatCurrency>  getFiat_values() {
        return fiat_values;
    }

    /**
     * setter: - sets the list of fiat currency list of this class
     * @param fiat_values
     */
    public void setFiat_values(List<FiatCurrency>  fiat_values) {
        this.fiat_values = fiat_values;
    }

    /**
     * simple text output in a sudo json format, used for logging and debugging.
     * @return
     */
    public String toString() {
        StringBuffer strOut = new StringBuffer();
        String prefix = "";
        strOut.append("{ fiatCurrencyList: {");
        for ( FiatCurrency fiatCurrency : fiat_values ) {
            strOut.append( prefix + fiatCurrency.toString() );
            prefix = ", ";
        }
        strOut.append("} }");
        return strOut.toString();
    }

}
