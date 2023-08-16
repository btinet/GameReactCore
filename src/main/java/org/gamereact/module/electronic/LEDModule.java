package org.gamereact.module.electronic;

import org.engine.TangibleObject;
import org.gamereact.module.ElectronicComponentModule;

import java.util.ArrayList;

public class LEDModule extends ElectronicComponentModule {

    protected ElectronicPort inPort = new ElectronicPort(this,0,-100,ElectronicPortStatus.ANODE,"A");
    protected ElectronicPort outPort = new ElectronicPort(this,0,100,ElectronicPortStatus.KATHODE,"K");
    public LEDModule(TangibleObject tangibleObject) {
        super(tangibleObject,2,"led");
        getPorts().put(inPort,new ArrayList<>());
        getPorts().put(outPort,new ArrayList<>());
    }


    @Override
    public void doCustomAction(double animationDuration) {

    }
}
