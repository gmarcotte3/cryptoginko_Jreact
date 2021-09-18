package com.marcotte.blockhead.gui;

import java.io.File;

/**
 * Application presenter
 * This performs actions from user via the view. The action will call the view
 * to perform UI.
 */
public class ApplicationPresenter implements IPresenter {

    private IView view;
    private File currentFile;
    private ApplicationServicesBean applicationServicesBean;

    public ApplicationPresenter (ApplicationServicesBean applicationServicesBean) {
        this.applicationServicesBean = applicationServicesBean;
    }

    @Override
    public void setView(IView view) {
        this.view = view;
    }

    /**
     * used internally. fails if the class is not setup correctly
     * (missing the view)
     * @return
     */
    private IView getView() {
        if ( view == null ) {
            throw new IllegalStateException("View is not set");
        }
        return view;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public ApplicationServicesBean getApplicationServicesBean() {
        return applicationServicesBean;
    }


//    /**
//     * does the open file logic.
//     * tells the view to showOpenFileChooser dialog box.
//     * processes the user selected file.
//     * if any problems tells viewer to show message to user.
//     */
//    @Override
//    public void onOpen() {
//        File file = getView().showOpenFileChooser(currentFile);
//        if ( file != null ) {
//            if ( file.isFile()) {
//                // load file
//            } else {
//                getView().showWarning("open file", "The file '" + file.getAbsolutePath() + "' is not a file");
//            }
//
//        }
//    }
}
