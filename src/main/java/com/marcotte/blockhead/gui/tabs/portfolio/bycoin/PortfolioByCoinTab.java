package com.marcotte.blockhead.gui.tabs.portfolio.bycoin;


import com.marcotte.blockhead.gui.ApplicationServicesBean;
import com.marcotte.blockhead.gui.tabs.portfolio.TotalValuePanel;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.coin.CoinSortByCoinValue;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;
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
 * Portfolio by Coin tab
 * This tab show portfolio values by coin sorted by most valueble to least.
 */
public class PortfolioByCoinTab extends JPanel  implements ActionListener {
    private ApplicationServicesBean applicationServicesBean;
    private PortfolioByCoinsTableDataModel portfolioByCoinsTableDataModel;
    private List<CoinDTO> coinDTOList;


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



    public PortfolioByCoinTab(ApplicationServicesBean applicationServicesBean) {
        super();

        this.applicationServicesBean = applicationServicesBean;
        defaultCurency1 = applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault();
        defaultCurency2 = applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault2();
        defaultCurency3 = applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault3();

        portfolioByCoinsTableDataModel = new PortfolioByCoinsTableDataModel();

        createUI();
    }

    /**
     * create the main UI panel
     */
    private void createUI() {
        portfolioTotals = new TotalValuePanel(defaultCurency1, defaultCurency2, defaultCurency3);
        JTable table = new JTable(portfolioByCoinsTableDataModel );
        JScrollPane tabkeScrollPane = new JScrollPane(table);
        configureTableColumns(table);

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout( new BorderLayout());
        detailPanel.add(portfolioTotals, BorderLayout.NORTH);
        detailPanel.add( tabkeScrollPane, BorderLayout.CENTER);

        exportPanel = createExportCSVPanel("export portfolio by coin to csv file");

        setLayout( new BorderLayout());
        add(exportPanel, BorderLayout.NORTH);
        add(detailPanel, BorderLayout.CENTER);


        refreashDataModel();
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
     * reset the model by calling the service and recalculating the totals.
     */
    public void refreashDataModel() {
        coinDTOList = applicationServicesBean.getPortfolioByCoinsService().findAllLatestSumBalanceGroupByCoin();
        Collections.sort(coinDTOList, (new CoinSortByCoinValue()).reversed());
        portfolioByCoinsTableDataModel.setModelData( coinDTOList);

        calculateFiatTotalValues( coinDTOList);
        portfolioTotals.update_FiatValueTotals(fiat_balances);
    }

    /**
     * calculate the total fiat value of the wallet from coin list
     * @param coinDTOList
     */
    private void calculateFiatTotalValues( List<CoinDTO> coinDTOList) {
        this.fiat_balances  = new FiatCurrencyList();

        for (CoinDTO coinDTO : coinDTOList ) {
            fiat_balances.addToFiatList(coinDTO.getFiat_balances());
        }
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

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);      // ticker
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        table.getColumnModel().getColumn(1).setMaxWidth(100);           // icon

        table.getColumnModel().getColumn(2).setPreferredWidth(150);     // coin balance
        table.getColumnModel().getColumn(2).setMaxWidth(350);

        table.getColumnModel().getColumn(3).setPreferredWidth(100);     // price
        table.getColumnModel().getColumn(3).setMaxWidth(350);
        table.getColumnModel().getColumn(3).setCellRenderer( rightRenderer );

        table.getColumnModel().getColumn(4).setPreferredWidth(150);     // value
        table.getColumnModel().getColumn(4).setMaxWidth(350);
        table.getColumnModel().getColumn(4).setCellRenderer( rightRenderer );

        table.getColumnModel().getColumn(5).setMaxWidth(50);           // fiat currency name
    }

    /**
     * actions performed here when button clicks are done on this tab pannel.
     * Actions:  1] export the portfolio by coins to a csv file
     *
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exportCsvButton) {

            fileCooser = new JFileChooser();
            fileCooser.setSelectedFile(new File("~/portfolioByCoins-" + Utils.timestampToDateStr_yyyymmdd(new Timestamp())) + ".csv"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files (*csv)", "csv");
            fileCooser.setFileFilter(filter);

            int returnVal = fileCooser.showSaveDialog(this);
            if ( returnVal == JFileChooser.APPROVE_OPTION) {
                File csvExportFile = fileCooser.getSelectedFile();
                String fullPathCsvExportFileName = csvExportFile.getAbsolutePath();
                writeDataLineByLine(csvExportFile, coinDTOList);

            }
        }

    }

    /**
     * write the by coin portfolio as a csv file line by line here. The first line is the header, data rows follow
     * @param exportFile
     * @param byCoinDTOList
     */
    public void writeDataLineByLine(File exportFile, List<CoinDTO> byCoinDTOList)
    {
        // first create file object for file placed at location
        // specified by filepath

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(exportFile);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = portfolioByCoinsTableDataModel.getColumnNames();
            writer.writeNext(header);

            for (CoinDTO coin: coinDTOList ) {
                String[] rowdata = new String[portfolioByCoinsTableDataModel.getColumnCount()];
                rowdata[portfolioByCoinsTableDataModel.TICKER_IDX] = coin.getTicker();
                rowdata[portfolioByCoinsTableDataModel.COIN_NAME_IDX] = coin.getCoinName();
                rowdata[portfolioByCoinsTableDataModel.COIN_BAL_IDX] = coin.getCoinBalance().toString();
                rowdata[portfolioByCoinsTableDataModel.COIN_PRICE_IDX] = coin.getFiat_prices().findFiat(portfolioByCoinsTableDataModel.getDefaultCurrency()).getValueMoneyFormat(2);
                rowdata[portfolioByCoinsTableDataModel.TOTAL_VALUE_IDX] = coin.getFiat_balances().findFiat(portfolioByCoinsTableDataModel.getDefaultCurrency()).getValueMoneyFormat(0);
                rowdata[portfolioByCoinsTableDataModel.TOTAL_VLUE_FIAT_TYPE_IDX] = portfolioByCoinsTableDataModel.getDefaultCurrency();
                writer.writeNext(rowdata);
            }

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // ======================================
    // getter and setters
    // ======================================
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