package com.arek00.clusterizer.demos.display.DistanceDisplay;

import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewController {
    public Pane mainPane;
    public Canvas canvas;
    public Slider scaleSlider;
    public Label scaleValueLabel;
    public AnchorPane pointsPane;
    public ScatterChart pointsScatteredChart;

    public void addDataToChart(List<XYChart.Data> data) {

        List<XYChart.Series> series = SeriesCreator.getSeries(data);

        series.stream().
                forEach(serie -> {
                    pointsScatteredChart.getData().addAll(serie);
                });
    }

    public void setScale(float scale) {
        pointsScatteredChart.setScaleX(scale);
    }


    public void onScale(Event event) {
        scaleValueLabel.setText(Double.toString(scaleSlider.getValue()));
        setScale((float) scaleSlider.getValue());
    }
}

class SeriesCreator {

    public static List<XYChart.Series> getSeries(List<XYChart.Data> data) {
        Map<String, XYChart.Series> map = new HashMap<String, XYChart.Series>();


        data.stream().
                forEach(dataEntity -> {
                    int clusterNumber = ((Integer) dataEntity.getExtraValue()).intValue();
                    float pointY = ((Float) dataEntity.getYValue()).floatValue();
                    dataEntity.setYValue(pointY + clusterNumber);


                    String clusterName = Integer.toString(clusterNumber);

                    if(! map.containsKey(clusterName)) {
                        XYChart.Series serie = new XYChart.Series();
                        serie.setName("Cluster " + clusterName);

                        map.put(clusterName, serie);
                    }

                    map.get(clusterName).getData().add(dataEntity);
                });

        return new ArrayList<XYChart.Series>(map.values());
    }

}