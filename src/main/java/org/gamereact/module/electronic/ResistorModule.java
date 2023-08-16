package org.gamereact.module.electronic;

import org.engine.TangibleObject;
import org.gamereact.module.ElectronicComponentModule;

import java.util.ArrayList;

public class ResistorModule extends ElectronicComponentModule {

    protected ElectronicPort inPort = new ElectronicPort(this,0,-100,ElectronicPortStatus.A,"I");
    protected ElectronicPort outPort = new ElectronicPort(this,0,100,ElectronicPortStatus.B,"O");
    public ResistorModule(TangibleObject tangibleObject) {
        super(tangibleObject,2,"resistor");
        getPorts().put(inPort,new ArrayList<>());
        getPorts().put(outPort,new ArrayList<>());
    }

    @Override
    public void doCustomAction(double animationDuration) {

    }

}
