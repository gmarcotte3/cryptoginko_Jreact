package com.marcotte.blockhead.gui.tabs.portfolio;

import com.marcotte.blockhead.model.fiat.FiatCurrencyList;

import javax.swing.*;

/**
 * display panel for total portfolio values in upto 3 fiat currencies.
 */
public class TotalValuePanel extends JPanel{
    private JTextField totalValueCurrency1;
    private JTextField totalValueCurrency2;
    private JTextField totalValueCurrency3;

    private String defaultCurency1;
    private String defaultCurency2;
    private String defaultCurency3;

    public TotalValuePanel(String defaultCurency1, String defaultCurency2, String defaultCurency3) {
        super();
        this.defaultCurency1 = defaultCurency1; // TODO set via configuration
        this.defaultCurency2 = defaultCurency2;
        this.defaultCurency3 = defaultCurency3;

        JLabel totalValueLabelCurrency1 = new JLabel ("Total "  + defaultCurency1 + ":");
        JLabel totalValueLabelCurrency2 = new JLabel ("Total "  + defaultCurency2 + ":");
        JLabel totalValueLabelCurrency3 = new JLabel ("Total "  + defaultCurency3 + ":");

        totalValueCurrency1 = new JTextField(10);
        totalValueCurrency1.setEnabled(false);
        totalValueCurrency2 = new JTextField(10);
        totalValueCurrency2.setEnabled(false);
        totalValueCurrency3 = new JTextField(10);
        totalValueCurrency3.setEnabled(false);

        add(totalValueLabelCurrency1);
        add(totalValueCurrency1);

        add(totalValueLabelCurrency2);
        add(totalValueCurrency2);

        add(totalValueLabelCurrency3);
        add(totalValueCurrency3);
    }


    public void update_FiatValueTotals(FiatCurrencyList fiat_balances) {
        totalValueCurrency1.setText( fiat_balances.findFiat(defaultCurency1).getValueMoneyFormat());
        totalValueCurrency2.setText( fiat_balances.findFiat(defaultCurency2).getValueMoneyFormat());
        totalValueCurrency3.setText( fiat_balances.findFiat(defaultCurency3).getValueMoneyFormat());
    }

}
