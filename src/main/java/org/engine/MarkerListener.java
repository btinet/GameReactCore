package org.engine;

import com.tuio.*;
import javafx.animation.ScaleTransition;
import org.gamereact.module.Module;

/**
 * Listens to Tangible Markers to add, modify and remove Stage-Objects.
 */
public class MarkerListener implements TuioListener {

    /**
     *
     * @param tobj  the TuioObject reference associated to the addTuioObject event
     */
     @Override
    public void addTuioObject(TuioObject tobj) {
        Controller.objectList.put(tobj, new TangibleObject(tobj));
    }

    /**
     *
     * @param tobj  the TuioObject reference associated to the removeTuioObject event
     */
    @Override
    public void removeTuioObject(TuioObject tobj) {
        Module module = Controller.objectList.get(tobj).getModule();
        module.onTuioObjectRemoved(tobj);
        Controller.connectionLineList.remove(module.getConnectionLine());
        Controller.objectList.remove(tobj);
    }

    /**
     *
     * @param tcur  the TuioCursor reference associated to the addTuioCursor event
     */
    @Override
    public void addTuioCursor(TuioCursor tcur) {
        FingerTouchObject fingerTouchObject = new FingerTouchObject();
        ScaleTransition cst = Transitions.createScaleTransition(50, fingerTouchObject, .5, 1);
        Controller.cursorList.put(tcur, fingerTouchObject);
        cst.play();
    }

    /**
     *
     * @param tcur  the TuioCursor reference associated to the removeTuioCursor event
     */
    @Override
    public void removeTuioCursor(TuioCursor tcur) {
        Controller.cursorList.remove(tcur);
    }

    @Override
    public void updateTuioObject(TuioObject tobj) {}
    @Override
    public void updateTuioCursor(TuioCursor tcur) {}
    @Override
    public void addTuioBlob(TuioBlob tblb) {}
    @Override
    public void updateTuioBlob(TuioBlob tblb) {}
    @Override
    public void removeTuioBlob(TuioBlob tblb) {}
    @Override
    public void refresh(TuioTime ftime) {}

}