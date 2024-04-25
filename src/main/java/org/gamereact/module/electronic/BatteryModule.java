package org.gamereact.module.electronic;

import javafx.beans.property.SimpleDoubleProperty;
import org.engine.TangibleObject;
import org.gamereact.module.ElectronicComponentModule;

import java.util.ArrayList;

public class BatteryModule extends ElectronicComponentModule {



    protected ElectronicPort anodePort = new ElectronicPort(this,0,-150,ElectronicPortStatus.DC,"A");
    protected ElectronicPort kathodePort = new ElectronicPort(this,0,150,ElectronicPortStatus.GND,"K");
    public BatteryModule(TangibleObject tangibleObject) {
        super(tangibleObject,2,"battery");
        setVoltage(9);
        setCurrent(1);
        anodePort.voltageProperty().bind(voltageProperty());
        anodePort.currentProperty().bind(currentProperty());
        kathodePort.setVoltage(0);
        kathodePort.setCurrent(0);
        getPorts().put(anodePort,new ArrayList<>());
        getPorts().put(kathodePort,new ArrayList<>());
    }

    @Override
    public void doCustomAction(double animationDuration) {

    }

}
