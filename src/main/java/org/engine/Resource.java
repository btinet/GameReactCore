package org.engine;

import org.gamereact.CreateModuleCommand;
import org.gamereact.module.*;
import org.gamereact.module.Module;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Resource {

    public static final String slash = System.getProperty("file.separator");
    public static final String resources = "." + slash;
    private final String config_file = resources + "marker.xml";

    private final TangibleObject tangibleObject;

    public Resource(TangibleObject tangibleObject) {
        this.tangibleObject = tangibleObject;
    }

    public Module readConfig(int id) {
        Document doc = null;
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(new File(config_file));

            String docType = doc.getDocumentElement().getNodeName();
            if (!docType.equalsIgnoreCase("marker")) {
                System.out.println("Root-Node 'marker' ist nicht vorhanden!");
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Einlesen von marker.xml");
        }

        assert doc != null;
        NodeList objectNodes = doc.getElementsByTagName("object");
        for (int i = 0; i < objectNodes.getLength(); i++) {
            Node objectNode = objectNodes.item(i);
            int markerId = Integer.parseInt(((Element) objectNode).getAttribute("id"));
            if (id == markerId) {
                CreateModuleCommand command = new CreateModuleCommand(tangibleObject);
                return command.createModule(objectNode);
            }
        }
        return new EmptyModule(tangibleObject);
    }

}
