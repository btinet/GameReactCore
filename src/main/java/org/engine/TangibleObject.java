package org.engine;

import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.gamereact.module.Module;

import java.util.HashMap;

public class TangibleObject extends Group {

    private final Rectangle objectPane = new Rectangle(80, 80, new Color(0.894, 0.007, 0.454, 1));
    private final Circle intersectPane = new Circle(100, new Color(0.3, 0.8, 0.9, .6));
    private final Circle dashPane = new Circle(20, new Color(0.3, 0.8, 0.9, 1));
    private final Module module;
    private final HashMap<TuioCursor, Circle> cursorList;
    protected HashMap<TuioObject, TangibleObject> objectList;
    private final TuioObject marker;

    public TangibleObject(TuioObject tuioObject, HashMap<TuioCursor, Circle> cursorList,HashMap<TuioObject, TangibleObject> objectList) {
        this.marker = tuioObject;
        this.cursorList = cursorList;
        this.objectList = objectList;
        intersectPane.setOpacity(0);
        Group group = new Group();
        group.getChildren().add(intersectPane);


        Resource resource = new Resource(this);
        module = resource.readConfig(tuioObject.getSymbolID());
        module.setTranslateX(100);
        objectPane.setArcWidth(20);
        objectPane.setArcHeight(20);
        objectPane.setTranslateX(-40);
        objectPane.setTranslateY(-40);
        Text idText = new Text();
        idText.setTranslateX(70);
        idText.setText(String.valueOf(tuioObject.getSymbolID()));
        getChildren().add(group);
        getChildren().add(objectPane);
        getChildren().add(dashPane);
        getChildren().add(module);

        ScaleTransition cst = Transitions.createScaleTransition(50, this, .5, 1);
        ScaleTransition cst2 = Transitions.createScaleTransition(100, dashPane, 0, 3);
        FadeTransition cft2 = Transitions.createFadeTransition(200, dashPane, 1, 0, 1);

        cst.play();
        cst2.play();
        cft2.play();
        cft2.setOnFinished(e -> this.getChildren().remove(dashPane));

    }

    public Circle getIntersectPane() {
        return intersectPane;
    }

    public Rectangle getObjectPane() {
        return objectPane;
    }

    public TuioObject getMarker() {
        return marker;
    }

    public Module getModule() {
        return module;
    }

    public HashMap<TuioCursor, Circle> getCursorList () {
        return this.cursorList;
    }

    public HashMap<TuioObject, TangibleObject> getObjectList() {
        return this.objectList;
    }
}
