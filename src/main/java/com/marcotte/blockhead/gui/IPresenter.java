package com.marcotte.blockhead.gui;

public interface IPresenter {
    void setView( IView view);
    ApplicationServicesBean getApplicationServicesBean();
    //   void onOpen();
}