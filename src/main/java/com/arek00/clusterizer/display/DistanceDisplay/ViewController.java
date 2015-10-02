package com.arek00.clusterizer.display.DistanceDisplay;

import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.List;


public class ViewController {
    public Pane mainPane;
    public Canvas canvas;
    public Slider scaleSlider;
    public Label scaleValueLabel;
    public AnchorPane pointsPane;

    public void addCircles(List<ClusteredPointView> circles) {

        circles.stream().
                forEach(i -> pointsPane.getChildren().add(i.getShape()));

    }

    public void setScale(float scale) {
        pointsPane.setScaleX(scale);
        pointsPane.setScaleY(scale);
    }


    public void onScale(Event event) {

        scaleValueLabel.setText(Double.toString(scaleSlider.getValue()));
        setScale((float)scaleSlider.getValue());
    }
}
