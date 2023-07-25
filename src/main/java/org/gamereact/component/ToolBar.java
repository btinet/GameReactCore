package org.gamereact.component;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ToolBar extends Group {

    protected ArrayList<ReactButton> buttonList = new ArrayList<>();
    Rectangle buttonFill = new Rectangle(300, 80, new Color(0.4, 0.6, 0.8, .2));
    ReactButton arduinoInputToggleButton = new ReactButton(ReactButtonAction.ARDUINO_TOGGLE, "cib-arduino");
    ReactButton arduinoInputToggleButton2 = new ReactButton(ReactButtonAction.ARDUINO_TOGGLE, "ci-circuit-composer");
    ReactButton chartMaximum = new ReactButton(ReactButtonAction.ARDUINO_TOGGLE, "ci-chart-maximum");
    ReactButton chartMinimum = new ReactButton(ReactButtonAction.ARDUINO_TOGGLE, "ci-chart-minimum");
    ReactButton chartAverage = new ReactButton(ReactButtonAction.ARDUINO_TOGGLE, "ci-chart-average");

    public ToolBar() {

        buttonList.add(arduinoInputToggleButton);
        buttonList.add(arduinoInputToggleButton2);
        buttonList.add(chartMaximum);
        buttonList.add(chartMinimum);
        buttonList.add(chartAverage);

        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).setTranslateX(i*75);
        }

        buttonFill.setWidth(buttonList.size() * 75);

        this.buttonFill.setTranslateY(-40);
        this.buttonFill.setTranslateX(-40);
        this.buttonFill.setStrokeWidth(0);
        this.buttonFill.setArcHeight(20);
        this.buttonFill.setArcWidth(20);



        getChildren().add(buttonFill);
        getChildren().addAll(buttonList);

    }

}
