/*
 * Copyright (c) 2021-2021. tranquilitySoftware
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcotte.blockhead.gui.tabs.portfolio.bycoinpie;

import com.marcotte.blockhead.model.coin.CoinDTO;
import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.collections.ObservableList;

import javafx.scene.control.ScrollPane;
import javax.swing.JPanel;

import java.awt.*;
import java.util.List;

/**
 * A pic chart showing the distrubution of value by coins
 */
public class PieChartPanel extends JPanel {
    private ObservableList<PieChart.Data> dataObservableList = FXCollections.observableArrayList();
    private static PieChart pieChart;

    private PieChart chart;

    private String defaultFiat;

    public PieChartPanel(List<CoinDTO> coinDTOList ) {
        super();
        defaultFiat = "NZD";  // TODO
        updateDataModel( coinDTOList);
        initGui();
    }

    private void initGui() {
      //  setLayout( new GridLayout(2,3,10,10));
        setLayout(new BorderLayout());
        final JFXPanel dataPaneel = new JFXPanel();
        ScrollPane sp = new ScrollPane();


        pieChart = new PieChart();
        pieChart.setData(dataObservableList);
        pieChart.setTitle("By Coin total value (" + defaultFiat + ")");
        pieChart.setLegendSide(Side.BOTTOM);
        pieChart.setLabelsVisible(true);
        sp.setContent(pieChart);

        Scene scene = new Scene(sp, 800, 800);  // TODO get the size right
        dataPaneel.setScene(scene);
        add(dataPaneel, BorderLayout.CENTER);
    }

    public void updateDataModel( List<CoinDTO> coinDTOList ) {
        dataObservableList = FXCollections.observableArrayList();
        for (CoinDTO coinDto : coinDTOList ) {
            Double coinValue = coinDto.getFiat_prices().findFiat(defaultFiat).getValue() * coinDto.getCoinBalance();
            dataObservableList.add(new PieChart.Data(coinDto.getCoinName() + "-" + coinValue.longValue(), coinValue   ));
        }
    }
}
