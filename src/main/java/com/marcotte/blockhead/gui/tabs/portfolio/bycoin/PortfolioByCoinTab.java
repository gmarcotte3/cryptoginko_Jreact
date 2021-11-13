package com.marcotte.blockhead.gui.tabs.portfolio.bycoin;


import com.marcotte.blockhead.gui.ApplicationServicesBean;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class PortfolioByCoinTab extends JPanel{
    private ApplicationServicesBean applicationServicesBean;
    private PortfolioByCoinsTableDataModel portfolioByCoinsTableDataModel;
    private FiatCurrencyList fiat_balances;

    private JTextField totalValueCurrency1;
    private JTextField totalValueCurrency2;
    private JTextField totalValueCurrency3;

    private JPanel portfolioTotals;
    public PortfolioByCoinTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;
        portfolioByCoinsTableDataModel = new PortfolioByCoinsTableDataModel();

        portfolioTotals = createPortfolioTotals();
        JTable table = new JTable(portfolioByCoinsTableDataModel );
        JScrollPane tabkeScrollPane = new JScrollPane(table);


        configureTableColumns(table);

        setLayout( new BorderLayout());
        add(portfolioTotals, BorderLayout.NORTH);
        add( tabkeScrollPane, BorderLayout.CENTER);


        List<CoinDTO> coinDTOList = applicationServicesBean.getPortfolioByCoinsService().findAllLatestSumBalanceGroupByCoin();
        portfolioByCoinsTableDataModel.setModelData( coinDTOList);

        calculateFiatTotalValues( coinDTOList);
        update_FiatValueTotals();

    }

    private void update_FiatValueTotals() {
        totalValueCurrency1.setText( fiat_balances.findFiat("USD").getValueMoneyFormat());
        totalValueCurrency2.setText( fiat_balances.findFiat("NZD").getValueMoneyFormat());
        totalValueCurrency3.setText( fiat_balances.findFiat("JPM").getValueMoneyFormat());
    }

    private void calculateFiatTotalValues( List<CoinDTO> coinDTOList) {
        this.fiat_balances  = new FiatCurrencyList();

        for (CoinDTO coinDTO : coinDTOList ) {
            fiat_balances.addToFiatList(coinDTO.getFiat_balances());
        }
    }

    /**
     * create the total value labels
     *
     * @return
     */
    private JPanel createPortfolioTotals() {
        JPanel totalPanel = new JPanel();
        JLabel totalValueLabelCurrency1 = new JLabel ("Total USD:");
        JLabel totalValueLabelCurrency2 = new JLabel ("Total NZD:");
        JLabel totalValueLabelCurrency3 = new JLabel ("Total JPY:");

        totalValueCurrency1 = new JTextField(10);
        totalValueCurrency2 = new JTextField(10);
        totalValueCurrency3 = new JTextField(10);

        totalPanel.add(totalValueLabelCurrency1);
        totalPanel.add(totalValueCurrency1);

        totalPanel.add(totalValueLabelCurrency2);
        totalPanel.add(totalValueCurrency2);

        totalPanel.add(totalValueLabelCurrency3);
        totalPanel.add(totalValueCurrency3);

        return totalPanel;
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

        table.getColumnModel().getColumn(0).setPreferredWidth(50);      // ticker
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        table.getColumnModel().getColumn(1).setMaxWidth(100);           // icon

        table.getColumnModel().getColumn(2).setPreferredWidth(150);     // coin balance
        table.getColumnModel().getColumn(2).setMaxWidth(350);

        table.getColumnModel().getColumn(3).setPreferredWidth(100);     // price
        table.getColumnModel().getColumn(3).setMaxWidth(350);

        table.getColumnModel().getColumn(4).setPreferredWidth(150);     // value
        table.getColumnModel().getColumn(4).setMaxWidth(350);

        table.getColumnModel().getColumn(5).setMaxWidth(50);           // fiat currency name
    }
}