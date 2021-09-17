package com.marcotte.blockhead.gui;


import javax.swing.*;


public class SwingMainFrame  extends JFrame {
    private ApplicationPresenter applicationPresenter;
    private ApplicationView applicationView;

    private static final String PROGRAM_TITLE = "Crypto Ginko Swing React version";


    public SwingMainFrame() {
        initUI();
    }

    private void initUI() {
        applicationPresenter = new ApplicationPresenter();
        applicationView = new ApplicationView();

        applicationPresenter.setView( applicationView);
        applicationView.setPresenter(applicationPresenter);
        applicationView.setTitle(PROGRAM_TITLE );
        applicationView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        applicationView.pack();
        applicationView.setLocationRelativeTo(null);
        applicationView.setVisible(true);
        applicationView.setSize(1000,500);
    }
}
