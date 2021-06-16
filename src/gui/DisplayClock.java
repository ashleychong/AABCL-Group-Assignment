package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;

public class DisplayClock extends Application {
    @Override
    public void start(Stage primaryStage) {
        ClockPane clock = new ClockPane(12,30,5);
        String timeString = clock.getHour() + ":" + clock.getMinute() + ":" + clock.getSecond();
        Label labelCurrentTime = new Label(timeString);

        BorderPane pane = new BorderPane();
        pane.setCenter(clock);
//        pane.setBottom(labelCurrentTime);
//        BorderPane.setAlignment(labelCurrentTime, Pos.TOP_CENTER);

        Scene scene = new Scene(pane, 250, 250);
        primaryStage.setTitle("DisplayClock");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
