package org.engine;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.gamereact.component.ReactButton;

import java.util.ArrayList;

public class Module extends Group {

    public static final String slash = System.getProperty("file.separator");
    public static final String resources = "."+slash+"music"+slash;
    protected TangibleObject tangibleObject;
    protected ArrayList<ReactButton> buttonList = new ArrayList<>();

    private FadeTransition connectIndicator;
    private Boolean connectable = false;
    private Boolean connectionScheduled = false;
    private Boolean connected = false;
    private Module controlModule;

    public Module(TangibleObject tangibleObject) {
        this.tangibleObject = tangibleObject;
        connectIndicator = Transitions.createFadeTransition(500,getIntersectPane(),0,.4);
    }

    public TangibleObject getTangibleObject() {
        return tangibleObject;
    }

    public Boolean isConnectionScheduled() {
        return connectionScheduled;
    }

    public void setConnectionScheduled(Boolean connectionScheduled) {
        this.connectionScheduled = connectionScheduled;
    }

    public void scheduleConnection(Module module) {
        setConnectionScheduled(true);
        setControlModule(module);
        getConnectIndicator().play();
    }

    public Rectangle getObjectPane() {
        return this.tangibleObject.getObjectPane();
    }

    public Circle getIntersectPane() {
        return this.tangibleObject.getIntersectPane();
    }

    public FadeTransition getConnectIndicator() {
        return connectIndicator;
    }

    public ArrayList<ReactButton> getButtonList() {
        return buttonList;
    }

    public void setControlModule(Module module) {
        this.controlModule = module;
    }

    public void unsetControlModule() {
        this.controlModule = null;
    }

    public Boolean isConnectable() {
        return !isConnected() && connectable && !isConnectionScheduled();
    }

    public void setConnectable(Boolean connectable) {
        this.connectable = connectable;
    }

    public Boolean isConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public void disconnect() {
        unsetControlModule();
        setConnected(false);
        setConnectionScheduled(false);
        getConnectIndicator().stop();
        getIntersectPane().setOpacity(0);
    }
}
