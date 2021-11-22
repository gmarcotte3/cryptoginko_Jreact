package com.marcotte.blockhead.gui;

import com.marcotte.blockhead.services.csv.GinkoCsvService;
import com.marcotte.blockhead.services.portfolio.CurrentPriceService;
import com.marcotte.blockhead.services.portfolio.PortFolioByWalletAndCoinService;
import com.marcotte.blockhead.services.portfolio.PortfolioByCoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * application service bean. This is created at system startup and passed around to the swing thread to provide access to
 * the back-end.  There are several autowired (spring frameworks) services that are loaded when the application starts.
 */
@Service
public class ApplicationServicesBean {
    static private ApplicationServicesBean instance = null;

    @Autowired
    private PortfolioByCoinsService portfolioByCoinsService;
    @Autowired
    private PortFolioByWalletAndCoinService portFolioByWalletAndCoinService;
    @Autowired
    private com.marcotte.blockhead.services.portfolio.CurrentPriceService currentPriceService;

    @Autowired
    private GinkoCsvService ginkoCsvService;        // CSV services

    public ApplicationServicesBean()
    {
        if ( instance == null) {
            instance = this;
        }

    }

    // TODO this is not needed now
    // @deprecated
    static public ApplicationServicesBean getInstance() {
        return instance;
    }

    // ================================
    // Getters and Setters
    // ================================
    public PortfolioByCoinsService getPortfolioByCoinsService() {
        return portfolioByCoinsService;
    }

    public void setPortfolioByCoinsService(PortfolioByCoinsService portfolioByCoinsService) {
        this.portfolioByCoinsService = portfolioByCoinsService;
    }

    public PortFolioByWalletAndCoinService getPortFolioByWalletAndCoinService() {
        return portFolioByWalletAndCoinService;
    }

    public void setPortFolioByWalletAndCoinService(PortFolioByWalletAndCoinService portFolioByWalletAndCoinService) {
        this.portFolioByWalletAndCoinService = portFolioByWalletAndCoinService;
    }

    public CurrentPriceService getCurrentPriceService() {
        return currentPriceService;
    }

    public void setCurrentPriceService(CurrentPriceService currentPriceService) {
        this.currentPriceService = currentPriceService;
    }

    public GinkoCsvService getGinkoCsvService() {
        return ginkoCsvService;
    }

    public void setGinkoCsvService(GinkoCsvService ginkoCsvService) {
        this.ginkoCsvService = ginkoCsvService;
    }
}