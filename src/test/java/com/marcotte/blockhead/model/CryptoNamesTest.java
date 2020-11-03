package com.marcotte.blockhead.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CryptoNamesTest {

    @Test
    public void valueOfCode() {
        CryptoNames cardano = CryptoNames.valueOfCode("ADA");
        System.out.println("cardano=" + cardano.getName());
        System.out.println("cardano=" + cardano.getCode());

        assertEquals("ADA", cardano.getCode());
        assertEquals("Cardano", cardano.getName() );
    }

    @Test
    public void getCode() {
    }

    @Test
    public void getName() {
    }
}