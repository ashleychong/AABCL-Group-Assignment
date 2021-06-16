package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    TextField optionTextField;

    @FXML
    Label exceptionLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene2(ActionEvent event) throws IOException {

        try {
            int option = Integer.parseInt(optionTextField.getText());

            if (option < 0 || option > 4) {
                exceptionLabel.setText("Enter numbers [1-4] only");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/Scene2.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            }

        } catch (NumberFormatException e) {
            exceptionLabel.setText("Enter numbers only");
        } catch (Exception e) {
            exceptionLabel.setText("Error");
        }

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public String getOption() {
        return optionTextField.getText();
    }

}
