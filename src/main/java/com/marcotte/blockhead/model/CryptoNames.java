package com.marcotte.blockhead.model;


public enum CryptoNames
{
    BITCOIN("BTC", "Bitcoin"),
    BITCOIN_CASH("BCH", "Bitcoin Cash"),
    CARDANO_ADA("ADA", "Cardano"),
    DASH("DASH", "Dash"),
    ETHEREUM("ETH", "Ethereum"),
    LITE_COIN("LTC", "Lite coin"),
    STELLAR_LUMENS_XLM("XLM", "Stellar lumens"),
    IOTA("IOT", "Iota"),
    EOS("EOS", "Eos"),
    ZCASH("ZEC", "Zcash"),
    MONERO( "XMR", "Monero"),
    LINK("LINK", "ChainLink"),
    MKR("MKR", "Maker");

    public final String code;
    public final String name;

    private CryptoNames(String code, String name)
    {
        this.code = code;
        this.name = name;
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

    public final String getCode()
    { return code; }

    public final String getName()
    { return name; }
}
