package com.arek00.clusterizer.demos.display.DistanceDisplay.Distribution;


import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

public interface Distributor {

    public void distribute(ObservableList<XYChart.Series> chartData);
}
