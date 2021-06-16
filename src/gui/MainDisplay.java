package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class MainDisplay extends Application {

    Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../scenes/Scene.fxml")));
        window.setTitle("Welcome to Museum AABCL");
        window.setScene(new Scene(root, 500, 300));
        window.show();

//        ProgressIndicator progressIndicator = new ProgressIndicator();
//        progressIndicator.progressProperty().addListener((ov, oldValue, newValue) -> {
//            Text text = (Text) progressIndicator.lookup(".percentage");
//            if(text!=null && text.getText().equals("Done")){
//                text.setText("Full!");
//                progressIndicator.setPrefWidth(text.getLayoutBounds().getWidth());
//            }
//        });
    }

}