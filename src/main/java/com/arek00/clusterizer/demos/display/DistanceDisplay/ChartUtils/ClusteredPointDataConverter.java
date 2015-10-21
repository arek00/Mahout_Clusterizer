package com.arek00.clusterizer.demos.display.DistanceDisplay.ChartUtils;

import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.ClusteredPoint;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.DisplayedPointEntity;
import javafx.scene.chart.XYChart;


public class ClusteredPointDataConverter {

    /**
     *
     * @param point
     * @return
     */
    public static XYChart.Data getData(DisplayedPointEntity point) {
        XYChart.Data data = new XYChart.Data(point.getDistanceFromZero(), point.getClusterId());
        data.setExtraValue(point);

        return data;
    }
}
