package org.gamereact.module;

import com.tuio.TuioObject;
import org.engine.TangibleObject;

public class EmptyModule extends Module {
    public EmptyModule(TangibleObject tangibleObject) {
        super(tangibleObject);
    }

    @Override
    public void doAction(double animationDuration) {

    }

    @Override
    public void onTuioObjectRemoved(TuioObject tobj) {

    }

}
