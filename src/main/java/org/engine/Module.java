package org.engine;

import javafx.scene.Group;
import org.gamereact.component.ReactButton;

import java.util.ArrayList;

public class Module extends Group {

    protected TangibleObject tangibleObject;
    protected ArrayList<ReactButton> buttonList = new ArrayList<>();

    public TangibleObject getTangibleObject() {
        return tangibleObject;
    }

    public ArrayList<ReactButton> getButtonList() {
        return buttonList;
    }

}
