package com.arek00.clusterizer.demos.display.DistanceDisplay.Distribution;

import com.arek00.clusterizer.Utils.VectorDistance;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.ClusterCenter;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.ClusteredPoint;
import com.arek00.clusterizer.demos.display.DistanceDisplay.ViewController;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lombok.NonNull;
import org.apache.commons.math.linear.EigenDecomposition;
import org.apache.commons.math.linear.EigenDecompositionImpl;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SparseMatrix;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.hadoop.decomposer.EigenVector;

/**
 * Cluster-Based distributor sets clusters position in traditional
 * way, putting them in distance from zero point.
 * <p>
 * Points position is based on distance between point and it cluster.
 */
public class ClusterBasedDistributor implements Distributor {


    private XYChart.Series clustersSeries = null;

    @Override
    public void distribute(@NonNull ObservableList<XYChart.Series> chartData) {
        clustersSeries = getClustersSeries(chartData);
        distributeClusters(clustersSeries);
        distributePoints(chartData);


    }

    private XYChart.Series getClustersSeries(ObservableList<XYChart.Series> chartData) {
        for (XYChart.Series series : chartData) {
            if (series.getName().equals(ViewController.CLUSTERS_SERIES_NAME)) {
                return series;
            }
        }

        throw new IllegalStateException("Chart's data doesn't contain cluster series.");
    }

    private void distributeClusters(XYChart.Series clustersSeries) {
        ZeroPointDistributor distributor = new ZeroPointDistributor();
        distributor.distributeSerie(clustersSeries);
    }

    private void distributePoints(ObservableList<XYChart.Series> chartData) {
        chartData.filtered(series ->
                !series.getName().equals(ViewController.CLUSTERS_SERIES_NAME))
                .forEach(seriesOfPoints -> distributeSeriesOfPoints(seriesOfPoints));
    }

    private void distributeSeriesOfPoints(XYChart.Series points) {
        points.getData()
                .forEach(point -> {

                    if (point instanceof XYChart.Data) {
                        distributePoint((XYChart.Data) point);
                    }
                });
    }

    private void distributePoint(XYChart.Data point) {

        ClusteredPoint clusteredPoint = null;
        if (!(point.getExtraValue() instanceof ClusteredPoint)) {
            return;
        } else {
            clusteredPoint = (ClusteredPoint) point.getExtraValue();
        }

        double xValue = calculatePointXValue(clusteredPoint, getClusterCentroid(clusteredPoint.getClusterId()));
        point.setXValue(xValue);
        point.setYValue(clusteredPoint.getClusterId());
    }

    private Vector getClusterCentroid(int clusterId) {
        ObservableList<XYChart.Data> clusters = clustersSeries.getData();

        for (XYChart.Data data : clusters) {
            if (data.getExtraValue() instanceof ClusterCenter) {
                ClusterCenter cluster = (ClusterCenter) data.getExtraValue();

                if (cluster.getClusterId() == clusterId) {
                    return cluster.getCenter();
                }
            }
        }

        throw new IllegalStateException("Couldn't find cluster with id " + clusterId);
    }

    private double calculatePointXValue(ClusteredPoint point, Vector clusterCentroid) {
        DistanceMeasure distanceMeasure = new SquaredEuclideanDistanceMeasure();
        Vector pointCenter = point.getCenter();

        double distanceFromCluster =
                VectorDistance.distance(clusterCentroid, pointCenter, distanceMeasure);

        double pointToZeroDistance = VectorDistance.distanceFromZero(pointCenter, distanceMeasure);
        double clusterToZeroDistance = VectorDistance.distanceFromZero(clusterCentroid, distanceMeasure);

        double pointPosition = (pointToZeroDistance > clusterToZeroDistance) ? clusterToZeroDistance + distanceFromCluster :
                clusterToZeroDistance - distanceFromCluster;

        return pointPosition;
    }

}
