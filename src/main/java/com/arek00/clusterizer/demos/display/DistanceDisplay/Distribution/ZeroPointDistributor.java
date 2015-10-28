package com.arek00.clusterizer.demos.display.DistanceDisplay.Distribution;

import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.DisplayedPointEntity;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lombok.NonNull;

public class ZeroPointDistributor implements Distributor {

    @Override
    public void distribute(@NonNull ObservableList<XYChart.Series> chartData) {
        chartData
                .forEach(serie -> {
                    distributeSerie(serie);
                });

    }

    public void distributeSerie(@NonNull XYChart.Series serie) {
        serie.getData()
                .forEach(point -> {

                    if (point instanceof XYChart.Data) {
                        XYChart.Data chartPoint = (XYChart.Data) point;
                        setXValue(chartPoint);
                    }
                });
    }

    private void setXValue(XYChart.Data point) {
        Object extraValue = point.getExtraValue();

        if(extraValue instanceof DisplayedPointEntity) {
            point.setXValue(((DisplayedPointEntity) extraValue).getDistanceFromZero());
        }
    }

}
