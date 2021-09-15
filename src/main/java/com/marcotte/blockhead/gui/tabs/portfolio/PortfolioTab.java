package com.marcotte.blockhead.gui.tabs.portfolio;



import com.marcotte.blockhead.gui.tabs.currentPriceTableDataModel.CurrentPriceTableDataModel;
import com.marcotte.blockhead.gui.tabs.portfolio.bycoin.PortfolioByCoinTab;
import com.marcotte.blockhead.gui.tabs.portfolio.bywallet.PortfolioByWalletTab;

import javax.swing.*;
import java.awt.*;

public class PortfolioTab extends JPanel
{
    CurrentPriceTableDataModel currentPriceTableDataModel;
    public PortfolioTab() {
        super();

        initContentPane();

    }

    private void initContentPane() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbedPane.addTab("By Coins" , null, new PortfolioByCoinTab(), "Portfolio value by coin");
        tabbedPane.addTab("By Wallet" , null, new PortfolioByWalletTab(), "Portfolio value by wallet");

        setLayout(new BorderLayout());
        add( tabbedPane, BorderLayout.CENTER);
    }
}
