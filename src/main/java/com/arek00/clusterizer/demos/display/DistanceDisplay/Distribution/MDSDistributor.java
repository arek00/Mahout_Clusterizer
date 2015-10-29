package com.arek00.clusterizer.demos.display.DistanceDisplay.Distribution;

import com.arek00.clusterizer.Algorithms.MultidimensionalScaling.MultiDimensionalScalingEstimator;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.DisplayedPointEntity;
import com.google.common.collect.Lists;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.math.Vector;

import java.util.List;

public class MDSDistributor implements Distributor {


    @Override
    public void distribute(ObservableList<XYChart.Series> chartData) {
        MultiDimensionalScalingEstimator estimator =
                new MultiDimensionalScalingEstimator(new SquaredEuclideanDistanceMeasure(), 2);

        RealMatrix matrix = estimator.MDSEstimation(getVectorsFromChart(chartData));
        setPositions(matrix, chartData);

    }

    private Vector[] getVectorsFromChart(ObservableList<XYChart.Series> chartData) {
        List<Vector> vectors = Lists.newArrayList();
        int index = 0;

        for(XYChart.Series series : chartData) {
            for(Object data : series.getData()) {
                if(data instanceof XYChart.Data) {
                    XYChart.Data xychartData = (XYChart.Data) data;

                    if(xychartData.getExtraValue() instanceof DisplayedPointEntity) {
                        DisplayedPointEntity entity = (DisplayedPointEntity) xychartData.getExtraValue();
                        vectors.add(index, entity.getCenter());
                        index++;
                    }
               }
            }
        }


        Vector[] vectorsArray = new Vector[vectors.size()];
        vectorsArray = vectors.toArray(vectorsArray);
        vectors = null;

        return vectorsArray;
    }

    private void setPositions(RealMatrix positionsMatrix, ObservableList<XYChart.Series> chartData) {
        int index = 0;

        for(XYChart.Series series : chartData) {
            for(Object data : series.getData()) {
                if(data instanceof XYChart.Data) {
                    XYChart.Data xychartData = (XYChart.Data) data;
                    ((XYChart.Data) data).setXValue(positionsMatrix.getEntry(index, 0));
                    ((XYChart.Data) data).setYValue(positionsMatrix.getEntry(index, 1));
                    index++;
                }
            }
        }
    }
}
