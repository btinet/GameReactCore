package org.gamereact.controller;

import com.tuio.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Transform;
import org.engine.*;
import org.gamereact.component.MenuBar;
import org.gamereact.component.ReactButton;
import org.gamereact.gamereactcore.CoreApplication;
import org.gamereact.module.*;
import org.gamereact.module.Module;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import static org.gamereact.gamereactcore.CoreApplication.stage;

public class AppController extends Controller {

    protected final Group connectionLineGroup = new Group();
    protected final Group layoutGroup = new Group();
    protected final Group objectGroup = new Group();
    protected final Group cursorGroup = new Group();

    private final MenuBar menuBar = new MenuBar();
    @FXML
    public BorderPane root;

    /**
     * Setup des Controllers, sobald die Stage vollständig verarbeitet wurde.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client.addTuioListener(new MarkerListener());
        client.connect();

        this.root.getChildren().addAll(
                connectionLineGroup,
                layoutGroup,
                objectGroup,
                menuBar,
                cursorGroup
        );

        root.setStyle("-fx-background-color: #005EAA");
        enableDraggable(root);

        this.start();
    }

    /**
     * Hauptschleife des Timers
     */
    @Override
    public void tick(float secondsSinceLastFrame) {
        menuBar.setTranslateX(root.getWidth() / 2);
        menuBar.setTranslateY(root.getHeight() - 50);

        getKeyboardInput();
        getTangibleInput(getAnimationDuration());
    }

    private void getKeyboardInput() {
        // Periodische Tastenabfragen
        // z.B. keys.isDown(KeyCode)

        // einmalige Tastenabfragen (innerhalb Anschlagverzögerung)
        if (keys.isPressed(ButtonConfig.toggleFullscreen)) toggleFullscreen();
        if (keys.isPressed(ButtonConfig.toggleCalibrationGrid)) toggleCalibrationGrid();
        if (keys.isPressed(ButtonConfig.exit)) System.exit(0);
    }

    private void getTangibleInput(double animationDuration) {

        for (Line connectionLine : connectionLineList) {
            if(!getConnectionLineGroup().getChildren().contains(connectionLine)){
                getConnectionLineGroup().getChildren().add(connectionLine);
            }
        }
        getConnectionLineGroup().getChildren().retainAll(connectionLineList);

        for (Map.Entry<TuioObject, TangibleObject> object : objectList.entrySet()) {
            setObjectPosition(object, animationDuration);
        }
        this.objectGroup.getChildren().retainAll(objectList.values());


        for (Map.Entry<TuioCursor, FingerTouchObject> cursor : cursorList.entrySet()) {
            setCursorPosition(cursor);
            getMenuBarInput(cursor.getValue());
        }
        this.cursorGroup.getChildren().retainAll(cursorList.values());

    }

    private void getMenuBarInput(FingerTouchObject fingerTouch) {
        for (ReactButton menuBarButton :
                this.menuBar.getButtonList()) {
            if (menuBarButton.localToScene(menuBarButton.getBoundsInLocal()).intersects(fingerTouch.getBoundsInParent())) {
                switch (menuBarButton.getName()) {
                    case START:
                        break;
                    case TOGGLE_FULLSCREEN:
                        toggleFullscreen();
                        break;
                    case EXIT:
                        System.exit(0);
                        break;
                    default:
                }
            }
        }
    }

    private void setCursorPosition(Map.Entry<TuioCursor, FingerTouchObject> cursor) {
        if (!this.cursorGroup.getChildren().contains(cursor.getValue())) {
            this.cursorGroup.getChildren().add(cursor.getValue());
        }
        int cx = cursor.getKey().getScreenX((int) this.root.getWidth());
        int cy = cursor.getKey().getScreenY((int) this.root.getHeight());
        cursor.getValue().setTranslateX(cx);
        cursor.getValue().setTranslateY(cy);
    }



    private void setObjectPosition(Map.Entry<TuioObject, TangibleObject> object, double animationDuration) {

        TuioObject tuioObject = object.getKey();
        TangibleObject tangibleObject = object.getValue();

        if (!this.objectGroup.getChildren().contains(tangibleObject)) {
            this.objectGroup.getChildren().add(tangibleObject);
        }

        double ox = tuioObject.getScreenX((int) this.root.getWidth());
        double oy = tuioObject.getScreenY((int) this.root.getHeight());

        tangibleObject.setTranslateX(ox);
        tangibleObject.setTranslateY(oy);

        if(tangibleObject.getModule() instanceof ControlModule) {
            Group group = tangibleObject.getModule().getRotationGroup();
            group.getTransforms().clear();
            group.getTransforms().add(Transform.rotate(tuioObject.getAngleDegrees(), -100, 0));
            tangibleObject.getObjectPane().setRotate(tuioObject.getAngleDegrees());



            for (Module controllableModule : tangibleObject.getModule().getModuleList()) {
                TangibleObject otherTangibleObject = controllableModule.getTangibleObject();
                double tx = otherTangibleObject.getMarker().getScreenX((int) this.root.getWidth());
                double ty = otherTangibleObject.getMarker().getScreenY((int) this.root.getHeight());
                otherTangibleObject.getModule().getConnectionLine().setStartX(ox);
                otherTangibleObject.getModule().getConnectionLine().setStartY(oy);
                otherTangibleObject.getModule().getConnectionLine().setEndX(tx);
                otherTangibleObject.getModule().getConnectionLine().setEndY(ty);
            }

        } else {
            tangibleObject.getTransforms().clear();
            tangibleObject.getTransforms().add(Transform.rotate(tuioObject.getAngleDegrees(), 0, 0));
        }

        tangibleObject.getModule().doAction(animationDuration);
    }



    /*
    BEGINN: Utility-Methoden
     */

    public static void toggleFullscreen() {
        stage.setFullScreen(!stage.isFullScreen());
    }

    public static void toggleVerbose() {
        CoreApplication.verbose = !CoreApplication.verbose;
    }
    public static void toggleCalibrationGrid() {
        // TODO: CalibrationGrid implementieren
    }

    public Group getConnectionLineGroup() {
        return connectionLineGroup;
    }

    /*
    ENDE: Utility-Methoden
     */

}