package com.marcotte.blockhead.gui;

import com.marcotte.blockhead.services.explorerServices.pricequote.PriceServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
public class ApplicationServicesBean {
    static private ApplicationServicesBean instance = null;

    //    @Autowired
    private PriceServiceInterface coinGeckoService;

    public ApplicationServicesBean()
    {
        if ( instance == null) {
            instance = this;
        }

    }

    static public ApplicationServicesBean getInstance() {
        return instance;
    }

    public PriceServiceInterface getCoinGeckoService() {
        return coinGeckoService;
    }
}