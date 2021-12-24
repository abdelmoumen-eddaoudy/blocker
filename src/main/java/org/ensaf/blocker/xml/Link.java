package org.ensaf.blocker.xml;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Link implements XMLTag {
    private ObjectProperty<File> file = new SimpleObjectProperty<>();

    public Link() {

    }

    public ObjectProperty<File> getFileProperty() {
        return file;
    }

    public File getFile() {
        return file.get();
    }

    public void setFile(File file) {
        this.file.set(file);
    }

    public void copy(@NotNull Link newLink) {
        this.setFile(newLink.getFile());
    }

    @Override
    public String toXMLString() {
        return "<link>" + getFile().getPath() + "</link>";
    }

    @Override
    public void toXML(File file) {

    }
}
