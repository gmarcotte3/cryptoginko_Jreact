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
import com.marcotte.blockhead.model.portfolio.CoinPriceValueTrackerDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

/**
 * This tab shows historical list of coin by date values
 */
public class HistoryDetailTab extends JPanel {
    private ApplicationServicesBean applicationServicesBean;

    private HistoryDetailTabModel historyDetailTabModel;

    private String defaultCurency1;
    private String defaultCurency2;
    private String defaultCurency3;


    public HistoryDetailTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;

        defaultCurency1 = applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault();
        defaultCurency2 =  applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault2();
        defaultCurency3 =  applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault3();

        historyDetailTabModel = new HistoryDetailTabModel();
        createUI();
    }

    /**
     * UI creation.
     */
    private void createUI() {
        JTable table = new JTable(historyDetailTabModel );
        configureTableColumns(table);
        JScrollPane tabkeScrollPane = new JScrollPane(table);


        JPanel detailPanel = new JPanel();
        detailPanel.setLayout( new BorderLayout());
        detailPanel.add( tabkeScrollPane, BorderLayout.CENTER);



        setLayout( new BorderLayout());
        add(detailPanel, BorderLayout.CENTER);

        refreashDataModel();
    }

    public void refreashDataModel() {
        List<CoinPriceValueTrackerDTO> foundCoinValueDTOs = applicationServicesBean.getCoinPriceValueTrackerService().findAllHistoricalDTOList();
        historyDetailTabModel.setModelData(foundCoinValueDTOs);
    }


    private void configureTableColumns( JTable table) {
        table.setFillsViewportHeight(true);
        TableColumn column = null;

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);      // date
        table.getColumnModel().getColumn(0).setMaxWidth(80);

        table.getColumnModel().getColumn(1).setPreferredWidth(50);      // ticker
        table.getColumnModel().getColumn(1).setMaxWidth(50);

        table.getColumnModel().getColumn(2).setMaxWidth(25);           // icon

        table.getColumnModel().getColumn(3).setPreferredWidth(150);     // coin balance
        table.getColumnModel().getColumn(3).setMaxWidth(150);

        table.getColumnModel().getColumn(4).setPreferredWidth(100);     // price
        table.getColumnModel().getColumn(4).setMaxWidth(100);
        table.getColumnModel().getColumn(4).setCellRenderer( rightRenderer );

        table.getColumnModel().getColumn(5).setPreferredWidth(150);     // value
        table.getColumnModel().getColumn(5).setMaxWidth(150);

        table.getColumnModel().getColumn(6).setMaxWidth(50);           // fiat currency name
        table.getColumnModel().getColumn(6).setCellRenderer( rightRenderer );
    }
}
