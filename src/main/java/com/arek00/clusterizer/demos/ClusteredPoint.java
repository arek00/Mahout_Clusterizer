package com.arek00.clusterizer.demos;

import lombok.Getter;

public class ClusteredPoint {

    private String dump;

    @Getter
    private float weight, distance;
    @Getter private String label;
    @Getter private int clusterNumber;

    public ClusteredPoint(String clusterNumber, String clusterDumpString) {
        doSetClusterNumber(clusterNumber);
        doSetDistance(clusterDumpString);
        doSetWeight(clusterDumpString);
        doSetLabel(clusterDumpString);

        this.dump = clusterDumpString;

    }

    private void doSetClusterNumber(String clusterNumber) {
        this.clusterNumber = Integer.parseInt(clusterNumber);
    }

    private void doSetWeight(String clusterDumpString) {
        String[] split = clusterDumpString.split(" ");
        this.weight = Float.parseFloat(split[1]);
        split = null;
    }

    private void doSetDistance(String clusterDumpString) {
        String[] split = clusterDumpString.split(" ");
        this.distance = Float.parseFloat(split[3]);
        split = null;
    }

    private void doSetLabel(String dumpString) {
        String[] split = dumpString.split("vec: ");
        split = split[1].split("=");
        this.label = split[0];
        split = null;
    }

}