package org.gamereact.module;


import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import org.engine.Module;
import org.engine.TangibleObject;
import org.gamereact.component.ReactButton;

import java.util.ArrayList;

public abstract class ControlModule extends Module {

    private final ReactButton lockConnectionButton = new ReactButton("lock", "jam-padlock-open");
    Rectangle fillRight = new Rectangle(80, 80, new Color(0.4, 0.6, 0.8, .2));

    Arc getVolumeIndicatorBackground = new Arc();
    Arc volumeIndicator = new Arc();

    public ControlModule(TangibleObject tangibleObject) {
        super(tangibleObject);
        this.setConnectable(true);

        getVolumeIndicatorBackground.setStartAngle(0);
        getVolumeIndicatorBackground.setLength(360);
        getVolumeIndicatorBackground.setRadiusX(80);
        getVolumeIndicatorBackground.setRadiusY(80);
        getVolumeIndicatorBackground.setStroke(new Color(1,1,1,.2));
        getVolumeIndicatorBackground.setStrokeWidth(25);
        getVolumeIndicatorBackground.setFill(Color.TRANSPARENT);
        getVolumeIndicatorBackground.setTranslateX(-100);

        volumeIndicator.setType(ArcType.OPEN);
        volumeIndicator.setStrokeLineCap(StrokeLineCap.BUTT);
        volumeIndicator.setStartAngle(80);
        volumeIndicator.setLength(20);
        volumeIndicator.setRadiusX(80);
        volumeIndicator.setRadiusY(80);
        volumeIndicator.setStroke(new Color(1,1,1,.4));
        volumeIndicator.setStrokeWidth(25);
        volumeIndicator.setFill(Color.TRANSPARENT);
        volumeIndicator.setTranslateX(-100);
        moduleColor = createRandomColor();
        getIntersectPane().setFill(moduleColor);

        this.fillRight.setTranslateY(-40);
        this.fillRight.setStrokeWidth(0);
        this.fillRight.setTranslateX(-40);
        this.fillRight.setArcHeight(20);
        this.fillRight.setArcWidth(20);
        this.fillRight.setEffect(new Bloom());

        lockConnectionButton.setBackground(new Color(0.4, 0.9, 0.5, .4));
        lockConnectionButton.setTranslateX(0);
        lockConnectionButton.setEnabled(false);


        buttonList.add(lockConnectionButton);
        addCancelConnectionButton();
        getChildren().add(fillRight);
        getChildren().add(getVolumeIndicatorBackground);
        getChildren().add(volumeIndicator);
        getChildren().addAll(buttonList);
    }

    public void connect(Module otherModule) {


        if (!isConnected()) {
            if (getIntersectPane().localToScene(getIntersectPane().getLayoutBounds()).intersects(otherModule.getIntersectPane().localToScene(otherModule.getLayoutBounds())) && !getIntersectPane().equals(otherModule.getIntersectPane())) {
                if (otherModule.isConnectable()) {

                    if (otherModule instanceof ChartModule) {
                        System.out.println("Chart connection scheduled!");
                        otherModule.setModuleColor(this.moduleColor);
                        getConnectIndicator().play();
                        otherModule.scheduleConnection(this);
                        otherModule.getCancelConnectionButton().setEnabled(true);
                        if(!this.moduleList.contains(otherModule)) this.moduleList.add(otherModule);
                        lockConnectionButton.setEnabled(true);
                        cancelConnectionButton.setEnabled(true);
                    }

                    if (otherModule instanceof MultimediaModule) {
                        System.out.println("Multimedia connection scheduled!");
                        otherModule.setModuleColor(this.moduleColor);
                        getConnectIndicator().play();
                        otherModule.scheduleConnection(this);
                        otherModule.getCancelConnectionButton().setEnabled(true);
                        if(!this.moduleList.contains(otherModule)) this.moduleList.add(otherModule);
                        lockConnectionButton.setEnabled(true);
                        cancelConnectionButton.setEnabled(true);
                    }
                }
            }
        }

    }

    public void disconnectAll() {
        disconnect();
        lockConnectionButton.setEnabled(false);
        this.lockConnectionButton.setIcon("lock","jam-padlock-open");
        lockConnectionButton.setBackground(new Color(0.4, 0.9, 0.5, .4));
        for (Module module : this.moduleList) {
            module.disconnect();
        }
        this.moduleList = new ArrayList<>();
    }

    public void lockAll() {
        lockConnectionButton.setEnabled(false);
        lock();
        this.lockConnectionButton.setIcon("unlock","jam-padlock");
        this.lockConnectionButton.setBackground(new Color(0.9, 0.2, 0.5, .4));
        for (Module module : this.moduleList) {
            module.lock();

        }
    }
}
