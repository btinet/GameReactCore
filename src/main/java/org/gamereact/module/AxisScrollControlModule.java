package org.gamereact.module;


import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import org.engine.Module;
import org.engine.TangibleObject;
import org.gamereact.component.ReactButton;

import java.util.Map;

public class AxisScrollControlModule extends ControlModule {


    public AxisScrollControlModule(TangibleObject tangibleObject) {
        super(tangibleObject);
    }

    public void setParameter(double time, double angle) {

        //volumeIndicator.setLength(angle);

        for (Module module : this.moduleList) {
            if(module instanceof ChartModule) {
                ChartModule chartModule = (ChartModule) module;
                chartModule.moveOnXAxis(Math.sin(angle) );
                //chartModule.getMediaPlayer().setVolume(parameter);
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

        for (Map.Entry<TuioCursor, Circle> finger : getCursorList()) {

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

        for (Map.Entry<TuioObject,Group> otherModule : getObjectList()) {
            connect( ((TangibleObject) otherModule.getValue()).getModule() );
        }

        setParameter( animationDuration, getTangibleObject().getMarker().getAngle() );

    }
}
