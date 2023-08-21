package org.gamereact;


import javafx.util.Duration;

import org.engine.Command;
import org.engine.TangibleObject;
import org.gamereact.component.Track;
import org.gamereact.module.*;
import org.gamereact.module.Module;
import org.gamereact.module.electronic.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;

public class CreateModuleCommand {

    private static TangibleObject tangibleObject;
    private static Node node;
    private static final Map<String, Command> MODULES;

    static {

        MODULES = Map.ofEntries(
                Map.entry("AUDIO_PLAYER_MODULE", new Command() {
                    @Override
                    public Module create(TangibleObject tangibleObject) {
                        AudioPlayerModuleBuilder moduleBuilder = new AudioPlayerModuleBuilder(tangibleObject);
                        String fileName = ((Element) node).getAttribute("file");
                        String title = ((Element) node).getAttribute("title");
                        moduleBuilder
                                .setTitle(title)
                                .setFile(fileName)
                        ;
                        NodeList parts = ((Element) node).getElementsByTagName("part");
                        for (int k = 0; k < parts.getLength(); k++) {
                            Node part = parts.item(k);
                            String partName = ((Element) part).getAttribute("name");
                            int startDuration = Integer.parseInt(((Element) part).getAttribute("start"));
                            int endDuration = Integer.parseInt(((Element) part).getAttribute("end"));
                            moduleBuilder.addTrack(new Track(partName, new Duration(startDuration), new Duration(endDuration)));
                        }
                        return moduleBuilder.createAudioPlayerModule();
                    }
                }),
                Map.entry("IMAGE_MODULE", new Command() {
                    @Override
                    public Module create(TangibleObject tangibleObject) {
                        String title = ((Element) node).getAttribute("title");
                        ImageModuleBuilder imageModuleBuilder = new ImageModuleBuilder().setTangibleObject(tangibleObject);
                        imageModuleBuilder.setTitle(title);
                        NodeList images = ((Element) node).getElementsByTagName("image");
                        for (int k = 0; k < images.getLength(); k++) {
                            Node image = images.item(k);
                            String imageName = ((Element) image).getAttribute("name");
                            String imageFile = ((Element) image).getAttribute("file");
                            imageModuleBuilder.addImage(imageFile, imageName);
                        }
                        return imageModuleBuilder.createImageModule();
                    }
                }),
                Map.entry("VOLUME_CONTROL_MODULE", VolumeControlModule::new),
                Map.entry("AXIS_SCROLL_CONTROL_MODULE", AxisScrollControlModule::new),
                Map.entry("ROTATION_SIGNAL_OUTPUT_MODULE", RotationSignalOutputModule::new),
                Map.entry("ARDUINO_CONTROL_MODULE", ArduinoControlModule::new),
                Map.entry("LED_COMPONENT_MODULE", LEDModule::new),
                Map.entry("TRANSISTOR_COMPONENT_MODULE", TransistorModule::new),
                Map.entry("RESISTOR_COMPONENT_MODULE", ResistorModule::new),
                Map.entry("CAPACITOR_COMPONENT_MODULE", CapacitorModule::new),
                Map.entry("COIL_COMPONENT_MODULE", CoilModule::new),
                Map.entry("INDUCTOR_COMPONENT_MODULE", InductorModule::new),
                Map.entry("BATTERY_COMPONENT_MODULE", BatteryModule::new),
                Map.entry("CHART_MODULE", ChartModule::new)
        );
    }

    public CreateModuleCommand(TangibleObject tanObj) {
        tangibleObject = tanObj;
    }

    public Module createModule(Node objectNode) {
        node = objectNode;
        String moduleName = ((Element) objectNode).getAttribute("class");
        Command command = MODULES.get(moduleName);

        if (command == null) {
            throw new IllegalArgumentException("Ungültiger Modulname: " + moduleName);
        }

        return command.create(tangibleObject);
    }

}
