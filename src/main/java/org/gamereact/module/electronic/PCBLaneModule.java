package org.gamereact.module.electronic;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class PCBLaneModule extends Line {

    ElectronicPort[] ports = new ElectronicPort[2];

    private final SimpleDoubleProperty voltage = new SimpleDoubleProperty(0);
    private final SimpleDoubleProperty current = new SimpleDoubleProperty(0);

    public PCBLaneModule(ElectronicPort inPort) {
        setInPort(inPort);
        inPort.addPCBLaneModule(this);
        setStroke(new Color(1,1,1,.4));
        setStrokeLineCap(StrokeLineCap.ROUND);
        setStrokeWidth(8);
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

    public void setPosition() {
        if(ports[0] != null && ports[1] != null) {
            Bounds boundsStart = ports[0].localToScene(ports[0].getBoundsInLocal());
            Bounds boundsEnd = ports[1].localToScene(ports[1].getBoundsInLocal());
            setStartX(boundsStart.getCenterX());
            setStartY(boundsStart.getCenterY());
            setEndX(boundsEnd.getCenterX());
            setEndY(boundsEnd.getCenterY());
        }
    }

    public ElectronicPort[] getPorts() {
        return ports;
    }

    public void setInPort(ElectronicPort inPort) {
        ports[0] = inPort;
    }

    public ElectronicPort getInPort() {
        return ports[0];
    }

    public void setOutPort(ElectronicPort outPort) {
        ports[1] = outPort;
        outPort.addPCBLaneModule(this);
    }

    public ElectronicPort getOutPort() {
        return ports[1];
    }

}
