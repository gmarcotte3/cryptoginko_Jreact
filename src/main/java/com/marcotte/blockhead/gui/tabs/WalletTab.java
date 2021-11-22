package com.marcotte.blockhead.gui.tabs;

import com.marcotte.blockhead.gui.ApplicationServicesBean;

import javax.swing.*;

/**
 * wallet tab, used to create new wallets and do maintance functions.,
 */
public class WalletTab extends JPanel {
    private ApplicationServicesBean applicationServicesBean;
    public WalletTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;
    }
}