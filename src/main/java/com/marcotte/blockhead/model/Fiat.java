package com.marcotte.blockhead.model;

/**
 * this class represents currency by degree (fiat) issued a government
 * It has value because they say so.
 */
public class Fiat {
    private Double value;
    private FiatNames fiatType;

    public Fiat() {
        this.value = 0.0;
        this.fiatType = FiatNames.USD;
    }
    public Fiat(Double value, FiatNames fiatType) {
        this.value = value;
        this.fiatType = fiatType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public FiatNames getFiatType() {
        return fiatType;
    }

    public void setFiatType(FiatNames fiatType) {
        this.fiatType = fiatType;
    }
}
