package org.gamereact.module;


import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import org.engine.Controller;
import org.engine.FingerTouchObject;
import org.engine.TangibleObject;
import org.gamereact.component.ReactButton;

import java.util.Map;

public class VolumeControlModule extends ControlModule {


    public VolumeControlModule(TangibleObject tangibleObject) {
        super(tangibleObject, "ci-headphones");


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

    }

    public void setParameter(double angle) {

        volumeIndicator.setLength(angle);

        double parameter = angle/360;

        for (Module module : this.moduleList) {
            if(module instanceof MultimediaModule) {
                MultimediaModule multimediaModule = (MultimediaModule) module;
                multimediaModule.getMediaPlayer().setVolume(parameter);
            }

        }
    }

    @Override
    public void connect(Module otherModule) {
        if (!isConnected()) {
            if (getIntersectPane().localToScene(getIntersectPane().getLayoutBounds()).intersects(otherModule.getIntersectPane().localToScene(otherModule.getLayoutBounds())) && !getIntersectPane().equals(otherModule.getIntersectPane())) {
                if (otherModule.isConnectable()) {



                    if (otherModule instanceof MultimediaModule) {
                        System.out.println("Multimedia connection scheduled!");

                        if(!Controller.connectionLineList.contains(otherModule.getConnectionLine())) {
                            Controller.connectionLineList.add(otherModule.getConnectionLine());
                        }

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

    @Override
    public void doAction(double animationDuration) {

        TuioObject tuioObject = getTangibleObject().getMarker();

        for (Map.Entry<TuioCursor, FingerTouchObject> finger : Controller.cursorList.entrySet()) {

            for(ReactButton button : getButtonList()) {
                if (button.isEnabled() && button.intersects(finger.getValue())) {
                    switch (button.getName()) {
                        case CANCEL:
                            disconnectAll();
                            break;
                        case LOCK:
                            lockAll();
                            break;
                    }
                }
            }

        }

        for (Map.Entry<TuioObject, TangibleObject> otherModule : Controller.objectList.entrySet()) {
            connect( otherModule.getValue().getModule() );
        }

        setParameter(tuioObject.getAngleDegrees());
        setValueDisplayText(Math.round(tuioObject.getAngleDegrees()/360*100));
        cancelConnectionButton.setRotate(-tuioObject.getAngleDegrees());
        lockConnectionButton.setRotate(-tuioObject.getAngleDegrees());
    }
}
