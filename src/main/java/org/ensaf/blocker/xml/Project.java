package org.ensaf.blocker.xml;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Project implements XMLTag {
    private StringProperty name = new SimpleStringProperty();
    private ObservableList<Database> databases = FXCollections.observableArrayList();

    public void addDatabase(Database database) {
        databases.add(database);
    }

    public ObservableList<Database> getDatabases() {
        return databases;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void copy(Project newProject) {
        setName(newProject.getName());
        databases.clear();
        for (Database database : newProject.databases) {
            Database database1 = new Database();
            database1.copy(database);
            addDatabase(database1);
        }
    }

    @Override
    public String toXMLString() {
        StringBuilder result = new StringBuilder("<project name=\"" + getName() + "\">\n");
        for (Database database : databases) {
            result.append(database.toXMLString()).append('\n');
        }
        result.append("</project>");
        return result.toString();
    }

    @Override
    public void toXML(File file) {
        try (FileOutputStream out = new FileOutputStream(file);) {
            // XML Header
            String header_tag = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
            out.write(header_tag.getBytes());
            out.write(toXMLString().getBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
