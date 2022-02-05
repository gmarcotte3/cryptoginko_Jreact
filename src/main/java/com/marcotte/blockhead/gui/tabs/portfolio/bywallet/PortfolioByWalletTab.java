package com.marcotte.blockhead.gui.tabs.portfolio.bywallet;

import com.marcotte.blockhead.gui.ApplicationServicesBean;
import com.marcotte.blockhead.gui.tabs.portfolio.TotalValuePanel;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.coin.CoinDTOCompareByFiatBalance;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;
import com.marcotte.blockhead.model.wallet.WalletDTO;
import com.marcotte.blockhead.model.wallet.WalletDTOCompareByFiatValue;
import com.marcotte.blockhead.util.Utils;
import com.opencsv.CSVWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

/**
 * sub pannel for protfolio By Wallet tab.
 * This tab will show values by wallet and then coins in a summary sorted by most valuble to least valuble.
 */
public class PortfolioByWalletTab extends JPanel implements ActionListener  {

    private ApplicationServicesBean applicationServicesBean;
    private PortfolioByWalletTableDataModel portfolioByWalletTableDataModel;
    private List<WalletDTO> walletDTOList;

    private String defaultCurency1;
    private String defaultCurency2;
    private String defaultCurency3;

    private TotalValuePanel portfolioTotals;
    private FiatCurrencyList fiat_balances;

    // export data controles here ------------
    private JButton exportCsvButton;
    private JFileChooser fileCooser;
    private File csvFile;
    private JTextField filename;
    private JPanel exportPanel;

    public PortfolioByWalletTab(ApplicationServicesBean applicationServicesBean) {
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
        portfolioTotals = new TotalValuePanel(defaultCurency1, defaultCurency2, defaultCurency3);


        portfolioByWalletTableDataModel = new PortfolioByWalletTableDataModel();
        JTable table = new JTable(portfolioByWalletTableDataModel );
        JScrollPane tabkeScrollPane = new JScrollPane(table);

        configureTableColumns(table);

        // build the lower part of the UI:  Totals + details
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout( new BorderLayout());
        detailPanel.add(portfolioTotals, BorderLayout.NORTH);
        detailPanel.add( tabkeScrollPane, BorderLayout.CENTER);

        setLayout( new BorderLayout());
        exportPanel = createExportCSVPanel("export portfolio by wallet to csv file");
        add(exportPanel, BorderLayout.NORTH);
        add( detailPanel, BorderLayout.CENTER);


        walletDTOList = applicationServicesBean.getPortFolioByWalletAndCoinService().findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();
        Collections.sort(walletDTOList, (new WalletDTOCompareByFiatValue()).reversed());
        sortWalletCoins (walletDTOList );
        portfolioByWalletTableDataModel.setModelData( walletDTOList);

        calculateFiatTotalValues( walletDTOList);


        portfolioTotals.update_FiatValueTotals(fiat_balances);
    }

