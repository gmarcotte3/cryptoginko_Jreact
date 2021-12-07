package com.marcotte.blockhead.gui.tabs.portfolio.bywallet;

import com.marcotte.blockhead.gui.ApplicationServicesBean;
import com.marcotte.blockhead.gui.tabs.portfolio.TotalValuePanel;
import com.marcotte.blockhead.model.coin.CoinDTOCompareByFiatBalance;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;
import com.marcotte.blockhead.model.wallet.WalletDTO;
import com.marcotte.blockhead.model.wallet.WalletDTOCompareByFiatValue;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * sub pannel for protfolio By Wallet tab.
 * This tab will show values by wallet and then coins in a summary sorted by most valuble to least valuble.
 */
public class PortfolioByWalletTab extends JPanel {

    private ApplicationServicesBean applicationServicesBean;
    private PortfolioByWalletTableDataModel portfolioByWalletTableDataModel;

    private String defaultCurency1;
    private String defaultCurency2;
    private String defaultCurency3;

    private TotalValuePanel portfolioTotals;
    private FiatCurrencyList fiat_balances;

    public PortfolioByWalletTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;

        defaultCurency1 = "USD"; // TODO set via configuration
        defaultCurency2 = "NZD";
        defaultCurency3 = "JPM";

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

        setLayout( new BorderLayout());
        add( portfolioTotals, BorderLayout.NORTH );
        add( tabkeScrollPane, BorderLayout.CENTER );

        List<WalletDTO> walletDTOList = applicationServicesBean.getPortFolioByWalletAndCoinService().findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();
        Collections.sort(walletDTOList, (new WalletDTOCompareByFiatValue()).reversed());
        sortWalletCoins (walletDTOList );
        portfolioByWalletTableDataModel.setModelData( walletDTOList);

        calculateFiatTotalValues( walletDTOList);
        portfolioTotals.update_FiatValueTotals(fiat_balances);
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