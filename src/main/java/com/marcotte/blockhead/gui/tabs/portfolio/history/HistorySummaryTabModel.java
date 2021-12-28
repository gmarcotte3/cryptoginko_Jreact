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

public class HistorySummaryTabModel extends AbstractTableModel {
    private static final Logger log = LoggerFactory.getLogger(HistorySummaryTabModel.class);

    private String defaultCurrency;
    private String defaultCurrency2;
    private String defaultCurrency3;

    // columns
    private final String[] columnNames = {"Date", "TotalValue", "TotalValue2",  "TotalValue3" };
    private final int NUMBER_OF_COLUMNS = 4;

    protected class Row {
        public Object arow[] = {"","","", "" };
    }

    private List<HistorySummaryTabModel.Row> datas;

    public static final int DATE_IDX = 0;
    public static final int TOTAL_VALUE_IDX = 1;
    public static final int TOTAL_VALUE2_IDX = 2;
    public static final int TOTAL_VALUE3_IDX = 3;

    public HistorySummaryTabModel() {
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

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void setModelData(List<CoinPriceValueTrackerDTO> coinDTOList){
        int irow = 0;
        if (coinDTOList.size() > 0 ) {
            datas = new ArrayList<>();

            String dateStr = "";
            double dayTotalValue = 0.0;
            double dayTotalValue2 = 0.0;
            double dayTotalValue3 = 0.0;
            HistorySummaryTabModel.Row row_i = new HistorySummaryTabModel.Row();
            for (CoinPriceValueTrackerDTO coin: coinDTOList ) {
                if ( irow == 0 ) {
                    defaultCurrency = coin.getCoinPriceFiatTicker();
                    defaultCurrency2 = coin.getCoinPriceFiatTicker2();
                    defaultCurrency3 = coin.getCoinPriceFiatTicker3();
                }
                if ( coin.getCoinBalance() > 0.0 ) {

                    if (dateStr.compareToIgnoreCase(coin.getPriceDate().toString()) != 0) {
                        if ( dayTotalValue > 0.0 ) {
                            datas.add(row_i);

                            datas.get(irow).arow[DATE_IDX] = dateStr;
                            datas.get(irow).arow[TOTAL_VALUE_IDX] = FiatCurrency.getValueMoneyFormat(dayTotalValue, FiatNames.valueOf(defaultCurrency) , 0) + " " + defaultCurrency;
                            datas.get(irow).arow[TOTAL_VALUE2_IDX] = FiatCurrency.getValueMoneyFormat(dayTotalValue2, FiatNames.valueOf(defaultCurrency2), 0) + " " + defaultCurrency2;
                            datas.get(irow).arow[TOTAL_VALUE3_IDX] = FiatCurrency.getValueMoneyFormat(dayTotalValue3, FiatNames.valueOf(defaultCurrency3), 0) + " " + defaultCurrency3;
                            irow++;

                            row_i = new HistorySummaryTabModel.Row();
                        }

                        defaultCurrency = coin.getCoinPriceFiatTicker();
                        defaultCurrency2 = coin.getCoinPriceFiatTicker2();
                        defaultCurrency3 = coin.getCoinPriceFiatTicker3();

                        dateStr = coin.getPriceDate().toString();
                        dayTotalValue = 0.0;
                        dayTotalValue2 = 0.0;
                        dayTotalValue3 = 0.0;
                    }
                    dayTotalValue += (coin.getCoinPrice() * coin.getCoinBalance());
                    dayTotalValue2 += (coin.getCoinPrice2() * coin.getCoinBalance());
                    dayTotalValue3 += (coin.getCoinPrice3() * coin.getCoinBalance());
                }
            }
            if ( dayTotalValue > 0.0 ) {
                datas.add(row_i);
                datas.get(irow).arow[DATE_IDX] = dateStr;
                datas.get(irow).arow[TOTAL_VALUE_IDX] = FiatCurrency.getValueMoneyFormat(dayTotalValue, FiatNames.valueOf(defaultCurrency), 0)  + " " + defaultCurrency;
                datas.get(irow).arow[TOTAL_VALUE2_IDX] = FiatCurrency.getValueMoneyFormat(dayTotalValue2, FiatNames.valueOf(defaultCurrency2), 0) + " " + defaultCurrency2;
                datas.get(irow).arow[TOTAL_VALUE3_IDX] = FiatCurrency.getValueMoneyFormat(dayTotalValue3, FiatNames.valueOf(defaultCurrency3) , 0) + " " + defaultCurrency3;
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
