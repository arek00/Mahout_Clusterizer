package com.arek00.clusterizer.demos.display.DistanceDisplay;

import com.arek00.clusterizer.demos.ClusteredPoint;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import lombok.Getter;
import lombok.NonNull;


public class ClusteredPointView {

    @Getter private Shape shape;
    private ClusteredPoint point;
    private float scale = 1.0f;

    public ClusteredPointView(@NonNull ClusteredPoint point) {
        this.shape = initializeShape(point);
    }


    private Shape initializeShape(ClusteredPoint point) {
        Circle circle = new Circle();
        circle.setLayoutX(point.getDistance());
        circle.setLayoutY(100d);
        circle.setCenterX(point.getDistance());
        circle.setCenterY(100d);
        circle.setRadius(point.getWeight() * scale);
        circle.setAccessibleText(point.getLabel());
        Paint circlePaint = ClusterColor.getClusterColor(point.getClusterNumber());
        circle.setFill(circlePaint);


        System.out.println(circle.getCenterX() + " ");
        return circle;
    }
}
