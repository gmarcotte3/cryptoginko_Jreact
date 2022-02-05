package com.marcotte.blockhead.gui.tabs.portfolio.bywallet;

import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.wallet.WalletDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PortfolioByWalletTableDataModel extends AbstractTableModel {
    private static final Logger log = LoggerFactory.getLogger(PortfolioByWalletTableDataModel.class);

    public static final int WALLET_NAME_IDX = 0;
    public static final int WALLET_TOTAL_VALUE_IDX = 1;
    public static final int WALLET_TOTAL_VLUE_FIAT_TYPE_IDX = 2;
    public static final int COIN_TICKER_IDX = 3;
    public static final int COIN_NAME_IDX = 4;
    public static final int COIN_BAL_IDX = 5;
    public static final int COIN_PRICE_IDX = 6;
    public static final int COIN_VALUE_IDX = 7;
    public static final int COIN_FIAT_IDX = 8;


    private String defaultCurrency;

    // columns
    private final String[] columnNames = {"wallet","TotalValue", "fiat" ,"Ticker", "Name", "Coin Balance", "Price", "Coin Value", "fiat"};
    private final int columnNumber = 9;

    // dummy data.
    private Object[][] data = {
            {"Exodud1", "$110,000,000.0",   "USD", "BTC", "Bitcoin",    "2.0",        "$500,000.00", "1,000,000.00", "USD"},
            {" ",       " ",                "USD", "ETH", "oshirieum",  "100.0",       "$10,000.00", "1,000,000.00", "USD"},
            {"Daedalus1","$10,000,000.20",  "USD", "ADA", "Cardano",    "100,000.0",   "$10.00",     "1,000,000.00", "USD"}
    };


    public PortfolioByWalletTableDataModel() {
        this.defaultCurrency = "NZD";   // TODO set by configuration
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }
    public String[] getColumnNames() {
        return columnNames;
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


    public void
    setModelData(List<WalletDTO> walletDTOList){
        int row = 0;
        String defaultFiatTicker = "NZD";  // TODO get the default from configuration
        if (walletDTOList.size() > 0 ) {
            data = new Object[calculateRowsNeeded( walletDTOList)][columnNumber];

            for (WalletDTO walletcoin: walletDTOList ) {
                data[row][WALLET_NAME_IDX] = walletcoin.getWalletName();
                double walletValue = walletcoin.getFiat_balances().findFiat("USD").getValue();
                if ( walletValue > (double) 0.0 ) {
                    data[row][WALLET_TOTAL_VALUE_IDX] = walletcoin.getFiat_balances().findFiat(defaultFiatTicker).getValueMoneyFormat();
                    data[row][WALLET_TOTAL_VLUE_FIAT_TYPE_IDX] = defaultFiatTicker;

                    for (CoinDTO coin : walletcoin.getCoinDTOs()) {
                        data[row][COIN_TICKER_IDX] = coin.getTicker();
                        data[row][COIN_NAME_IDX] = coin.getCoinName();
                        data[row][COIN_BAL_IDX] = coin.getCoinBalance().toString();
                        data[row][COIN_PRICE_IDX] = coin.getFiat_prices().findFiat(defaultFiatTicker).getValueMoneyFormat();
                        data[row][COIN_VALUE_IDX] = coin.getFiat_balances().findFiat(defaultFiatTicker).getValueMoneyFormat();
                        data[row][COIN_FIAT_IDX] = defaultFiatTicker;

                        if (data[row][WALLET_NAME_IDX] == null) {
                            data[row][WALLET_NAME_IDX] = " ";
                            data[row][WALLET_TOTAL_VALUE_IDX] = " ";
                            data[row][WALLET_TOTAL_VLUE_FIAT_TYPE_IDX] = " ";
                        }
                        row++;
                    } // endfor
                }  // if wallet value > 0
            } //endfor wallets
        }

    }
    private int calculateRowsNeeded( List<WalletDTO> walletDTOList) {
        int rows = 0;
        for (WalletDTO wallet: walletDTOList ) {
            rows = rows + wallet.getCoinDTOs().size();
        }
        return rows + 1;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
}