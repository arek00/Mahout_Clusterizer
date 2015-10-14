package com.arek00.clusterizer.demos.display.DistanceDisplay;

import com.arek00.clusterizer.demos.ClusteredPoint;
import javafx.scene.chart.XYChart;


public class ClusteredPointDataConverter {

    /**
     * Get chart's data object from ClusteredPoint instance.
     *
     * @param point
     * @return
     */
    public static XYChart.Data getData(ClusteredPoint point) {
        return new XYChart.Data(point.getDistance(), point.getWeight(), point.getClusterNumber());
    }
}
