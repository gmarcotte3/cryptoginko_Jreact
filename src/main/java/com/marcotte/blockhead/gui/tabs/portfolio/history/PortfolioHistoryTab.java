/*
 * Copyright (c) 2021. tranquilitySoftware
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcotte.blockhead.gui.tabs.portfolio.history;

import com.marcotte.blockhead.gui.ApplicationServicesBean;
import javax.swing.*;
import java.awt.*;

/**
 * Portfolio history tab
 */
public class PortfolioHistoryTab extends JPanel {
    private ApplicationServicesBean applicationServicesBean;


    public PortfolioHistoryTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;
        createUI();
    }

    private void createUI() {

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Summary" , null, new HistorySummaryTab(applicationServicesBean), "Portfolio History Summary");
        tabbedPane.addTab("Detail" , null, new HistoryDetailTab(applicationServicesBean), "Portfolio History detail");

        setLayout( new BorderLayout());
        add( tabbedPane, BorderLayout.CENTER);

    }

}
