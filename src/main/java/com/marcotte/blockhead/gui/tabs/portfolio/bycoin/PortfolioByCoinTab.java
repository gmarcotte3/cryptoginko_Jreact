package com.marcotte.blockhead.gui.tabs.portfolio.bycoin;


import com.marcotte.blockhead.gui.ApplicationServicesBean;
import com.marcotte.blockhead.gui.tabs.portfolio.TotalValuePanel;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.coin.CoinSortByCoinValue;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * Portfolio by Coin tab
 * This tab show portfolio values by coin sorted by most valueble to least.
 */
public class PortfolioByCoinTab extends JPanel{
    private ApplicationServicesBean applicationServicesBean;
    private PortfolioByCoinsTableDataModel portfolioByCoinsTableDataModel;


    private String defaultCurency1;
    private String defaultCurency2;
    private String defaultCurency3;

    private TotalValuePanel portfolioTotals;
    private FiatCurrencyList fiat_balances;

    public PortfolioByCoinTab(ApplicationServicesBean applicationServicesBean) {
        super();

//        defaultCurency1 = "USD"; // TODO set via configuration
//        defaultCurency2 = "NZD";
//        defaultCurency3 = "JPM";

        this.applicationServicesBean = applicationServicesBean;
        defaultCurency1 = applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault();
        defaultCurency2 = applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault2();
        defaultCurency3 = applicationServicesBean.getBlockheadConfig().getFiatCurrencyDefault3();

        portfolioByCoinsTableDataModel = new PortfolioByCoinsTableDataModel();

        createUI();
    }

    private void createUI() {
        portfolioTotals = new TotalValuePanel(defaultCurency1, defaultCurency2, defaultCurency3);
        JTable table = new JTable(portfolioByCoinsTableDataModel );
        JScrollPane tabkeScrollPane = new JScrollPane(table);
        configureTableColumns(table);

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout( new BorderLayout());
        detailPanel.add(portfolioTotals, BorderLayout.NORTH);
        detailPanel.add( tabkeScrollPane, BorderLayout.CENTER);


        setLayout( new BorderLayout());
        add(detailPanel, BorderLayout.CENTER);


        refreashDataModel();
    }


    /**
     * reset the model by calling the service and recalculating the totals.
     */
    public void refreashDataModel() {
        List<CoinDTO> coinDTOList = applicationServicesBean.getPortfolioByCoinsService().findAllLatestSumBalanceGroupByCoin();
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