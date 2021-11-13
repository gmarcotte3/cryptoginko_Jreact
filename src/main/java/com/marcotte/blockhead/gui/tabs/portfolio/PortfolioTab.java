package com.marcotte.blockhead.gui.tabs.portfolio;



import com.marcotte.blockhead.gui.ApplicationServicesBean;
import com.marcotte.blockhead.gui.tabs.CurrentPricingTab.CurrentPriceTableDataModel;
import com.marcotte.blockhead.gui.tabs.portfolio.bycoin.PortfolioByCoinTab;
import com.marcotte.blockhead.gui.tabs.portfolio.bywallet.PortfolioByWalletTab;

import javax.swing.*;
import java.awt.*;

public class PortfolioTab extends JPanel
{
    private ApplicationServicesBean applicationServicesBean;
    private CurrentPriceTableDataModel currentPriceTableDataModel;
    public PortfolioTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;
        initContentPane();

    }

    private void initContentPane() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbedPane.addTab("By Coins" , null, new PortfolioByCoinTab(applicationServicesBean), "Portfolio value by coin");
        tabbedPane.addTab("By Wallet" , null, new PortfolioByWalletTab(applicationServicesBean), "Portfolio value by wallet");

        setLayout(new BorderLayout());
        add( tabbedPane, BorderLayout.CENTER);
    }
}
