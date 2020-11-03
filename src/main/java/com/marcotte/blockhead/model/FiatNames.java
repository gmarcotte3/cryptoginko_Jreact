package com.marcotte.blockhead.model;

/**
 * common fiat currencies
 */
public enum FiatNames
{
    USD("USD", "US dollar"),                    // US dollar
    EUR("EUR", "Euro"),                         // Euro
    NZD("NZD", "New Zeland dollar"),            // New Zealand (Kiwi) dollar
    AUD("AUD", "Australian dollar"),            // Aussie dollar
    GRP("GRP", "British pound"),                // British pound
    JPY("JPY", "Japanese yen"),                 // Japanese yen
    JPM("JPM", "Japanese yen in 10,000s 万"),   // Japanese yen (10,000) 万
    KRW("KRW", "Korian won"),                   // Korian won
    INR("INR", "Indian ruppee");                // Indian ruppee

    public final String code;
    public final String name;

    private FiatNames(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    /**
     * convert a code into a Fiat enum
     * @param code
     * @return
     */
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




