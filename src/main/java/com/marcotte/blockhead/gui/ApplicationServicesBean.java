package com.marcotte.blockhead.gui;

import com.marcotte.blockhead.services.explorerServices.pricequote.PriceServiceInterface;
import com.marcotte.blockhead.services.portfolio.CurrentPriceService;
import com.marcotte.blockhead.services.portfolio.PortFolioByWalletAndCoinService;
import com.marcotte.blockhead.services.portfolio.PortfolioByCoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServicesBean {
    static private ApplicationServicesBean instance = null;

//    @Autowired
//    private PriceServiceInterface coinGeckoService;
    @Autowired
    private PortfolioByCoinsService portfolioByCoinsService;
    @Autowired
    private PortFolioByWalletAndCoinService portFolioByWalletAndCoinService;
    @Autowired
    private com.marcotte.blockhead.services.portfolio.CurrentPriceService CurrentPriceService;

    public ApplicationServicesBean()
    {
        if ( instance == null) {
            instance = this;
        }

    }

    static public ApplicationServicesBean getInstance() {
        return instance;
    }

    public PortfolioByCoinsService getPortfolioByCoinsService() {
        return portfolioByCoinsService;
    }

    public void setPortfolioByCoinsService(PortfolioByCoinsService portfolioByCoinsService) {
        this.portfolioByCoinsService = portfolioByCoinsService;
    }


    public PortFolioByWalletAndCoinService getPortFolioByWalletAndCoinService() {
        return portFolioByWalletAndCoinService;
    }

    public CurrentPriceService getCurrentPriceService() {
        return CurrentPriceService;
    }
}