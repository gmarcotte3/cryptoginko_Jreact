package com.marcotte.blockhead.gui.tabs.CurrentPricingTab;

import com.marcotte.blockhead.gui.ApplicationServicesBean;
import com.marcotte.blockhead.model.coin.CoinDTO;


import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

/**
 * Tab for the current pricing of coins
 * this tabl will create the gui, set the data and set the format of columns of the data
 */
public class CurrentPricingTab extends JPanel
{
    ApplicationServicesBean applicationServicesBean;
    CurrentPriceTableDataModel currentPriceTableDataModel;
    public CurrentPricingTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;

        List<CoinDTO> coinList = applicationServicesBean.getCurrentPriceService().getPriceAllTrackedCoinsNow();
        currentPriceTableDataModel = new CurrentPriceTableDataModel();
        currentPriceTableDataModel.setCoinData( coinList );
        JTable table = new JTable(currentPriceTableDataModel );
        JScrollPane tabkeScrollPane = new JScrollPane(table);

        configureTableColumns(table);

        setLayout( new BorderLayout());
        add( tabkeScrollPane, BorderLayout.CENTER);
    }

    /**
     * Set the formatting for columns in the table here.
     *
     * setting perfered column widths
     * @param table
     */
    private void configureTableColumns( JTable table) {
        table.setFillsViewportHeight(true);
        TableColumn column = null;

        table.getColumnModel().getColumn(0).setPreferredWidth(50);      // ticker
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        table.getColumnModel().getColumn(1).setMaxWidth(200);           // coin name
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        table.getColumnModel().getColumn(2).setPreferredWidth(100);     // price
        table.getColumnModel().getColumn(2).setMaxWidth(350);

        table.getColumnModel().getColumn(3).setMaxWidth(50);           // fiat currency name
    }
}
