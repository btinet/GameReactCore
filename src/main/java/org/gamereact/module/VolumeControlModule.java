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

public class VolumeControlModule extends ControlModule {


    public VolumeControlModule(TangibleObject tangibleObject) {
        super(tangibleObject);


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
        volumeIndicator.setStartAngle(90);
        volumeIndicator.setLength(0);
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

    }

    public void setParameter(double angle) {

        volumeIndicator.setLength(angle);

        double parameter = angle/360;

        for (Module module : this.moduleList) {
            if(module instanceof MultimediaModule) {
                MultimediaModule MultimediaModule = (MultimediaModule) module;
                MultimediaModule.getMediaPlayer().setVolume(parameter);
            }

        }
    }

}
