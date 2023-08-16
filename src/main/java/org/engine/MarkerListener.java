package org.engine;

import com.tuio.*;
import javafx.animation.ScaleTransition;
import org.gamereact.gamereactcore.CoreApplication;
import org.gamereact.module.*;

public class MarkerListener implements TuioListener {

    public MarkerListener() {
    }

    @Override
    public void addTuioObject(TuioObject tobj) {
        if (CoreApplication.verbose) {
            System.out.println("Object added!");
        }
        Controller.objectList.put(tobj, new TangibleObject(tobj));
    }

    @Override
    public void updateTuioObject(TuioObject tobj) {

    }

    @Override
    public void removeTuioObject(TuioObject tobj) {
        if (CoreApplication.verbose) System.out.println("Object removed!");
        TangibleObject disposedObject = Controller.objectList.get(tobj);
        Controller.connectionLineList.remove(disposedObject.getModule().getConnectionLine());
        if (disposedObject.getModule() instanceof MultimediaModule) {
            ((MultimediaModule) disposedObject.getModule()).getMediaPlayer().dispose();
        }
        if (disposedObject.getModule() instanceof ControlModule) {
            ((ControlModule) disposedObject.getModule()).disconnectAll();
            if (disposedObject.getModule() instanceof RotationSignalOutputModule) {
                ((RotationSignalOutputModule) disposedObject.getModule()).getArduinoControl().closePort();
            }

        }
        if(disposedObject.getModule() instanceof ChartModule) {
            ((ChartModule) disposedObject.getModule()).resetData();
        }

        Controller.objectList.remove(tobj);
    }

    @Override
    public void addTuioCursor(TuioCursor tcur) {
        if (CoreApplication.verbose) System.out.println("Cursor added!");
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
        if (CoreApplication.verbose) System.out.println("Cursor removed!");
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
