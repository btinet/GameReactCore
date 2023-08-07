package org.gamereact.module;

import org.engine.TangibleObject;

public abstract class ControllableModule extends Module {


    public ControllableModule(TangibleObject tangibleObject) {
        super(tangibleObject);
    }

    public void disconnect() {
        getTangibleObject().getConnectionLineList().remove(getConnectionLine());
        super.disconnect();
    }

    public void setPosition(double animationDuration) {

    }

}
