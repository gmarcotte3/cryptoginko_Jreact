package com.marcotte.blockhead.gui.tabs.portfolio;



import com.marcotte.blockhead.gui.ApplicationServicesBean;
import com.marcotte.blockhead.gui.tabs.CurrentPricingTab.CurrentPriceTableDataModel;
import com.marcotte.blockhead.gui.tabs.portfolio.bycoin.PortfolioByCoinTab;
import com.marcotte.blockhead.gui.tabs.portfolio.bycoinpie.PortfolioByCoinPiechartTab;
import com.marcotte.blockhead.gui.tabs.portfolio.bywallet.PortfolioByWalletTab;
import com.marcotte.blockhead.gui.tabs.portfolio.history.PortfolioHistoryTab;

import javax.swing.*;
import java.awt.*;

/**
 * portfolioTab
 * Overall tab that is devided in to sub tab panels.
 * Sub Panels:
 *      portfolio by coin
 *      portfolio by wallet.
 */
public class PortfolioTab extends JPanel
{
    private ApplicationServicesBean applicationServicesBean;            // back-end support
    private CurrentPriceTableDataModel currentPriceTableDataModel;      // data model.

    /**
     * constructor.
     * @param applicationServicesBean
     */
    public PortfolioTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;
        initContentPane();

    }

    /**
     * create the UI
     */
    private void initContentPane() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addTab("By CoinPie" , null, new PortfolioByCoinPiechartTab(applicationServicesBean), "Portfolio value by Coin piechart");
        tabbedPane.addTab("By Coins" , null, new PortfolioByCoinTab(applicationServicesBean), "Portfolio value by coin");
        tabbedPane.addTab("By Wallet" , null, new PortfolioByWalletTab(applicationServicesBean), "Portfolio value by wallet");
        tabbedPane.addTab("History" , null, new PortfolioHistoryTab(applicationServicesBean), "Portfolio History");

        setLayout(new BorderLayout());
        add( tabbedPane, BorderLayout.CENTER);
    }
}
