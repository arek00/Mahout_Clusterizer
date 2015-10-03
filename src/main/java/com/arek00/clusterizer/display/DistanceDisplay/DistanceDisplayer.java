package com.arek00.clusterizer.display.DistanceDisplay;

import com.arek00.clusterizer.SequenceFileUtils.SequencePrinter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DistanceDisplayer extends Application {

    private static String[] clusteredPoints;

    public static void main(String[] args) {

        DistanceDisplayer.clusteredPoints = args;

        if (clusteredPoints.length < 1) {
            displayHelp();
        } else {
            launch(args);
        }
    }

    private static void displayHelp() {
        System.out.println("Pass file paths as arguments.");
    }


    @Override
    public void start(Stage primaryStage) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/style.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);

            ViewController controller = loader.getController();


            Arrays.stream(DistanceDisplayer.clusteredPoints).
                    forEach(each -> {
                        addDataToChart(controller, each);
                    });

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDataToChart(ViewController controller, String pointsFilePath) {
        Path pointsSequenceFile = new Path(pointsFilePath);

        controller.addDataToChart(
                SequencePrinter.getPoints(pointsSequenceFile).
                        stream().
                        map(point -> ClusteredPointDataConverter.getData(point)).
                        collect(Collectors.toList())
        );


    }
}
