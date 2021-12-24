package org.ensaf.blocker.xml;

import java.io.File;

public interface XMLTag {
    String toXMLString();
    void toXML(
            File file
    );
}
