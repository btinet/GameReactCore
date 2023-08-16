package org.engine;

import javafx.util.Duration;
import org.gamereact.component.Track;
import org.gamereact.module.*;
import org.gamereact.module.Module;
import org.gamereact.module.electronic.*;
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
            e.printStackTrace();
            System.out.println("Fehler beim Einlesen von marker.xml");
        }

        assert doc != null;
        NodeList objectNodes = doc.getElementsByTagName("object");
        for (int i = 0; i < objectNodes.getLength(); i++) {

            Node objectNode = objectNodes.item(i);
            int markerId = Integer.parseInt(((Element) objectNode).getAttribute("id"));
            String modelClassName = ((Element) objectNode).getAttribute("class");
            String fileName = ((Element) objectNode).getAttribute("file");
            String title = ((Element) objectNode).getAttribute("title");

            if (id == markerId) {
                System.out.printf("Marker %s ist ein %s mit dem Titel '%s'%n", markerId, modelClassName, title);

                if(fileName.length() > 0) {
                    System.out.printf("Eine Datei ist angehängt: %s%n", fileName);
                }

                NodeList parts = ((Element) objectNode).getElementsByTagName("part");

                if(parts.getLength() > 0) {
                    System.out.println("Folgende Abschnitte sind vorhanden:");
                }

                switch (modelClassName) {
                    case "AUDIO_PLAYER_MODULE":
                        AudioPlayerModuleBuilder moduleBuilder = new AudioPlayerModuleBuilder(tangibleObject);
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
                            System.out.println(partName + ", Start: " + startDuration + " | Ende: " + endDuration);
                        }
                        return moduleBuilder.createAudioPlayerModule();
                    case "VOLUME_CONTROL_MODULE":
                        return new VolumeControlModule(tangibleObject);
                    case "AXIS_SCROLL_CONTROL_MODULE":
                        return new AxisScrollControlModule(tangibleObject);
                    case "ROTATION_SIGNAL_OUTPUT_MODULE":
                        return new RotationSignalOutputModule(tangibleObject);
                    case "LED_COMPONENT_MODULE":
                        return new LEDModule(tangibleObject);
                    case "TRANSISTOR_COMPONENT_MODULE":
                        return new TransistorModule(tangibleObject);
                    case "RESISTOR_COMPONENT_MODULE":
                        return new ResistorModule(tangibleObject);
                    case "CAPACITOR_COMPONENT_MODULE":
                        return new CapacitorModule(tangibleObject);
                    case "COIL_COMPONENT_MODULE":
                        return new CoilModule(tangibleObject);
                    case "INDUCTOR_COMPONENT_MODULE":
                        return new InductorModule(tangibleObject);
                    case "CHART_MODULE":
                        return new ChartModule(tangibleObject);
                    case "IMAGE_MODULE":
                        ImageModuleBuilder imageModuleBuilder =  new ImageModuleBuilder().setTangibleObject(tangibleObject);
                        imageModuleBuilder.setTitle(title);
                        NodeList images = ((Element) objectNode).getElementsByTagName("image");
                        for (int k = 0; k < images.getLength(); k++) {
                            Node image = images.item(k);
                            String imageName = ((Element) image).getAttribute("name");
                            String imageFile = ((Element) image).getAttribute("file");
                            imageModuleBuilder.addImage(imageFile,imageName);
                        }
                        return imageModuleBuilder.createImageModule();
                }

            }

        }
        return new EmptyModule(tangibleObject);
    }

}
