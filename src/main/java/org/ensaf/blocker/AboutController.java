package org.ensaf.blocker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    private AnchorPane anchorPane;
    @FXML
    public Label current_version;
    @FXML
    private Label app_version;


    public void onActionClose() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
    // TODO [CRITICAL] get the current application version from Github
    // TODO [CRITICAL] make the 'Update' button working in the next update

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = null;
        try {
            // BUG [CRITICAL] Relaying on pom.xml to get information version will generate a runtime error in the JAR artifact
            model = reader.read(new FileReader("pom.xml"));
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        assert model != null;
        app_version.setText(app_version.getText() + "\t\t" + model.getVersion());
    }
}
