package org.ensaf.blocker;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class NewProjectController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button apply;
    @FXML
    private Button cancel;
    @FXML
    private TextField project_name;

    public NewProjectController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        project_name.setFocusTraversable(false);
        Platform.runLater(() -> {
            project_name.requestFocus();
            project_name.selectAll();
        });
    }

    public void onActionApply(ActionEvent event) {
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        MainController.project.setName(project_name.getText());
        currentStage.close();
    }

    public void onActionCancel(ActionEvent event) {
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        currentStage.close();
    }
}
