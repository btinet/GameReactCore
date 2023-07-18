package org.gamereact.module;

import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.engine.Module;
import org.engine.TangibleObject;
import org.engine.Transitions;

import java.util.ArrayList;

public class VolumeControlModule extends Module {

    private Boolean connected = false;
    private ArrayList<Module> moduleList = new ArrayList<>();

    public VolumeControlModule(TangibleObject tangibleObject) {
        super(tangibleObject);
    }

    public Boolean isConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public void connect(Module otherModule) {


        if(!isConnected()) {
            if(getIntersectPane().localToScene(getIntersectPane().getLayoutBounds()).intersects(otherModule.getIntersectPane().localToScene(otherModule.getLayoutBounds())) && !getIntersectPane().equals(otherModule.getIntersectPane())){
                if(otherModule.isConnectable()) {
                    System.out.println("Connectable!");

                    if(otherModule instanceof AudioPlayerModule) {
                        System.out.println("Schedule connection!");
                        getConnectIndicator().play();
                        otherModule.scheduleConnection(this);
                        this.moduleList.add(otherModule);
                    }
                }
            }
        }

    }

    public void disconnectAll() {
        disconnect();
        System.out.println(moduleList.size());
        for (Module module:
             this.moduleList) {
            module.disconnect();
        }
    }

}
