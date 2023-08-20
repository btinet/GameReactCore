package org.gamereact.module.electronic;

import javafx.beans.property.SimpleDoubleProperty;
import org.engine.TangibleObject;
import org.gamereact.module.ElectronicComponentModule;

import java.util.ArrayList;

public class CapacitorModule extends ElectronicComponentModule {

    private final SimpleDoubleProperty capacity = new SimpleDoubleProperty(470E-03);

    protected ElectronicPort anodePort = new ElectronicPort(this,0,-100,ElectronicPortStatus.ANODE,"A");
    protected ElectronicPort kathodePort = new ElectronicPort(this,0,100,ElectronicPortStatus.KATHODE,"K");
    public CapacitorModule(TangibleObject tangibleObject) {
        super(tangibleObject,2,"capacitor");
        getPorts().put(anodePort,new ArrayList<>());
        getPorts().put(kathodePort,new ArrayList<>());
    }

    @Override
    public void doCustomAction(double animationDuration) {
        for (PCBLaneModule pcbLaneModule: anodePort.getPcbLaneModules()) {
            setVoltage(pcbLaneModule.getVoltage());
            setCurrent(pcbLaneModule.getCurrent());
            System.out.println("Spannung am Kondensator: " + getVoltage());

        }
    }

}
