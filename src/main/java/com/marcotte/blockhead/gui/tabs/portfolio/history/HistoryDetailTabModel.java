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

import com.marcotte.blockhead.model.fiat.FiatCurrency;
import com.marcotte.blockhead.model.fiat.FiatNames;
import com.marcotte.blockhead.model.portfolio.CoinPriceValueTrackerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class HistoryDetailTabModel extends AbstractTableModel {
    private static final Logger log = LoggerFactory.getLogger(HistoryDetailTabModel.class);

    private String defaultCurrency;

    // columns
    private final String[] columnNames = {"Date", "Coin", "Icon", "Coin Balance", "Price", "TotalValue", "fiat" };
    private final int NUMBER_OF_COLUMNS = 7;


    protected class Row {
        public Object arow[] = {"","","","","","", ""};
    }

    private List<Row> datas;

    public static final int DATE_IDX = 0;
    public static final int TICKER_IDX = 1;
    public static final int ICON_IDX = 2;
    public static final int COIN_BAL_IDX = 3;
    public static final int COIN_PRICE_IDX = 4;
    public static final int TOTAL_VALUE_IDX = 5;
    public static final int TOTAL_VLUE_FIAT_TYPE_IDX = 6;

    public HistoryDetailTabModel() {
        this.defaultCurrency = "NZD";   // TODO set by configuration
        datas = new ArrayList<>();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return datas.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {

        return datas.get(row).arow[col];
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


    public void setModelData(List<CoinPriceValueTrackerDTO> coinDTOList){
        int irow = 0;
        if (coinDTOList.size() > 0 ) {
            datas = new ArrayList<>();

            String dateStr = "";
            double dayTotalValue = 0.0;
            for (CoinPriceValueTrackerDTO coin: coinDTOList ) {
                if ( coin.getCoinBalance() > 0.0 ) {
                    Row row_i = new Row();
                    datas.add(row_i);
                    if (dateStr.compareToIgnoreCase(coin.getPriceDate().toString()) == 0) {
                        datas.get(irow).arow[DATE_IDX] = "";
                    } else {
                        if ( dayTotalValue > 0.0 ) {
                            datas.get(irow).arow[DATE_IDX] = "Total";
                            datas.get(irow).arow[TOTAL_VALUE_IDX] = FiatCurrency.getValueMoneyFormat(dayTotalValue, FiatNames.valueOf(defaultCurrency), 0);
                            datas.get(irow).arow[TOTAL_VLUE_FIAT_TYPE_IDX] = coin.getCoinPriceFiatTicker();
                            irow++;
                            row_i = new Row();  // blank line
                            datas.add(row_i);
                            irow++;

                            row_i = new Row();
                            datas.add(row_i);
                        }
                        datas.get(irow).arow[DATE_IDX] = coin.getPriceDate().toString();
                        dateStr = coin.getPriceDate().toString();
                        dayTotalValue = 0.0;
                    }
                    datas.get(irow).arow[TICKER_IDX] = coin.getTicker();
                    datas.get(irow).arow[ICON_IDX] = "";                                              // TODO icon look up.
                    datas.get(irow).arow[COIN_BAL_IDX] = coin.getCoinBalanceAsFormattedString(4);
                    datas.get(irow).arow[COIN_PRICE_IDX] = FiatCurrency.getValueMoneyFormat(coin.getCoinPrice(), FiatNames.valueOf(defaultCurrency), 2);
                    datas.get(irow).arow[TOTAL_VALUE_IDX] = FiatCurrency.getValueMoneyFormat( (coin.getCoinPrice() * coin.getCoinBalance()), FiatNames.valueOf(defaultCurrency), 0);
                    datas.get(irow).arow[TOTAL_VLUE_FIAT_TYPE_IDX] = coin.getCoinPriceFiatTicker();   //TODO use configuration to find the right fiat
                    dayTotalValue += (coin.getCoinPrice() * coin.getCoinBalance());
                    irow++;
                }
            }
        }
    }


    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
}
