package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    public Button submitBtn;
    public TextField userOption;
    public Label option;
    public Text exceptionText;
    private Stage stage;
    private Scene scene;
    private Parent root;
//    public void redirectToSelectedOption(ActionEvent actionEvent) {
//        MuseumController.redirectToSelectedOption(userOption.getText());
//    }

    public void switchToMuseumScene(ActionEvent event) throws IOException {
        try {
            if (userOption.getText().isEmpty()) {
                exceptionText.setText("Enter number [1-4]");
            } else {
                int option = Integer.parseInt(userOption.getText());
                System.out.println(option);
                if (option < 1 || option > 4) {
                    exceptionText.setText("Enter number [1-4] only");
                } else {
//                    System.out.println("s");
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/scenes/MuseumScene.fxml"));
                    root = loader.load();
                    scene = new Scene(root);

                    //access the controller and call a method
                    MuseumSceneController controller = loader.getController();
                    controller.initData(userOption);

                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } catch (NumberFormatException e) {
            exceptionText.setText("Enter numbers only");
        } catch (Exception e) {
            System.out.println(e);
            exceptionText.setText("Error");
        }
    }
}
