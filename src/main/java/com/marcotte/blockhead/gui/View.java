package com.marcotte.blockhead.gui;

public interface View {
    void setPresenter(Presenter presenter);
    //    File showOpenFileChooser(File currentFile);
    void showWarning( String title, String message);
}
