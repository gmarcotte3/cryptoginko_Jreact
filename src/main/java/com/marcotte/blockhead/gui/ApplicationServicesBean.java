package com.marcotte.blockhead.gui;

import com.marcotte.blockhead.services.explorerServices.pricequote.PriceServiceInterface;
import com.marcotte.blockhead.services.portfolio.PortfolioByCoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServicesBean {
    static private ApplicationServicesBean instance = null;

    @Autowired
    private PriceServiceInterface coinGeckoService;
    @Autowired
    private PortfolioByCoinsService portfolioByCoinsService;

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

    public void setCoinGeckoService(PriceServiceInterface coinGeckoService) {
        this.coinGeckoService = coinGeckoService;
    }

    public PortfolioByCoinsService getPortfolioByCoinsService() {
        return portfolioByCoinsService;
    }

    public void setPortfolioByCoinsService(PortfolioByCoinsService portfolioByCoinsService) {
        this.portfolioByCoinsService = portfolioByCoinsService;
    }
}