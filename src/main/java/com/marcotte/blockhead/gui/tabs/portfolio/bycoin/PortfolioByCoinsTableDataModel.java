package com.marcotte.blockhead.gui.tabs.portfolio.bycoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;

public class PortfolioByCoinsTableDataModel extends AbstractTableModel {
    private static final Logger log = LoggerFactory.getLogger(PortfolioByCoinsTableDataModel.class);

    // columns
    private final String[] columnNames = {"Coin", "Icon", "Coin Balance", "Price", "TotalValue", "fiat", "Notes" };

    // dummy data.
    private Object[][] data = {
            {"BTC", "B", "10.01", "$100,000.00", "$10,000,000.10", "USD", "The one coint to rule them all"},
            {"ETH", "E", "100.05", "$10,000.00", "$10,000,000.50", "USD", "the Oshirium coin"},
            {"ADA", "A", "100,000.02", "$10.00", "$10,000,000.20", "USD", "the Eth killer"}
    };


    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }


//    private void printDebugData() {
//        int numRows = getRowCount();
//        int numCols = getColumnCount();
//
//        for (int i=0; i < numRows; i++) {
//            StringBuffer line = new StringBuffer();
//            line.append("    row " + i + ":");
//            for (int j=0; j < numCols; j++) {
//                line.append("  " + data[i][j]);
//            }
//            log.debug(line.toString());
//        }
//        log.debug("--------------------------");
//    }
}