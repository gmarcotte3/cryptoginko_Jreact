package com.marcotte.blockhead.gui.controller;

import java.awt.event.ActionListener;
import javax.swing.JButton;

public abstract class AbstractFrameController {

    public abstract void prepareAndOpenFrame();

    protected void registerAction(JButton button, ActionListener listener) {
        button.addActionListener(listener);
    }

}