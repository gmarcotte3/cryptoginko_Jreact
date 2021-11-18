package com.marcotte.blockhead.gui.tabs.portfolio.bywallet;

import com.marcotte.blockhead.gui.ApplicationServicesBean;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.coin.CoinSortByCoinValue;
import com.marcotte.blockhead.model.wallet.WalletDTO;
import com.marcotte.blockhead.model.wallet.WalletDTOCompareByFiatValue;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PortfolioByWalletTab extends JPanel {

    private ApplicationServicesBean applicationServicesBean;
    private PortfolioByWalletTableDataModel portfolioByWalletTableDataModel;

    public PortfolioByWalletTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;
        portfolioByWalletTableDataModel = new PortfolioByWalletTableDataModel();
        JTable table = new JTable(portfolioByWalletTableDataModel );
        JScrollPane tabkeScrollPane = new JScrollPane(table);

        configureTableColumns(table);

        setLayout( new BorderLayout());
        add( tabkeScrollPane, BorderLayout.CENTER);

        List<WalletDTO> walletDTOList = applicationServicesBean.getPortFolioByWalletAndCoinService().findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();
        Collections.sort(walletDTOList, (new WalletDTOCompareByFiatValue()).reversed());
        portfolioByWalletTableDataModel.setModelData( walletDTOList);
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
}