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

public class HistorySummaryTab  extends JPanel {
    private ApplicationServicesBean applicationServicesBean;

    private String defaultCurency1;
    private String defaultCurency2;
    private String defaultCurency3;

    public HistorySummaryTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;

        defaultCurency1 = applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault();
        defaultCurency2 =  applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault2();
        defaultCurency3 =  applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault3();

        createUI();
    }

    /**
     * UI creation.
     */
    private void createUI() {
        setLayout( new BorderLayout());
    }

}
