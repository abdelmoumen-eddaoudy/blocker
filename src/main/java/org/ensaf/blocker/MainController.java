package org.ensaf.blocker;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.*;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.ensaf.blocker.engine.Paragraph;
import org.ensaf.blocker.engine.Plagiarism;
import org.ensaf.blocker.xml.Database;
import org.ensaf.blocker.xml.Link;
import org.ensaf.blocker.xml.Project;
import org.ensaf.blocker.xml.XMLHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @FXML
    public WebView webViewPreview;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private TreeView<String> treeView1;
    @FXML
    private TreeView<String> reportTree;
    @FXML
    private Button addFolder;
    @FXML
    private Button start;
    @FXML
    private Button stop;
    @FXML
    private Button addFile;
    @FXML
    private Button upFolder;
    @FXML
    private ProgressBar progressbar;


    // TreeItems (Structure)
    static public TreeItem<String> root1;
    // TreeItems (Summary Report)
    private TreeItem<String> root2;
    static public Project project = null;
    Stage newProjectStage;
    Stage newLinkStage;
    Stage aboutStage;

    // Internal Applets
    FileChooser fileChooser = new FileChooser();
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File chosenFile = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initializing TreeView (Structure)
        root1 = new TreeItem<>("default", new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/structure.png")))));
        root1.setExpanded(true);

        // Init TreeView (Summary Report)
        root2 = new TreeItem<>("Summary");
        root2.setExpanded(true);

        treeView1.setRoot(root1);
        reportTree.setRoot(root2);

        // Setting ICONS for Toolbar Buttons
        addFile.setGraphic(new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/addFile.png")))));
        addFolder.setGraphic(new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/addFolder.png")))));
        start.setGraphic(new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/start.png")))));
        upFolder.setGraphic(new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/uploadFolder.png")))));
        stop.setGraphic(new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/stop.png")))));
        // Initializing Properties (Structure)
        // Initializing Properties (Summary)
        reportTree.setStyle("-fx-font-size: 15;");

        // Project
        project = new Project();
        project.getNameProperty().addListener((observableValue, s, t1) -> MainController.root1.setValue(t1));


        project.getDatabases().addListener((ListChangeListener<Database>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    Database newDb = change.getAddedSubList().get(0);
                    TreeItem<String> branchItem = new TreeItem<>(newDb.toString(), new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/folder.png")))));
                    MainController.root1.getChildren().add(branchItem);
                }
            }
        });
        project.setName("default");


    }

    public void onActionNewEmptyFolder() throws IOException {
        loadStageAsDialog(new Stage(), "New Folder", "new-database-view.fxml", mainPane.getScene().getWindow());
    }

    public void onActionUploadFile() throws IOException {
        if (newLinkStage == null) {
            Stage mainStage = (Stage) mainPane.getScene().getWindow();
            newLinkStage = new Stage();
            loadStageAsDialog(newLinkStage, "Upload File", "new-link-view.fxml", mainStage);
            return;
        }
        newLinkStage.show();

    }

    public void onActionClose() {
        Platform.exit();
    }

    public void onActionUploadFolder() {

        Stage mainStage = (Stage) mainPane.getScene().getWindow();
        directoryChooser.setTitle("Upload Folder");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File chosenDir = directoryChooser.showDialog(mainStage);
        if (chosenDir != null) {
            try {
                List<Path> paths = Handlers.listFiles(Paths.get(chosenDir.getPath()));
                Database database = new Database();
                database.setName(chosenDir.getName());
                project.addDatabase(database);
                ListChangeListener<Link> listener = change -> {
                    while (change.next()) {
                        TreeItem<String> folderItem = MainController.root1.getChildren().get(MainController.root1.getChildren().size() - 1);
                        TreeItem<String> newFileItem = new TreeItem<>(change.getAddedSubList().get(0).getFile().getName(), new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/pdf.png")))));
                        folderItem.getChildren().add(newFileItem);
                    }
                };
                database.getLinks().addListener(listener);
                for (Path path : paths) {
                    Link link = new Link();
                    link.setFile(path.toFile());
                    database.addLinks(link);
                }
                database.getLinks().removeListener(listener);

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void onActionStart() {
        progressbar.setVisible(true);
        PlagiarismService service = new PlagiarismService();
        service.start();
    }

    public void onActionNewProject() throws IOException {
        if (newProjectStage == null) {
            Stage mainStage = (Stage) mainPane.getScene().getWindow();
            newProjectStage = new Stage();
            loadStageAsDialog(newProjectStage, "New Project", "new-project-view.fxml", mainStage);
        } else {
            newProjectStage.show();
        }
    }

    public void onActionOpenProject() {
        XMLHandler xmlHandler = new XMLHandler();
        try {
            fileChooser.setTitle("Importing Document");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            // XML files (Filter)
            FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(filter1);
            Stage stage = (Stage) mainPane.getScene().getWindow();
            chosenFile = fileChooser.showOpenDialog(stage);
            if (chosenFile != null) {
                SAXParser saxParser = (SAXParserFactory.newInstance()).newSAXParser();
                saxParser.parse(chosenFile, xmlHandler);
                project.copy(xmlHandler.getProject());
                loadProject();
            }
        } catch (SAXException | IOException | ParserConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    public void onActionSaveProject() {
        if (chosenFile != null) {
            project.toXML(chosenFile);
        } else {
            fileChooser.setTitle("Saving Document");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            // XML files (Filter)
            FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(filter1);
            Stage stage = (Stage) mainPane.getScene().getWindow();
            chosenFile = fileChooser.showSaveDialog(stage);
            if (chosenFile != null) {
                project.toXML(chosenFile);
            }
        }
    }

    public void onActionAbout() throws IOException {
        if (aboutStage == null) {
            Stage mainStage = (Stage) mainPane.getScene().getWindow();
            aboutStage = new Stage();

            loadStageAsDialog(aboutStage, "About", "about-view.fxml", mainStage);
        } else {
            aboutStage.show();
        }
    }

    public void loadProject() {
        // Applying to the Tree
        root1.getChildren().clear();
        root1.setValue(project.getName());
        TreeItem<String> branchItem, leafItem;
        for (Database database : project.getDatabases()) {
            branchItem = new TreeItem<>(database.toString(), new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/folder.png")))));
            root1.getChildren().add(branchItem);
            for (Link link : database.getLinks()) {
                leafItem = new TreeItem<>(link.getFile().getName(), new ImageView(new Image(String.valueOf(MainApplication.class.getResource("icons/pdf.png")))));
                branchItem.getChildren().add(leafItem);
            }
        }
    }

    public void loadStageAsDialog(@NotNull Stage stage, String title, String name, Window mainStage) throws
            IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(name));
        Scene scene = new Scene(fxmlLoader.load());
        JMetro jMetro = new JMetro(scene, Style.LIGHT);
        stage.initOwner(mainStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private class PlagiarismService extends Service<ArrayList<ArrayList<Plagiarism>>> {
        ArrayList<String> filenames = new ArrayList<>();

        private PlagiarismService() {
            setOnSucceeded(event -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Getting Data from the Service Task
                final ArrayList<ArrayList<Plagiarism>> result = (ArrayList<ArrayList<Plagiarism>>) event.getSource().getValue();

                // Initializing root2[TreeView] Values
                MainController.this.root2.getChildren().clear();

                TreeItem<String> branchItem;
                int j = 0;
                StringBuilder previewHTML = new StringBuilder("<link rel=\"stylesheet\" href=\"" + getClass().getResource("css/style.css") + "\">");
                previewHTML.append("<div class=\"container\">");
                previewHTML.append("<div class=\"items\">");
                previewHTML.append("""
                        <div class="items-head">
                            <p>DETAILS</p>
                            <hr>
                        </div>
                        """);
                previewHTML.append("<div class=\"items-body\">");
                for (ArrayList<Plagiarism> plagiarisms : result) {
                    previewHTML.append("<div class=\"items-body-content\">");
                    // var [filenames] - the target filename
                    branchItem = new TreeItem<>(filenames.get(j));

                    previewHTML.append("<div><h3 style=\"font-family:'Lucida console';color:#5f9ea0;'\"> Document name : ").append(branchItem.getValue()).append("</h3>");
                    MainController.this.root2.getChildren().add(branchItem);
                    int i = 1;
                    int totalPercentage = 0;
                    for (Plagiarism plagiarism : plagiarisms) {
                        totalPercentage += plagiarism.getPercentage();
                        // BODY
                        previewHTML.append("<span style=\"font-size:14px\">");
                        previewHTML.append(plagiarism.getText());
                        previewHTML.append("</span>");
                        previewHTML.append("<span style=\"font-size:14px\">");
                        previewHTML.append("&nbsp;&#8211;&nbsp;");
                        previewHTML.append("</span>");

                        i++;
                    }
                    branchItem.setValue(branchItem.getValue() + " - " + totalPercentage + "%");
                    previewHTML.append("</div></div>");
                    j++;
                }
                previewHTML.append("</div>");
                previewHTML.append("</div>");
                previewHTML.append("</div>");
                WebEngine enginePreview = MainController.this.webViewPreview.getEngine();
                enginePreview.loadContent(previewHTML.toString());
                //enginePreview.load(getClass().getResource("test/test.html").toExternalForm());

                progressbar.setVisible(false);
            });
        }

        @Contract(value = " -> new", pure = true)
        @Override
        protected @NotNull Task<ArrayList<ArrayList<Plagiarism>>> createTask() {
            return new Task<>() {
                @Override
                protected ArrayList<ArrayList<Plagiarism>> call() {
                    ObservableList<Database> databases = project.getDatabases();
                    ArrayList<ArrayList<Plagiarism>> result = new ArrayList<>();
                    File file1 = databases.get(0).getLinks().get(0).getFile();
                    for (Database database : databases) {
                        ObservableList<Link> links = database.getLinks();
                        for (Link link : links) {
                            File file2 = link.getFile();
                            if (file2.equals(file1))
                                continue;
                            filenames.add(file2.getName());
                            try (PDDocument loader1 = PDDocument.load(file1); PDDocument loader2 = PDDocument.load(file2)) // try with-resource (Initiate the PDFBox library)
                            {
                                PDFTextStripper txtStripper1 = new PDFTextStripper();

                                String content1 = txtStripper1.getText(loader1);
                                String content2 = txtStripper1.getText(loader2);

                                // Checking for plagiarism (Inverted Problem Not solved yet)
                                if (content1.length() < content2.length()) {
                                    result.add(Paragraph.check(content1, content2));
                                } else
                                    result.add(Paragraph.check(content2, content1));
                            } catch (IOException exception) {
                                // TODO Replace with a GUI Message.
                                System.out.println("I/O Error : " + exception);
                            }
                        }
                    }
                    return result;
                }
            };
        }
    }
}
