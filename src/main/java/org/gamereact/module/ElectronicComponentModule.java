package org.gamereact.module;

import com.tuio.TuioCursor;
import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.engine.Controller;
import org.engine.FingerTouchObject;
import org.engine.TangibleObject;
import org.gamereact.module.electronic.ElectronicPort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ElectronicComponentModule extends Module {

    protected HashMap<ElectronicPort,ArrayList<Module>> ports;
    protected final ImageView iconImageView = new ImageView();
    public ElectronicComponentModule(TangibleObject tangibleObject, int availablePorts, String iconName) {
        super(tangibleObject);
        ports = new HashMap<>(availablePorts);
        try {
            Image iconImage = new Image(Objects.requireNonNull(this.getClass().getResource("/org/electronics/1x/" + iconName +".png")).openStream());
            iconImageView.setImage(iconImage);
            iconImageView.setPreserveRatio(true);
            iconImageView.setFitWidth(64);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        iconImageView.setTranslateY(-32);
        getChildren().add(iconImageView);
    }

    public ArrayList<Module> getModulesOnPort(ElectronicPort port) {
        return ports.get(port);
    }

    public HashMap<ElectronicPort, ArrayList<Module>> getPorts() {
        return ports;
    }

    public int getAvailablePorts() {
        return ports.size();
    }

    protected void checkPortHits() {
        for(Map.Entry<TuioCursor, FingerTouchObject> cursor : Controller.cursorList.entrySet()) {

            FingerTouchObject fingerTouchObject = cursor.getValue();

            for (Map.Entry<ElectronicPort,ArrayList<Module>> portMapEntry : getPorts().entrySet()) {
                ElectronicPort port = portMapEntry.getKey();
                ArrayList<Module> moduleList = portMapEntry.getValue();
                if(port.intersects(fingerTouchObject) && port.getTouchObject() == null) {
                    System.out.printf("Hit auf %s!%n",port.getPortStatus());
                    port.setTouchObject(fingerTouchObject);
                    //port.getTransition().setOnFinished(e -> );
                    port.getTransition().playFromStart();
                }

                if(!Controller.cursorList.containsValue(port.getTouchObject())) {
                    port.getTransition().stop();
                    port.setScaleX(1);
                    port.setScaleY(1);
                    port.setTouchObject(null);
                }


            }
        }

    }

    public ImageView getIconImageView() {
        return iconImageView;
    }

    public void doAction(double animationDuration) {
        getIconImageView().setRotate(-getTangibleObject().getMarker().getAngleDegrees());
        checkPortHits();
        doCustomAction(animationDuration);
    }

    public abstract void doCustomAction(double animationDuration);

}
