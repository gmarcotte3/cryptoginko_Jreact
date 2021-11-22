package com.marcotte.blockhead.gui.tabs.importcsv;

import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.gui.ApplicationServicesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import static com.marcotte.blockhead.util.ReadCSV.readCsv;


/**
 * Import CSV files. this tab provides csv inports
 * 1] ginko csv import: a special format for importing public addresses of cryptos with wallet names and coin balances.
 * 2] exodus wallet address imports. Exodus wallet provides export of addresses, and this option will let you import the
 *    addresses into the assety management system assigning a ticker (crypto ticker) and wallet so the application can track
 *    the value by coin and wallet.
 */
public class ImportTab extends JPanel implements ActionListener {
    private static final Logger log = LoggerFactory.getLogger(ImportTab.class);

    private JPanel importPanel;
    private JButton importcsvButton;
    private JFileChooser fileCooser;
    private File csvFile;

    private ApplicationServicesBean applicationServicesBean;


    private JTextField filename;

    public ImportTab(ApplicationServicesBean applicationServicesBean) {
        super();
        this.applicationServicesBean = applicationServicesBean;
        createUI();
    }

    private void createUI() {
        setLayout( new BorderLayout());
        importPanel = createImportPanel("import a Ginkou csv file");
        add(importPanel, BorderLayout.NORTH);


    }

    /**
     * create the main panel
     * @param myTitle
     * @return
     */
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

    /**
     * actions performed here when button clicks are done on this tab pannel.
     * Actions:  1] import-ginko csv file
     *           2] import exodus addresses.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == importcsvButton) {
            int returnVal = fileCooser.showOpenDialog(this);
            if ( returnVal == JFileChooser.APPROVE_OPTION) {
                java.util.List<java.util.List<String>> csvFileArray;
                List<BlockchainAddressStore> savedList;

                csvFile = fileCooser.getSelectedFile();
                filename.setText(csvFile.getAbsolutePath());
                try {
                    csvFileArray = readCsv(csvFile.getAbsolutePath());
                    savedList = applicationServicesBean.getGinkoCsvService().processCsvAddressDump(csvFileArray);
                    // TODO display message of succes here.

                    log.info( "Import Success: file:" + csvFile.getAbsolutePath() + " number of records: " + savedList.size());
                } catch (Exception e2 ) {
                    // TODO check for particular errors and recover properly.
                    log.error( "Import Exception: " + e2.getMessage());
                    e2.printStackTrace();
                }
            }
        }

    }
}