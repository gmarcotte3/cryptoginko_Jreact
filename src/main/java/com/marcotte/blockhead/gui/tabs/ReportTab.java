package com.marcotte.blockhead.gui.tabs;


import com.marcotte.blockhead.gui.ApplicationServicesBean;

import javax.swing.*;

/**
 * Report Tab. this contains the output of cap gains calcs frin imported transaction files from
 * wallets.
 */
public class ReportTab extends JPanel {
    private ApplicationServicesBean applicationServicesBean;

    public ReportTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;
    }
}
