package org.ensaf.blocker.xml;

import java.io.File;

public class Parser {
    public static void main(String[] args) {
        Project project = new Project();
        Database database = new Database();
        Link link = new Link();
        // Project
        project.setName("PFA Reports");
        // Database
        database.setName("File");
        // Link
        link.setFile(new File("C:\\test.pdf"));

        database.addLinks(link);
        project.addDatabase(database);
//        project.toXML();
    }
}
