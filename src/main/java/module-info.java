module com.alpha.blocker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.jetbrains.annotations;
    requires org.apache.pdfbox;
    requires java.xml;
    requires org.jfxtras.styles.jmetro;
    requires maven.model;
    requires plexus.utils;

    opens org.ensaf.blocker to javafx.fxml;
    exports org.ensaf.blocker;
    exports org.ensaf.blocker.xml;
}