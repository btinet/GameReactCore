package org.gamereact.module.electronic;

import org.engine.TangibleObject;
import org.gamereact.module.ElectronicComponentModule;

import java.util.ArrayList;

public class CapacitorModule extends ElectronicComponentModule {

    protected ElectronicPort anodePort = new ElectronicPort(this,0,-100,ElectronicPortStatus.ANODE,"A");
    protected ElectronicPort kathodePort = new ElectronicPort(this,0,100,ElectronicPortStatus.KATHODE,"K");
    public CapacitorModule(TangibleObject tangibleObject) {
        super(tangibleObject,2,"capacitor");
        getPorts().put(anodePort,new ArrayList<>());
        getPorts().put(kathodePort,new ArrayList<>());
    }

    @Override
    public void doCustomAction(double animationDuration) {

    }

}
