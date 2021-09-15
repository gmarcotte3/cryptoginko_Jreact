package com.marcotte.blockhead.gui;

import java.io.File;

/**
 * Application presenter
 * This performs actions from user via the view. The action will call the view
 * to perform UI.
 */
public class ApplicationPresenter implements Presenter {

    private View view;
    private File currentFile;

    @Override
    public void setView(View view) {
        this.view = view;
    }

    /**
     * used internally. fails if the class is not setup correctly
     * (missing the view)
     * @return
     */
    private View getView() {
        if ( view == null ) {
            throw new IllegalStateException("View is not set");
        }
        return view;
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
