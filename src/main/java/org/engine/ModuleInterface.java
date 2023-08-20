package org.engine;

import com.tuio.TuioObject;

public interface ModuleInterface {

    void doAction(double animationDuration);

    void onTuioObjectRemoved(TuioObject tobj);



}
