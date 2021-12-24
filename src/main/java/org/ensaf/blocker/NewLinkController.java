package org.ensaf.blocker;

import org.ensaf.blocker.xml.Database;
import org.ensaf.blocker.xml.Link;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class NewLinkController implements Initializable {
    @FXML
    public TextField link_path;
    @FXML
    public ComboBox<Database> link_parent;
    @FXML
    public AnchorPane anchorPane;
    // Internal Applets
    private FileChooser fileChooser;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initializing Internal Applets
        fileChooser = new FileChooser();

        // Events Handler
        MainController.project.getDatabases().addListener((ListChangeListener<Database>) change -> {
            link_parent.setItems(MainController.project.getDatabases());
        });
        link_parent.setItems(MainController.project.getDatabases());
    }


    public void onActionApply(ActionEvent event) {
        Database database = link_parent.getSelectionModel().getSelectedItem();
        Link link = new Link();
        link.setFile(new File(link_path.getText()));
        ListChangeListener<Link> listener = change -> {
            TreeItem<String> selectedBranchItem = null;
            for (TreeItem<String> branchItem : MainController.root1.getChildren()) {
                if (Objects.equals(branchItem.getValue(), database.toString()))
                    selectedBranchItem = branchItem;
            }
            if (selectedBranchItem != null) {
                selectedBranchItem.getChildren().add(new TreeItem<>(link.getFileProperty().get().getName(), new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/pdf.png"))))));
                selectedBranchItem.setExpanded(true);
            }
        };

        database.getLinks().addListener(listener);
        database.addLinks(link);
        database.getLinks().removeListener(listener);
        close();
    }

    public void onActionCancel(ActionEvent event) {
        close();
    }

    public void onActionBrowse() {
        fileChooser.setTitle("Importing Document");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // Adobe PDF files (Filter)
        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("Adobe PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(filter1);
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File chosenFile = fileChooser.showOpenDialog(stage);
        if (chosenFile != null) {
            String filename = chosenFile.getName();
            link_path.setText(chosenFile.getPath());
        }
    }


    public void close() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}
