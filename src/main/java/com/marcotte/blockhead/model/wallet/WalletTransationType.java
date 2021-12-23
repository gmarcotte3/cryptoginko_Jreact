/*
 * Copyright (c) 2021. tranquilitySoftware
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcotte.blockhead.model.wallet;

public enum WalletTransationType {

    BUY_COINS("BUYCOIN", "Coin perchase"),
    SALE_COIN( "SALECOIN", "Normal sale"),
    DEPOSIT_COIN( "DEPOSITCOIN", "Deposit coin from anotehr wallet, from existing coins"),
    REMIT_COIN( "REMITCOIN", "Remittance, Sending coins to family or friends for no consideration"),
    LOST_COIN( "LOSTCOIN", "Lost coins or hacked coins"),
    DONATION_COIN("DONATE", "donations of coins"),
    WTF_COIN("WTFCOIN", "Unknown coin transaction");

    public final String code;
    public final String description;

    private WalletTransationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * convert a code into a Fiat enum
     * @param code
     * @return
     */
    public static WalletTransationType valueOfCode(String code)
    {
        for( WalletTransationType walletTransationType : values())
        {
            if ( walletTransationType.code.equals(code.toUpperCase()))
            {
                return walletTransationType;
            }
        }
        return WTF_COIN;
    }
}

