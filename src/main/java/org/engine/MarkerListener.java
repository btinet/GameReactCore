package org.engine;

import com.tuio.*;
import javafx.animation.ScaleTransition;

public class MarkerListener implements TuioListener {

    public MarkerListener() {
    }

    @Override
    public void addTuioObject(TuioObject tobj) {
        Controller.objectList.put(tobj, new TangibleObject(tobj));
    }

    @Override
    public void updateTuioObject(TuioObject tobj) {

    }

    @Override
    public void removeTuioObject(TuioObject tobj) {
        Controller.objectList.get(tobj).getModule().onTuioObjectRemoved(tobj);
        Controller.connectionLineList.remove(Controller.objectList.get(tobj).getModule().getConnectionLine());
        Controller.objectList.remove(tobj);
    }

    @Override
    public void addTuioCursor(TuioCursor tcur) {
        FingerTouchObject fingerTouchObject = new FingerTouchObject();
        ScaleTransition cst = Transitions.createScaleTransition(50, fingerTouchObject, .5, 1);
        Controller.cursorList.put(tcur, fingerTouchObject);
        cst.play();
    }

    @Override
    public void updateTuioCursor(TuioCursor tcur) {

    }

    @Override
    public void removeTuioCursor(TuioCursor tcur) {
        Controller.cursorList.remove(tcur);
    }

    @Override
    public void addTuioBlob(TuioBlob tblb) {

    }

    @Override
    public void updateTuioBlob(TuioBlob tblb) {

    }

    @Override
    public void removeTuioBlob(TuioBlob tblb) {

    }

    @Override
    public void refresh(TuioTime ftime) {
    }

}
