package org.gamereact.module;


import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import org.engine.Controller;
import org.engine.FingerTouchObject;
import org.engine.TangibleObject;
import org.gamereact.component.ReactButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RotationSignalOutputModule extends ControlModule {

       public RotationSignalOutputModule(TangibleObject tangibleObject) {
        super(tangibleObject, "ci-chart-line");
        getVolumeIndicatorBackground.setStrokeWidth(8);
        getVolumeIndicatorBackground.getStrokeDashArray().addAll(10d,10d,10d,10d);
    }

    public void setParameter(double time, double angle) {
        for (Module module : this.moduleList) {
            if(module instanceof ChartModule) {
                ChartModule chartModule = (ChartModule) module;
                HashMap<String, Double> set = new HashMap<>();
                set.put("Winkel",Math.cos( (angle)));
                chartModule.updateChart(time, set);
            }

        }
    }

    @Override
    public void connect(Module otherModule) {
        if (!isConnected()) {
            if (getIntersectPane().localToScene(getIntersectPane().getLayoutBounds()).intersects(otherModule.getIntersectPane().localToScene(otherModule.getLayoutBounds())) && !getIntersectPane().equals(otherModule.getIntersectPane())) {
                if (otherModule.isConnectable()) {


                    if (otherModule instanceof ChartModule) {
                        System.out.println("Chart connection scheduled!");

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

        for (Map.Entry<TuioCursor, FingerTouchObject> finger : Controller.cursorList.entrySet()) {

            for(ReactButton button : getButtonList()) {
                if (button.isEnabled() && button.intersects(finger.getValue())) {
                    switch (button.getName()) {
                        case CANCEL:
                            disconnectAll();
                            System.out.println("Disconnect!");
                            break;
                        case LOCK:
                            lockAll();
                            System.out.println("Lock!");
                            break;
                    }
                }
            }

        }

        for (Map.Entry<TuioObject, TangibleObject> otherModule : Controller.objectList.entrySet()) {
            connect( otherModule.getValue().getModule() );
        }

        setParameter( animationDuration, getTangibleObject().getMarker().getAngle() );
        getVolumeIndicatorBackground.setRotate(-getTangibleObject().getMarker().getAngleDegrees());
        setValueDisplayText( getDf().format(Math.cos( (getTangibleObject().getMarker().getAngle()) )) );
        cancelConnectionButton.setRotate(-getTangibleObject().getMarker().getAngleDegrees());
        lockConnectionButton.setRotate(-getTangibleObject().getMarker().getAngleDegrees());

    }

    @Override
    public void onTuioObjectRemoved(TuioObject tobj) {
        disconnectAll();
    }
}
