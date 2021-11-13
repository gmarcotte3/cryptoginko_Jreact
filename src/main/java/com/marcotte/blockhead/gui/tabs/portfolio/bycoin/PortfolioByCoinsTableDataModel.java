package com.marcotte.blockhead.gui.tabs.portfolio.bycoin;

import com.marcotte.blockhead.model.coin.CoinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PortfolioByCoinsTableDataModel extends AbstractTableModel {
    private static final Logger log = LoggerFactory.getLogger(PortfolioByCoinsTableDataModel.class);

    // columns
    private final String[] columnNames = {"Coin", "Icon", "Coin Balance", "Price", "TotalValue", "fiat" };
    private final int NUMBER_OF_COLUMNS = 6;

    // dummy data.
    private Object[][] data
    = {
            {"BTC", "B", "10.01", "$100,000.00", "$10,000,000.10", "USD"},
            {"ETH", "E", "100.05", "$10,000.00", "$10,000,000.50", "USD"},
            {"ADA", "A", "100,000.02", "$10.00", "$10,000,000.20", "USD"}
    };

    public static final int TICKER_IDX = 0;
    public static final int COIN_NAME_IDX = 1;
    public static final int COIN_BAL_IDX = 2;
    public static final int COIN_PRICE_IDX = 3;
    public static final int TOTAL_VALUE_IDX = 4;
    public static final int TOTAL_VLUE_FIAT_TYPE_IDX = 5;

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


    public void setModelData(List<CoinDTO> coinDTOList){
        int row = 0;
        if (coinDTOList.size() > 0 ) {
            data = new Object[coinDTOList.size()][NUMBER_OF_COLUMNS];

            for (CoinDTO coin: coinDTOList ) {
                data[row][TICKER_IDX] = coin.getTicker();
                data[row][COIN_NAME_IDX] = coin.getCoinName();
                data[row][COIN_BAL_IDX] = coin.getCoinBalance().toString();
                data[row][COIN_PRICE_IDX] = coin.getPriceNZD();  //TODO use configuration to find the right fiat
                data[row][TOTAL_VALUE_IDX] = coin.getFiat_balances().findFiat("NZD").getValue().toString();
                data[row][TOTAL_VLUE_FIAT_TYPE_IDX] = "NZD";   //TODO use configuration to find the right fiat
                row++;
            }
        }

    }
}