package org.gamereact.module;


import org.engine.Module;
import org.engine.TangibleObject;

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

}
