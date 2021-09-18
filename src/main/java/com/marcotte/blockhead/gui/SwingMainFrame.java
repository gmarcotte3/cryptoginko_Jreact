package com.marcotte.blockhead.gui;


import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

public class SwingMainFrame  extends JFrame {
    private static final String PROGRAM_TITLE = "Crypto Ginko Swing React version";

    // members
    private ApplicationPresenter applicationPresenter;
    private ApplicationView applicationView;

    public SwingMainFrame(ApplicationPresenter applicationPresenter) throws HeadlessException {
        this.applicationPresenter = applicationPresenter;
    }

    public  void prepareAndOpenFrame() {
        initUI();
    }
    private void initUI() {
        applicationView = new ApplicationView();

        applicationPresenter.setView( applicationView);
        applicationView.setPresenter(applicationPresenter);
        applicationView.setTitle(PROGRAM_TITLE );
        applicationView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        applicationView.pack();
        applicationView.setLocationRelativeTo(null);
        applicationView.setVisible(true);
        applicationView.setSize(1000,500);
        //this.show();
    }

    public ApplicationPresenter getApplicationPresenter() {
        return applicationPresenter;
    }

    public void setApplicationPresenter(ApplicationPresenter applicationPresenter) {
        this.applicationPresenter = applicationPresenter;
    }

    public ApplicationView getApplicationView() {
        return applicationView;
    }

}
