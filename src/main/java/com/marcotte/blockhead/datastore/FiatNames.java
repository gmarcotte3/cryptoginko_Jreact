package com.marcotte.blockhead.datastore;

/**
 * common fiat currencies
 */
public enum FiatNames
{
    USD("USD"),   // US dollar
    EUR("EUR"),   // Euro
    NZD("NZD"),   // New Zealand (Kiwi) dollar
    AUD("AUD"),   // Aussie dollar
    GRP("GRP"),   // British pound
    JPY("JPY"),   // Japanese yen
    JPM("JPM"),   // Japanese yen (10,000) 万
    KRW("KRW"),   // Korian won
    INR("INR");   // Indian ruppee

    public final String code;

    private FiatNames(String code)
    {
        this.code = code;
    }

    public static FiatNames valueOfCode(String code)
    {
        for( FiatNames fiat : values())
        {
            if ( fiat.code.equals(code))
            {
                return fiat;
            }
        }
        return null;
    }

}




