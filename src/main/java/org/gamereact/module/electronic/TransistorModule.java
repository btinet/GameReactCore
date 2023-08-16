package org.gamereact.module.electronic;

import com.tuio.TuioCursor;
import org.engine.Controller;
import org.engine.FingerTouchObject;
import org.engine.TangibleObject;
import org.gamereact.module.ElectronicComponentModule;
import org.gamereact.module.Module;

import java.util.ArrayList;
import java.util.Map;

public class TransistorModule extends ElectronicComponentModule {

    protected ElectronicPort collectorPort = new ElectronicPort(this,0,-100,ElectronicPortStatus.COLLECTOR,"C");
    protected ElectronicPort basePort = new ElectronicPort(this,-100,0,ElectronicPortStatus.BASE,"B");
    protected ElectronicPort emitterPort = new ElectronicPort(this,0,100,ElectronicPortStatus.EMITTER,"E");
    public TransistorModule(TangibleObject tangibleObject) {
        super(tangibleObject,3,"transistor-x");
        getPorts().put(collectorPort,new ArrayList<>());
        getPorts().put(basePort,new ArrayList<>());
        getPorts().put(emitterPort,new ArrayList<>());
    }

    @Override
    public void doCustomAction(double animationDuration) {

    }

}