    /**
     * create the CSV export panel
     * @param myTitle
     * @return
     */
    private JPanel createExportCSVPanel(String myTitle) {
        fileCooser = new JFileChooser();

        JPanel componentPanel = new JPanel();
        componentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(myTitle),
                BorderFactory.createEmptyBorder(5,5,5,5)));

        BoxLayout boxLayout = new BoxLayout(componentPanel, BoxLayout.X_AXIS);
        componentPanel.setLayout(boxLayout);

        JLabel label = new JLabel("Filename: ");
        componentPanel.add(label);

        filename = new JTextField(20);
        filename.setEnabled(false);
        componentPanel.add(filename);

        exportCsvButton = new JButton("Select csv file");
        exportCsvButton.addActionListener(this);
        componentPanel.add(exportCsvButton);
        return componentPanel;
    }

    /**
     * sart the coins in a wallet by reverse fiat value
     * @param walletDTOList
     */
    private void sortWalletCoins( List<WalletDTO> walletDTOList) {
        for (WalletDTO wallet: walletDTOList) {
            Collections.sort( wallet.getCoinDTOs(), (new CoinDTOCompareByFiatBalance().reversed()));
        }
    }

    /**
     * calculate the total fiat values from the wallet list.
     * @param walletDTOList
     */
    private void calculateFiatTotalValues( List<WalletDTO> walletDTOList) {
        this.fiat_balances  = new FiatCurrencyList();

        for (WalletDTO walletDTO : walletDTOList ) {
            fiat_balances.addToFiatList(walletDTO.getFiat_balances());
        }
    }

    /**
     * Set the formatting for columns in the table here.
     *{ "wallet","TotalValue", "fiat" ,"Ticker", "Name", "Coin Balance", "Price", "Coin Value", "fiat"};
     * setting perfered column widths
     * @param table
     */
    private void configureTableColumns( JTable table) {
        table.setFillsViewportHeight(true);
        TableColumn column = null;

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);

        table.getColumnModel().getColumn(0).setPreferredWidth(150);      // wallet name
        table.getColumnModel().getColumn(0).setMaxWidth(150);

        table.getColumnModel().getColumn(1).setPreferredWidth(150);     // wallet value
        table.getColumnModel().getColumn(1).setMaxWidth(350);
        table.getColumnModel().getColumn(1).setCellRenderer( rightRenderer );

        table.getColumnModel().getColumn(2).setMaxWidth(50);           // fiat currency name

        table.getColumnModel().getColumn(3).setPreferredWidth(50);      // ticker
        table.getColumnModel().getColumn(3).setMaxWidth(50);

        table.getColumnModel().getColumn(4).setMaxWidth(100);           // coin name

        table.getColumnModel().getColumn(5).setPreferredWidth(150);     // coin balance
        table.getColumnModel().getColumn(5).setMaxWidth(350);

        table.getColumnModel().getColumn(6).setPreferredWidth(100);     // price
        table.getColumnModel().getColumn(6).setMaxWidth(350);
        table.getColumnModel().getColumn(6).setCellRenderer( rightRenderer );

        table.getColumnModel().getColumn(7).setPreferredWidth(150);     // value
        table.getColumnModel().getColumn(7).setMaxWidth(350);
        table.getColumnModel().getColumn(7).setCellRenderer( rightRenderer );

        table.getColumnModel().getColumn(8).setMaxWidth(50);           // fiat currency name
    }

    /**
     * actions performed here when button clicks are done on this tab pannel.
     * Actions:  1] export the portfolio by wallet to a csv file
     *
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exportCsvButton) {

            fileCooser = new JFileChooser();
            fileCooser.setSelectedFile(new File("~/portfolioByWallet-" + Utils.timestampToDateStr_yyyymmdd(new Timestamp(System.currentTimeMillis())) + ".csv"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files (*csv)", "csv");
            fileCooser.setFileFilter(filter);

            int returnVal = fileCooser.showSaveDialog(this);
            if ( returnVal == JFileChooser.APPROVE_OPTION) {
                File csvExportFile = fileCooser.getSelectedFile();
                String fullPathCsvExportFileName = csvExportFile.getAbsolutePath();
                writeDataLineByLine(csvExportFile, walletDTOList);

            }
        }

    }
    /**
     * write the by coin portfolio as a csv file line by line here. The first line is the header, data rows follow
     * @param exportFile
     * @param byCoinDTOList
     */
    public void writeDataLineByLine(File exportFile, List<WalletDTO> byWalletDTOList)
    {
        // first create file object for file placed at location
        // specified by filepath

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(exportFile);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = portfolioByWalletTableDataModel.getColumnNames();
            writer.writeNext(header);

//            for (WalletDTO walletDTO : byWalletDTOList ) {
//                String[] rowdata = new String[portfolioByWalletTableDataModel.getColumnCount()];
//                rowdata[portfolioByCoinsTableDataModel.TICKER_IDX] = coin.getTicker();
//                rowdata[portfolioByCoinsTableDataModel.COIN_NAME_IDX] = coin.getCoinName();
//                rowdata[portfolioByCoinsTableDataModel.COIN_BAL_IDX] = coin.getCoinBalance().toString();
//                rowdata[portfolioByCoinsTableDataModel.COIN_PRICE_IDX] = coin.getFiat_prices().findFiat(portfolioByCoinsTableDataModel.getDefaultCurrency()).getValueMoneyFormat(2);
//                rowdata[portfolioByCoinsTableDataModel.TOTAL_VALUE_IDX] = coin.getFiat_balances().findFiat(portfolioByCoinsTableDataModel.getDefaultCurrency()).getValueMoneyFormat(0);
//                rowdata[portfolioByCoinsTableDataModel.TOTAL_VLUE_FIAT_TYPE_IDX] = portfolioByCoinsTableDataModel.getDefaultCurrency();
//                writer.writeNext(rowdata);
//            }

            for (WalletDTO walletcoin: walletDTOList ) {

                String[] datarow = new String[portfolioByWalletTableDataModel.getColumnCount()];
                datarow[portfolioByWalletTableDataModel.WALLET_NAME_IDX] = walletcoin.getWalletName();
                double walletValue = walletcoin.getFiat_balances().findFiat("USD").getValue();
                if ( walletValue > (double) 0.0 ) {
                    datarow[portfolioByWalletTableDataModel.WALLET_TOTAL_VALUE_IDX] = walletcoin.getFiat_balances().findFiat(portfolioByWalletTableDataModel.getDefaultCurrency()).getValueMoneyFormat();
                    datarow[portfolioByWalletTableDataModel.WALLET_TOTAL_VLUE_FIAT_TYPE_IDX] = portfolioByWalletTableDataModel.getDefaultCurrency();

                    for (CoinDTO coin : walletcoin.getCoinDTOs()) {
                        datarow[portfolioByWalletTableDataModel.COIN_TICKER_IDX] = coin.getTicker();
                        datarow[portfolioByWalletTableDataModel.COIN_NAME_IDX] = coin.getCoinName();
                        datarow[portfolioByWalletTableDataModel.COIN_BAL_IDX] = coin.getCoinBalance().toString();
                        datarow[portfolioByWalletTableDataModel.COIN_PRICE_IDX] = coin.getFiat_prices().findFiat(portfolioByWalletTableDataModel.getDefaultCurrency()).getValueMoneyFormat();
                        datarow[portfolioByWalletTableDataModel.COIN_VALUE_IDX] = coin.getFiat_balances().findFiat(portfolioByWalletTableDataModel.getDefaultCurrency()).getValueMoneyFormat();
                        datarow[portfolioByWalletTableDataModel.COIN_FIAT_IDX] = portfolioByWalletTableDataModel.getDefaultCurrency();

                        if (datarow[portfolioByWalletTableDataModel.WALLET_NAME_IDX] == null) {
                            datarow[portfolioByWalletTableDataModel.WALLET_NAME_IDX] = " ";
                            datarow[portfolioByWalletTableDataModel.WALLET_TOTAL_VALUE_IDX] = " ";
                            datarow[portfolioByWalletTableDataModel.WALLET_TOTAL_VLUE_FIAT_TYPE_IDX] = " ";
                        }

                        writer.writeNext(datarow);
                    } // endfor
                }  // if wallet value > 0
            } // end for
            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // ========================
    // getters and setters
    // ========================

    public String getDefaultCurency1() {
        return defaultCurency1;
    }

    public void setDefaultCurency1(String defaultCurency1) {
        this.defaultCurency1 = defaultCurency1;
    }

    public String getDefaultCurency2() {
        return defaultCurency2;
    }

    public void setDefaultCurency2(String defaultCurency2) {
        this.defaultCurency2 = defaultCurency2;
    }

    public String getDefaultCurency3() {
        return defaultCurency3;
    }

    public void setDefaultCurency3(String defaultCurency3) {
        this.defaultCurency3 = defaultCurency3;
    }
}