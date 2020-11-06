package com.marcotte.blockhead.model;

/**
 * TODO there are too meny and they are changing too fast. make this a table
 * initalize it with the coins below, when we do a coin price look up we should update the table
 */
public enum CryptoNames
{
    BITCOIN("BTC", "Bitcoin"),
    BITCOIN_CASH("BCH", "Bitcoin Cash"),
    CARDANO_ADA("ADA", "Cardano"),
    DASH("DASH", "Dash"),
    ETHEREUM("ETH", "Ethereum"),
    LITE_COIN("LTC", "Litecoin"),
    STELLAR_LUMENS_XLM("XLM", "Stellar lumens"),
    IOTA("MIOT", "IOTA"),
    EOS("EOS", "Eos"),
    ZCASH("ZEC", "Zcash"),
    MONERO( "XMR", "Monero"),
    LINK("LINK", "ChainLink"),
    MKR("MKR", "Maker"),
    USDT( "USDT", "Tether"),
    XRP("XRP", "XRP"),
    BNB("BNB", "Binance Coin"),
    DOT("DOT", "Polkadot"),
    BSV("BSV", "Bitcoin SV"),
    USDC("USDC", "USD Coin"),
    WBTC("WBTC", "Wrapped Bitcoin"),
    TRX("TRX", "TRON"),
    XLM("XLM", "Stellar"),
    CRO("CRO", "Crypto.com Coin"),
    XTZ("XTZ", "Tezos"),
    CDAI("CDAI", "cDAI"),
    NEO("NEO", "NEO"),
    NEM("NEM", "NEM"),
    DIA("DIA", "Dia"),
    FIL("FIL", "Filecoin"),
    BUSD("BUSD", "Binance USD"),
    VET("VET", "VeChain");




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
