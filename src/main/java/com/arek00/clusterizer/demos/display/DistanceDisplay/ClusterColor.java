package com.arek00.clusterizer.demos.display.DistanceDisplay;


import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClusterColor {

    private static List<Color> color = new ArrayList<Color>();

    public static Color getClusterColor(int clusterNumber) {
        while (color.size() <= clusterNumber) {
            color.add(getRandomColor());
        }

        return color.get(clusterNumber);
    }

    private static Color getRandomColor() {
        double red, green, blue;
        Random random = new Random();



        red = (Math.abs(random.nextInt()) % 10) / 10d;
        green = (Math.abs(random.nextInt()) % 10) / 10d;
        blue = (Math.abs(random.nextInt()) % 10) / 10d;

        return new Color(red, green, blue, 1.0);

    }


}
