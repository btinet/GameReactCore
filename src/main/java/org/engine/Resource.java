package org.engine;

import javafx.util.Duration;
import org.gamereact.module.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;

public class Resource {

    public static final String slash = System.getProperty("file.separator");
    public static final String resources = "." + slash;
    private String config_file = resources + "marker.xml";

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
                System.out.println("error parsing configuration file");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error reading configuration file");
        }

        assert doc != null;
        NodeList objectNodes = doc.getElementsByTagName("object");
        for (int i = 0; i < objectNodes.getLength(); i++) {
            AudioPlayerModuleBuilder moduleBuilder = new AudioPlayerModuleBuilder(tangibleObject);
            Node objectNode = objectNodes.item(i);
            int markerId = Integer.parseInt(((Element) objectNode).getAttribute("id"));
            String modelClassName = ((Element) objectNode).getAttribute("class");
            String fileName = ((Element) objectNode).getAttribute("file");
            String title = ((Element) objectNode).getAttribute("title");

            if (id == markerId) {
                System.out.printf("Marker %s ist ein %s mit dem Titel '%s'%n", markerId, modelClassName, title);
                System.out.printf("Eine Datei ist angehängt: %s%n%n", fileName);

                System.out.println("Folgende Abschnitte sind vorhanden:");
                NodeList parts = ((Element) objectNode).getElementsByTagName("part");

                switch (modelClassName) {
                    case "AUDIO_PLAYER_MODULE":
                        moduleBuilder
                                .setTitle(title)
                                .setFile(fileName)
                        ;
                        for (int k = 0; k < parts.getLength(); k++) {
                            Node part = parts.item(k);
                            String partName = ((Element) part).getAttribute("name");
                            int startDuration = Integer.parseInt(((Element) part).getAttribute("start"));
                            int endDuration = Integer.parseInt(((Element) part).getAttribute("end"));
                            moduleBuilder.addTrack(new Track(partName, new Duration(startDuration), new Duration(endDuration)));
                        }
                        return moduleBuilder.createAudioPlayerModule();
                    case "VOLUME_CONTROL_MODULE":
                        return new VolumeControlModule(tangibleObject);
                    case "AXIS_SCROLL_CONTROL_MODULE":
                        return new AxisScrollControlModule(tangibleObject);
                    case "ROTATION_SIGNAL_OUTPUT_MODULE":
                        return new RotationSignalOutputModule(tangibleObject);
                    case "CHART_MODULE":
                        return new ChartModule(tangibleObject);
                }

            }

        }
        return new EmptyModule(tangibleObject);
    }

}
