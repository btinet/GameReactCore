package org.gamereact.module;


import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.scene.paint.Color;
import org.engine.ArduinoControl;
import org.engine.Controller;
import org.engine.FingerTouchObject;
import org.engine.TangibleObject;
import org.gamereact.component.ReactButton;

import java.util.ArrayList;
import java.util.Map;

public class ArduinoControlModule extends ControlModule {

    ArduinoControl arduinoControl;

    public ArduinoControlModule(TangibleObject tangibleObject) {
        super(tangibleObject, "cib-arduino");
        // Testweise:
            ArrayList<String> measurementTypes = new ArrayList<>();
            measurementTypes.add("voltage");
        // Testweise, ENDE
        arduinoControl = new ArduinoControl(measurementTypes);
        //getVolumeIndicatorBackground.setStroke(Color.TRANSPARENT);
        volumeIndicator.setStroke(Color.TRANSPARENT);
        getVolumeIndicatorBackground.setStrokeWidth(8);
        getVolumeIndicatorBackground.getStrokeDashArray().addAll(10d,10d,10d,10d);
    }

    public void setParameter(double time, double angle) {
        for (Module module : this.moduleList) {
            if(module instanceof ChartModule) {
                ChartModule chartModule = (ChartModule) module;
                chartModule.updateChart(time, arduinoControl.getData() );
            }

        }
    }

    public ArduinoControl getArduinoControl() {
        return arduinoControl;
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
        //setValueDisplayText( getDf().format(arduinoControl.getData()) );
        setValueDisplayText( getDf().format(0) );
        cancelConnectionButton.setRotate(-getTangibleObject().getMarker().getAngleDegrees());
        lockConnectionButton.setRotate(-getTangibleObject().getMarker().getAngleDegrees());

    }

    @Override
    public void onTuioObjectRemoved(TuioObject tobj) {
        disconnectAll();
        getArduinoControl().closePort();
    }
}
