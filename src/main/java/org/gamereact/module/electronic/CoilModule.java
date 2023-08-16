package org.gamereact.module.electronic;

import org.engine.TangibleObject;
import org.gamereact.module.ElectronicComponentModule;

import java.util.ArrayList;

public class CoilModule extends ElectronicComponentModule {

    protected ElectronicPort inPort = new ElectronicPort(this,0,-100,ElectronicPortStatus.A,"I");
    protected ElectronicPort outPort = new ElectronicPort(this,0,100,ElectronicPortStatus.B,"O");
    public CoilModule(TangibleObject tangibleObject) {
        super(tangibleObject,2,"coil");
        getPorts().put(inPort,new ArrayList<>());
        getPorts().put(outPort,new ArrayList<>());
    }

    @Override
    public void doCustomAction(double animationDuration) {

    }

}
