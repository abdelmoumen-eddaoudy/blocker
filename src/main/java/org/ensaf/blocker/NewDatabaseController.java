package org.ensaf.blocker;

import org.ensaf.blocker.xml.Database;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewDatabaseController implements Initializable {
    @FXML
    public TextField database_name;

    @FXML
    public AnchorPane anchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        database_name.setFocusTraversable(false);
        Platform.runLater(() -> {
            database_name.requestFocus();
            database_name.selectAll();
        });
    }
    public void onActionApply(ActionEvent event) {
        Database database = new Database();
        database.setName(database_name.getText());
        MainController.project.addDatabase(database);
        onActionCancel(event);
        close();
    }

    public void onActionCancel(ActionEvent event) {
        close();
    }

    public void close() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}
