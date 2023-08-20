package org.gamereact.module;

import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.engine.Controller;
import org.engine.FingerTouchObject;
import org.engine.TangibleObject;
import org.gamereact.module.electronic.ElectronicPort;
import org.gamereact.module.electronic.PCBLaneModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ElectronicComponentModule extends Module {

    private final SimpleDoubleProperty voltage = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty current = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty resistence = new SimpleDoubleProperty(0);

    protected HashMap<ElectronicPort,ArrayList<Module>> ports;
    protected ArrayList<PCBLaneModule> laneModules = new ArrayList<>();
    protected final ImageView iconImageView = new ImageView();

    private boolean connected = false;

    public ElectronicComponentModule(TangibleObject tangibleObject, int availablePorts, String iconName) {
        super(tangibleObject);
        ports = new HashMap<>(availablePorts);
        try {
            Image iconImage = new Image(Objects.requireNonNull(this.getClass().getResource("/org/electronics/1x/" + iconName +".png")).openStream());
            iconImageView.setImage(iconImage);
            iconImageView.setPreserveRatio(true);
            iconImageView.setFitWidth(64);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        iconImageView.setTranslateY(-32);
        getChildren().add(iconImageView);
    }

    public double getVoltage() {
        return voltage.get();
    }

    public SimpleDoubleProperty voltageProperty() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage.set(voltage);
    }

    public double getCurrent() {
        return current.get();
    }

    public SimpleDoubleProperty currentProperty() {
        return current;
    }

    public void setCurrent(double current) {
        this.current.set(current);
    }

    public double getResistence() {
        return resistence.get();
    }

    public SimpleDoubleProperty resistenceProperty() {
        return resistence;
    }

    public void setResistence(double resistence) {
        this.resistence.set(resistence);
    }

    public ArrayList<Module> getModulesOnPort(ElectronicPort port) {
        return ports.get(port);
    }

    public HashMap<ElectronicPort, ArrayList<Module>> getPorts() {
        return ports;
    }

    public int getAvailablePorts() {
        return ports.size();
    }

    protected void checkPortHits() {
        for(Map.Entry<TuioCursor, FingerTouchObject> cursor : Controller.cursorList.entrySet()) {

            FingerTouchObject fingerTouchObject = cursor.getValue();

            for (Map.Entry<ElectronicPort,ArrayList<Module>> portMapEntry : getPorts().entrySet()) {
                ElectronicPort port = portMapEntry.getKey();
                ArrayList<Module> moduleList = portMapEntry.getValue();
                if(port.intersects(fingerTouchObject) && port.getTouchObject() == null) {
                    System.out.printf("Hit auf %s!%n",port.getPortStatus());


                        if(Controller.CURRENT_PCB_LANE == null) {
                            Controller.CURRENT_PCB_LANE = new PCBLaneModule(port);
                        } else {
                            Controller.CURRENT_PCB_LANE.setOutPort(port);
                            port.getTransition().stop();
                            port.setScaleX(1);
                            port.setScaleY(1);
                            port.setTouchObject(null);
                        }






                    port.setTouchObject(fingerTouchObject);
                    port.getTransition().playFromStart();
                }

                if(!Controller.cursorList.containsValue(port.getTouchObject())) {
                    port.getTransition().stop();
                    port.setScaleX(1);
                    port.setScaleY(1);
                    port.setTouchObject(null);
                }


            }
        }

    }

    public ImageView getIconImageView() {
        return iconImageView;
    }

    public void doAction(double animationDuration) {
        getIconImageView().setRotate(-getTangibleObject().getMarker().getAngleDegrees());
        checkPortHits();
        doCustomAction(animationDuration);
    }

    @Override
    public void onTuioObjectRemoved(TuioObject tobj) {
        for (Map.Entry<ElectronicPort, ArrayList<Module>> port : getPorts().entrySet()) {
            for(PCBLaneModule pcbLaneModule : port.getKey().getPcbLaneModules()) {
                Controller.PCB_LANE_MODULES.remove(pcbLaneModule);
            }
        }
    }

    public abstract void doCustomAction(double animationDuration);

}
