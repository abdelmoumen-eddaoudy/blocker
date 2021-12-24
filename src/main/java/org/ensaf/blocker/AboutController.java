package org.ensaf.blocker;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutController {
    @FXML
    private AnchorPane anchorPane;

    public void onActionClose() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}
