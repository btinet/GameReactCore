package org.engine;

import org.gamereact.ModuleLibrary;
import org.gamereact.module.*;
import org.gamereact.module.Module;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Loads and processes the marker configuration file
 */
public class Resource {

    public static final String slash = System.getProperty("file.separator");
    public static final String resources = "." + slash;

    /**
     * file name of the marker config file
     */
    private final String config_file = resources + "marker.xml";

    /**
     * the TangibleObject reference to be passed to the ModuleLibrary
     */
    private final TangibleObject tangibleObject;

    /**
     * Constructor
     * @param tangibleObject TangibleObject reference
     */
    public Resource(TangibleObject tangibleObject) {
        this.tangibleObject = tangibleObject;
    }

    /**
     * parses the marker config file and tries to find an object node matching the TuioObject id
     * @param id current TuioObject id reference
     * @return returns a ModuleObject set up in ModuleLibrary. If no Module has been configured, an empty Module will
     * be returned.
     */
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
                ModuleLibrary moduleLibrary = new ModuleLibrary(tangibleObject);
                return moduleLibrary.createModule(objectNode);
            }
        }
        return new EmptyModule(tangibleObject);
    }

}
