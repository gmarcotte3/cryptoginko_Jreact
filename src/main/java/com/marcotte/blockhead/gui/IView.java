package com.marcotte.blockhead.gui;

public interface IView {
    void setPresenter(IPresenter presenter);
    //    File showOpenFileChooser(File currentFile);
    void showWarning( String title, String message);
}
