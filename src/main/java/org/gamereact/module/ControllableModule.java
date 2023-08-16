package org.gamereact.module;

import org.engine.Controller;
import org.engine.TangibleObject;

public abstract class ControllableModule extends Module {


    public ControllableModule(TangibleObject tangibleObject) {
        super(tangibleObject);
    }

    public void disconnect() {
        Controller.connectionLineList.remove(getConnectionLine());
        super.disconnect();
    }

    public void setPosition(double animationDuration) {

    }

}
