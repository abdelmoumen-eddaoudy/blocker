package org.ensaf.blocker.xml;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;


public class Database implements XMLTag {
    protected StringProperty id = new SimpleStringProperty();
    protected StringProperty name = new SimpleStringProperty();
    protected ObservableList<Link> links = FXCollections.observableArrayList();

    public StringProperty getIdProperty() {
        return id;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public ObservableList<Link> getLinks() {
        return links;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void addLinks(Link link) {
        links.add(link);
    }

    public void copy(Database newDatabase) {
        links.clear();
        setName(newDatabase.getName());
        for (Link link: newDatabase.getLinks()) {
            Link link1 = new Link();
            link1.copy(link);
            links.add(link1);
        }
    }

    @Override
    public String toXMLString() {
        StringBuilder result = new StringBuilder("\t<database " + "name=\"" + getName() + "\">\n");
        for (Link link : links) {
            result.append("\t\t").append(link.toXMLString()).append('\n');
        }
        result.append("\t</database>");
        return result.toString();

    }


    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public void toXML(File file) {

    }

}