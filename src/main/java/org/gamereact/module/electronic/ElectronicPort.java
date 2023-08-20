package org.gamereact.module.electronic;

import com.tuio.TuioCursor;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.engine.FingerTouchObject;
import org.engine.Fonts;
import org.engine.Transitions;
import org.gamereact.module.ElectronicComponentModule;

import java.util.ArrayList;

public class ElectronicPort extends Circle {

    private final ElectronicPortStatus portStatus;
    private final ElectronicComponentModule module;
    private final String label;
    private final ArrayList<PCBLaneModule> pcbLaneModules = new ArrayList<>();
    private final SimpleDoubleProperty voltage = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty current = new SimpleDoubleProperty(0);
    private final ScaleTransition transition = Transitions.createScaleTransition(250,this,1,.6,-1);
    private FingerTouchObject touchObject;

    public ElectronicPort(ElectronicComponentModule module, double tx, double ty, ElectronicPortStatus portStatus, String label) {
        this.module = module;
        this.portStatus = portStatus;
        this.label = label;

        setRadius(10);
        setFill(Color.WHITE);

        setTranslateX(tx-100);
        setTranslateY(ty);
        module.getChildren().add(this);

        Text text = new Text(portStatus.toString());
        text.setFill(Color.WHITE);
        text.setFont(Fonts.BOLD_12.getFont());
        text.setTranslateX(tx-80);
        text.setTranslateY(ty+5);
        module.getChildren().add(text);
    }

    public FingerTouchObject getTouchObject() {
        return touchObject;
    }

    public ArrayList<PCBLaneModule> getPcbLaneModules() {
        return pcbLaneModules;
    }

    public void addPCBLaneModule(PCBLaneModule pcbLaneModule) {
        pcbLaneModule.voltageProperty().bind(voltageProperty());
        pcbLaneModule.currentProperty().bind(currentProperty());
        pcbLaneModules.add(pcbLaneModule);
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

    public void setTouchObject(FingerTouchObject touchObject) {
        this.touchObject = touchObject;
    }

    public ScaleTransition getTransition() {
        return transition;
    }

    public ElectronicPortStatus getPortStatus() {
        return portStatus;
    }

    public ElectronicComponentModule getModule() {
        return module;
    }

    public String getLabel() {
        return label;
    }

    public boolean intersects(Node node) {
        return localToScene(getBoundsInLocal()).intersects(node.getBoundsInParent());
    }
}
