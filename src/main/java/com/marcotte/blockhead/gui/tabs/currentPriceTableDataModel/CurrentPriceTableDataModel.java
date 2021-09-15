package com.marcotte.blockhead.gui.tabs.currentPriceTableDataModel;

import com.marcotte.blockhead.model.coin.CoinDTO;
import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table data model for the current coin prices, balance and value
 *
 * TODO we likely want to limit this to just price info and maybe a link to the coins website and
 * to coin geko. The price and value thing should be moved to portfolio secton.
 */
public class CurrentPriceTableDataModel extends AbstractTableModel {
    private static final Logger log = LoggerFactory.getLogger(CurrentPriceTableDataModel.class);

    // columns
    private final String[] columnNames = {"Coin", "Icon", "Price", "fiat", "Notes" };

    //    // dummy data.
//    private Object[][] data = {
//            {"BTC", "B", "100,000.01",      "USD", "The one coint to rule them all"},
//            {"ETH", "E", "10,000.05",       "USD", "the Oshirium coin"},
//            {"ADA", "A", "50.02",           "USD", "the Eth killer"}
//    };
    private List<CoinDTO> coinData = new ArrayList<CoinDTO>();

    public void setCoinData( List<CoinDTO> coinList ) {
        this.coinData = coinList;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return coinData.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        CoinDTO coin = coinData.get(row);
        Object value = null;
        switch (col ) {
            case 0:
                value = coin.getTicker();
                break;
            case 1:
                value = coin.getTicker();
                break;
            case 2:
                //TODO look up which price we want (use member function or utility for this)
                value = coin.getPriceNZD();  // default case NZD
                break;
            case 3:
                // TODO same issue as above
                value = "NZD";
                break;
            case 4:
                // TODO provide notes link to coingeko or some other use full info
                value = coin.getCoinName();
                break;
        }

        return value;
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

    private void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();

        for (int i=0; i < numRows; i++) {
            StringBuffer line = new StringBuffer();
            line.append("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                line.append("  " + getValueAt(i,j));
            }
            log.debug(line.toString());
        }
        log.debug("--------------------------");
    }
}