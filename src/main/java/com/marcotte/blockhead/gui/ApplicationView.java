package com.marcotte.blockhead.gui;

import com.marcotte.blockhead.gui.tabs.CurrentPricingTab.CurrentPricingTab;
import com.marcotte.blockhead.gui.tabs.ReportTab;
import com.marcotte.blockhead.gui.tabs.WalletTab;
import com.marcotte.blockhead.gui.tabs.importcsv.ImportTab;
import com.marcotte.blockhead.gui.tabs.portfolio.PortfolioTab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * application view
 * This contains only the UI logic
 */
public class ApplicationView extends JFrame implements IView {

    private static final long serialVersionUID = 2104090919;

    private IPresenter presenter;
    private Action  openAction;
    private Action  notImplementedAction;

    @Override
    public void setPresenter(IPresenter presenter) {
        this.presenter = presenter;
    }

    private IPresenter getPresenter() {
        if ( presenter == null ) {
            throw new IllegalStateException("The presentor is not set");
        }
        return presenter;
    }

    /**
     * Application action setup
     * @throws HeadlessException
     */
    public ApplicationView(ApplicationPresenter applicationPresenter) throws HeadlessException {
        this.presenter = applicationPresenter;
        initActions();
        initMenu();
        initContentPane();
    }

    /**
     * init the menu items
     * create a menu bar
     * create menu items
     *
     * Menu items
     * File
     *    open file
     *    settings
     * Portfolio
     *    import Exodus Address
     * Price - refresh prices
     * Reports
     *    Cap Gain Exodus
     *    Cap Gain Dedalus
     *    Cap Gain Trezor
     *    ??
     *  Setting
     */
    private void initMenu() {

        final JMenuBar menuBar = new JMenuBar();
        setJMenuBar( menuBar );

        final JMenu file = menuBar.add(new JMenu("file"));
        file.add(openAction);

        // portfolio menu -----------------------------------------------------------------------------------
        createPortfolioMenuItems(menuBar);

        // Price menu -------------------------------------------------------------------------------------------
        createCurrentPricesMenuItems(menuBar);

        // Reports menu -----------------------------------------------------------------------------------------
        createReportsMenuItems(menuBar);
    }

    /**
     * Create Portfolio Menu Items
     * @param menuBar
     */
    private void createPortfolioMenuItems(JMenuBar menuBar)
    {
        final JMenu portfolioMenu = menuBar.add(new JMenu("portfolio"));
        final JMenuItem importCoinCSVItem = new JMenuItem("Import Exodus Addr");
        importCoinCSVItem.addActionListener(notImplementedAction);
        portfolioMenu.add(importCoinCSVItem);
        final JMenuItem importDaedalusAddrsItem = new JMenuItem("Import Daedalus Addr");
        importCoinCSVItem.addActionListener(notImplementedAction);
        portfolioMenu.add(importDaedalusAddrsItem);
    }

    /**
     * create the current pricess menu items
     * @param menuBar
     */
    private void createCurrentPricesMenuItems(JMenuBar menuBar)
    {
        final JMenu priceMenu = menuBar.add(new JMenu("Prices"));
        final JMenuItem refreshPricesItem = new JMenuItem("Refresh");
        refreshPricesItem.addActionListener(notImplementedAction);
        priceMenu.add(refreshPricesItem);

    }

    /**
     * Reports menu creation.
     * Cap gains reports from csv file inputs from Exodus, Daedalus wallet, Trezor
     * @param menuBar
     */
    private void createReportsMenuItems(JMenuBar menuBar)
    {
        final JMenu reportsMenu = menuBar.add(new JMenu("Reports"));
        final JMenuItem capGainExodusItem = new JMenuItem("Exodus trans csv");
        capGainExodusItem.addActionListener(notImplementedAction);
        reportsMenu.add(capGainExodusItem);

        final JMenuItem capGainDaedalusItem = new JMenuItem("Daedalus trans csv");
        capGainDaedalusItem.addActionListener(notImplementedAction);
        reportsMenu.add(capGainDaedalusItem);
    }

    /**
     * create/init the actions
     * pass the action events to the presenter.
     */
    private void initActions() {
        openAction = new AbstractAction("open", null)
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWarning(  "Warning", "NO files to open for you");
            }
        };

        notImplementedAction = new AbstractAction("not implementedd", null)
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWarning(  "Warning", "This feature is coming to a screen near you");
            }
        };
    }

    /**
     * create the main content panel
     */
    private void initContentPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Current Prices" , null, new CurrentPricingTab(presenter.getApplicationServicesBean()),"Current Coin Prices");
        //tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        tabbedPane.addTab("Portfolio Value", null, new PortfolioTab(presenter.getApplicationServicesBean()),"Current Portfolio value");
        //tabbedPane.addTab(null, null, new PortfolioTab());
        //tabbedPane.setTabComponentAt(1, new JLabel("Current Portfolio Value"));
        //tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        tabbedPane.addTab("Import", null, new ImportTab(), "Import csv files");
        tabbedPane.addTab("Reports", null, new ReportTab(),"Reports gains loses");

        tabbedPane.addTab("Wallets", null, new WalletTab(),"Wallets performance and maintenance");


        add( tabbedPane, BorderLayout.CENTER);
    }

    /**
     * show warning
     * present the user with warning message.
     * @param title
     * @param message
     */
    @Override
    public void showWarning( String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

}