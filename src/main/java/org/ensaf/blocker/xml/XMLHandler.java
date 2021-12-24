package org.ensaf.blocker.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;

public class XMLHandler extends DefaultHandler {
    public Project getProject() {
        return project;
    }

    private Project project = null;
    private Database database = null;
    private boolean isDatabaseTagOpen = false;
    private Link link = null;
    private boolean isLinkTagOpen = false;

    @Override
    public void startDocument() throws SAXException {
        project = new Project();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "project" -> project.setName(attributes.getValue("name"));
            case "database" -> {
                database = new Database();
                database.setName(attributes.getValue("name"));
                isDatabaseTagOpen = true;
            }
            case "link" -> {
                link = new Link();
                isLinkTagOpen = true;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (isLinkTagOpen) {
            database.addLinks(link);
            isLinkTagOpen = false;
        } else if (isDatabaseTagOpen) {
            project.addDatabase(database);
            isDatabaseTagOpen = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (isLinkTagOpen) {
            String file_path = new String(ch, start, length).trim();
            if (file_path.length() != 0) link.setFile(new File(file_path));
        }
    }
}
