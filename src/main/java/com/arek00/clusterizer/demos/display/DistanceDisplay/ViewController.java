package com.arek00.clusterizer.demos.display.DistanceDisplay;

import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.ClusterCenter;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewController {
    private static Logger logger = LogManager.getLogger(ViewController.class);

    public Pane mainPane;
    public AnchorPane pointsPane;
    public ScatterChart pointsScatteredChart;

    public void addDataToChart(List<XYChart.Data> data) {


        boolean isCluster = false;

        if(data.size() > 0) {
            isCluster = (data.get(0).getExtraValue() instanceof ClusterCenter);
            logger.info("Clusters data: " + isCluster);
            logger.info(data.get(0).getExtraValue().getClass().getName());
        }
        else {
            return;
        }

        List<XYChart.Series> series = SeriesCreator.getSeries(data, isCluster);

        series.stream().
                forEach(serie -> {
                    pointsScatteredChart.getData().addAll(serie);
                });
    }
}

class SeriesCreator {

    public static List<XYChart.Series> getSeries(List<XYChart.Data> data, boolean clusters) {
        Map<String, XYChart.Series> map = new HashMap<String, XYChart.Series>();

        data.stream().
                forEach(dataEntity -> {

                    String clusterName = "";

                    if (clusters) {
                        clusterName = "Clusters";
                    }
                    else {
                        clusterName = String.format("Cluster %d", dataEntity.getYValue());
                    }


                    if (!map.containsKey(clusterName)) {
                        XYChart.Series serie = new XYChart.Series();
                        serie.setName(clusterName);

                        map.put(clusterName, serie);
                    }

                    map.get(clusterName).getData().add(dataEntity);
                });

        return new ArrayList<XYChart.Series>(map.values());
    }
}