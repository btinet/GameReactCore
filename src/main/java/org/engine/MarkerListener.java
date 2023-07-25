package org.engine;

import com.tuio.*;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.gamereact.controller.AppController;
import org.gamereact.gamereactcore.CoreApplication;
import org.gamereact.module.ChartModule;
import org.gamereact.module.MultimediaModule;
import org.gamereact.module.VolumeControlModule;

import java.util.HashMap;

public class MarkerListener implements TuioListener {

    private final AppController controller;
    public final HashMap<TuioCursor, Circle> cursorList;
    public final HashMap<TuioObject, Group> objectList;

    public MarkerListener(AppController controller) {
        this.controller = controller;
        this.cursorList = controller.cursorList;
        this.objectList = controller.objectList;
    }

    @Override
    public void addTuioObject(TuioObject tobj) {
        if (CoreApplication.verbose) {
            System.out.println("Object added!");
        }
        this.controller.objectList.put(tobj, new TangibleObject(tobj,this.cursorList,this.objectList));
    }

    @Override
    public void updateTuioObject(TuioObject tobj) {

    }

    @Override
    public void removeTuioObject(TuioObject tobj) {
        if (CoreApplication.verbose) System.out.println("Object removed!");
        TangibleObject disposedObject = (TangibleObject) this.controller.objectList.get(tobj);
        if (disposedObject.getModule() instanceof MultimediaModule) {
            ((MultimediaModule) disposedObject.getModule()).getMediaPlayer().dispose();
        }
        if (disposedObject.getModule() instanceof VolumeControlModule) {
            ((VolumeControlModule) disposedObject.getModule()).disconnectAll();
        }
        if(disposedObject.getModule() instanceof ChartModule) {
            ((ChartModule) disposedObject.getModule()).resetData();
        }

        this.controller.objectList.remove(tobj);
    }

    @Override
    public void addTuioCursor(TuioCursor tcur) {
        if (CoreApplication.verbose) System.out.println("Cursor added!");
        Circle circle = new Circle(15, Color.WHITE);
        ScaleTransition cst = Transitions.createScaleTransition(50, circle, .5, 1);
        this.controller.cursorList.put(tcur, circle);
        cst.play();
    }

    @Override
    public void updateTuioCursor(TuioCursor tcur) {

    }

    @Override
    public void removeTuioCursor(TuioCursor tcur) {
        if (CoreApplication.verbose) System.out.println("Cursor removed!");
        this.controller.cursorList.remove(tcur);
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
