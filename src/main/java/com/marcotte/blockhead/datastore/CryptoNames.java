package com.marcotte.blockhead.datastore;


public enum CryptoNames
{
    BITCOIN("BTC"),
    BITCOIN_CASH("BCH"),
    CARDANO_ADA("ADA"),
    DASH("DASH"),
    ETHEREUM("ETH"),
    LITE_COIN("LTC"),
    STELLAR_LUMENS_XLM("XLM"),
    IOTA("IOT"),
    EOS("EOS"),
    ZCASH("ZEC");

    public final String code;

    private CryptoNames(String code)
    {
        this.code = code;
    }

    public static CryptoNames valueOfCode(String code)
    {
        for( CryptoNames crypto : values())
        {
            if ( crypto.code.equals(code))
            {
                return crypto;
            }
        }
        return null;
    }
}
