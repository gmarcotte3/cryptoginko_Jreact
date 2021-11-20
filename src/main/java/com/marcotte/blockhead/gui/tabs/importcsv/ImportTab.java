package com.marcotte.blockhead.gui.tabs.importcsv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class ImportTab extends JPanel implements ActionListener {
    private JPanel importPanel;
    private JButton importcsvButton;
    private JFileChooser fileCooser;
    private File csvFile;

    private JTextField filename;

    public ImportTab() {
        super();
        createUI();
    }

    private void createUI() {
        setLayout( new BorderLayout());
        importPanel = createImportPanel("import a Ginkou csv file");
        add(importPanel, BorderLayout.NORTH);


    }

    private JPanel createImportPanel(String myTitle) {
        fileCooser = new JFileChooser();

        JPanel componentPanel = new JPanel();
        componentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(myTitle),
                BorderFactory.createEmptyBorder(5,5,5,5)));

        BoxLayout boxLayout = new BoxLayout(componentPanel, BoxLayout.X_AXIS);
        componentPanel.setLayout(boxLayout);

        JLabel label = new JLabel("Filename: ");
        componentPanel.add(label);

        filename = new JTextField(20);
        filename.setEnabled(false);
        componentPanel.add(filename);

        importcsvButton = new JButton("Select csv file");
        importcsvButton.addActionListener(this);
        componentPanel.add(importcsvButton);
        return componentPanel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == importcsvButton) {
            int returnVal = fileCooser.showOpenDialog(this);
            if ( returnVal == JFileChooser.APPROVE_OPTION) {
                csvFile = fileCooser.getSelectedFile();
                filename.setText(csvFile.getAbsolutePath());
            }
        }

    }
}