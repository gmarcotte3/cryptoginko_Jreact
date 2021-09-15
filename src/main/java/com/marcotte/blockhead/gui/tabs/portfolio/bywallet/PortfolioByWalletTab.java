package com.marcotte.blockhead.gui.tabs.portfolio.bywallet;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class PortfolioByWalletTab extends JPanel {

    PortfolioByWalletTableDataModel portfolioByWalletTableDataModel;
    public PortfolioByWalletTab() {
        super();
        portfolioByWalletTableDataModel = new PortfolioByWalletTableDataModel();
        JTable table = new JTable(portfolioByWalletTableDataModel );
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

        table.getColumnModel().getColumn(0).setPreferredWidth(150);      // wallet
        table.getColumnModel().getColumn(0).setMaxWidth(150);

        table.getColumnModel().getColumn(1).setPreferredWidth(50);      // ticker
        table.getColumnModel().getColumn(1).setMaxWidth(50);

        table.getColumnModel().getColumn(2).setMaxWidth(100);           // icon

        table.getColumnModel().getColumn(3).setPreferredWidth(150);     // coin balance
        table.getColumnModel().getColumn(3).setMaxWidth(350);

        table.getColumnModel().getColumn(4).setPreferredWidth(100);     // price
        table.getColumnModel().getColumn(4).setMaxWidth(350);

        table.getColumnModel().getColumn(5).setPreferredWidth(150);     // value
        table.getColumnModel().getColumn(5).setMaxWidth(350);

        table.getColumnModel().getColumn(6 ).setMaxWidth(50);           // fiat currency name
    }
}