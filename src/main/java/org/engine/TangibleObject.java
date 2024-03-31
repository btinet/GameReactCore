package org.engine;

import com.tuio.TuioObject;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.gamereact.module.Module;

public class TangibleObject extends Group implements TangibleInterface {

    /**
     * Untergrund des Markers
     */
    private final Circle objectPane = new Circle(50, new Color(0.76, 0.78, 0.19, 1));
    private final Circle intersectPane = new Circle(100, new Color(0.3, 0.8, 0.9, .6));
    private final Circle dashPane = new Circle(20, new Color(0.3, 0.8, 0.9, 1));

    /**
     * Modul, das dem TangibleObject zugeordnete ist;
     */
    private final Module module;

    /**
     * Marker, der dem TangibleObject zugeordnete ist;
     */
    private final TuioObject marker;

    public TangibleObject(TuioObject tuioObject) {
        this.marker = tuioObject;
        intersectPane.setOpacity(0);
        Group group = new Group();
        group.getChildren().add(intersectPane);

        Resource resource = new Resource(this);
        module = resource.readConfig(tuioObject.getSymbolID());
        module.setTranslateX(100);
        getChildren().add(group);
        getChildren().add(objectPane);
        getChildren().add(dashPane);
        getChildren().add(module);
        Controller.connectionLineList.add(module.getConnectionLine());

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

    public Circle getObjectPane() {
        return objectPane;
    }

    public TuioObject getMarker() {
        return marker;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public void setPosition(TuioObject tuioObject, double animationDuration) {

    }

}
