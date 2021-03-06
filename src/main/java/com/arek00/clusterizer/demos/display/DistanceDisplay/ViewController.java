package com.arek00.clusterizer.demos.display.DistanceDisplay;

import com.arek00.clusterizer.Algorithms.MultidimensionalScaling.MultiDimensionalScalingEstimator;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Distribution.ClusterBasedDistributor;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Distribution.Distributor;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Distribution.MDSDistributor;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Distribution.ZeroPointDistributor;
import com.arek00.clusterizer.demos.display.DistanceDisplay.Points.ClusterCenter;
import javafx.event.ActionEvent;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewController {
    public static final String CLUSTERS_SERIES_NAME = "clusters";
    private static Logger logger = LogManager.getLogger(ViewController.class);

    public Pane mainPane;
    public AnchorPane pointsPane;
    public ScatterChart pointsScatteredChart;
    public Button zeroPointDistributionButton;
    public Button clustersBasedDistributionButton;
    public Button mdsDistributionButton;

    private boolean clustersDistributionEnabled = false;

    public void addDataToChart(List<XYChart.Data> data) {
        boolean isCluster = containsClusters(data);
        clustersDistributionEnabled = clustersDistributionEnabled || isCluster;
        clustersBasedDistributionButton.setDisable(!clustersDistributionEnabled);

        List<XYChart.Series> series = SeriesCreator.getSeries(data);

        series.stream()
                .forEach(serie -> {
                    pointsScatteredChart.getData().addAll(serie);
                });

        setTooltip(pointsScatteredChart.getData());
    }


    private boolean containsClusters(List<XYChart.Data> data) {
        if (!data.isEmpty()) {
            boolean containsClusters = (data.get(0).getExtraValue() instanceof ClusterCenter);
            logger.info("Data contains clusters: " + containsClusters);
            return containsClusters;
        }

        return false;
    }


    private void setTooltip(List<XYChart.Series> points) {
        logger.info("Install tooltips");

        points.forEach(serie -> {
            serie.getData().forEach(point -> {
                if (point instanceof XYChart.Data) {
                    XYChart.Data dataPoint = (XYChart.Data) point;
                    String accessibleHelpText = dataPoint.getExtraValue().toString();
                    Tooltip tooltip = new Tooltip(accessibleHelpText);
                    Tooltip.install(dataPoint.getNode(), tooltip);
                }
            });
        });
    }

    public void onZerPointDistributionClick(ActionEvent actionEvent) {
        logger.info("Zero point distribution button clicked");
        zeroPointDistributionButton.setDisable(true);
        mdsDistributionButton.setDisable(true);
        clustersBasedDistributionButton.setDisable(true);


        logger.info("Disabled button");
        Distributor distributor = new ZeroPointDistributor();

        logger.info("Run distribution.");
        distributor.distribute(pointsScatteredChart.getData());

        logger.info("Enable cluster based distribution if possible");

        mdsDistributionButton.setDisable(false);
        clustersBasedDistributionButton.setDisable(!(clustersDistributionEnabled && true));
    }

    public void onClusterBasedDistributionClick(ActionEvent actionEvent) {

        logger.info("Cluster based distribution button clicked.");
        zeroPointDistributionButton.setDisable(true);
        mdsDistributionButton.setDisable(true);
        clustersBasedDistributionButton.setDisable(true);

        logger.info("Disabled button");
        Distributor distributor = new ClusterBasedDistributor();

        logger.info("Run distribution");
        distributor.distribute(pointsScatteredChart.getData());

        logger.info("Enable zero point distribution button");

        mdsDistributionButton.setDisable(false);
        zeroPointDistributionButton.setDisable(false);
    }

    public void onMDSDistributionClick(ActionEvent actionEvent) {

        logger.info("Start MDS points distribution.");
        logger.info("Disabling buttons");
        mdsDistributionButton.setDisable(true);
        zeroPointDistributionButton.setDisable(true);
        clustersBasedDistributionButton.setDisable(true);

        Distributor distributor = new MDSDistributor();

        logger.info("Run MDS.");
        distributor.distribute(pointsScatteredChart.getData());

        zeroPointDistributionButton.setDisable(false);
        clustersBasedDistributionButton.setDisable(!(clustersDistributionEnabled && true));
    }
}

class SeriesCreator {

    public static List<XYChart.Series> getSeries(List<XYChart.Data> data) {
        Map<String, XYChart.Series> map = new HashMap<String, XYChart.Series>();

        if (data.isEmpty() || data == null) {
            return new ArrayList<XYChart.Series>();
        }

        data.stream().
                forEach(dataEntity -> {
                    String seriesName = getSeriesName(dataEntity);
                    createSeries(map, seriesName);
                    map.get(seriesName)
                            .getData()
                            .add(dataEntity);
                });

        return new ArrayList<XYChart.Series>(map.values());
    }

    private static String getSeriesName(XYChart.Data data) {
        String clusterName = "";
        if (data.getExtraValue() instanceof ClusterCenter) {
            clusterName = ViewController.CLUSTERS_SERIES_NAME;
        } else {
            clusterName = String.format("Cluster %d", data.getYValue());
        }

        return clusterName;
    }

    private static void createSeries(Map seriesMap, String seriesName) {
        if (!seriesMap.containsKey(seriesName)) {
            XYChart.Series serie = new XYChart.Series();
            serie.setName(seriesName);
            seriesMap.put(seriesName, serie);
        }
    }

}