package org.gamereact.controller;

import com.tuio.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import org.engine.*;
import org.gamereact.component.MenuBar;
import org.gamereact.component.ReactButton;
import org.gamereact.gamereactcore.CoreApplication;
import org.gamereact.module.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static org.gamereact.gamereactcore.CoreApplication.stage;

public class AppController extends AppTimer implements Initializable {

    private final KeyPolling keys = KeyPolling.getInstance();
    private final TuioClient client = new TuioClient();
    private final Group layoutGroup = new Group();
    private final Group objectGroup = new Group();
    private final Group cursorGroup = new Group();
    private final MenuBar menuBar = new MenuBar();
    public final HashMap<TuioCursor, FingerTouchObject> cursorList = new HashMap<>();
    public final HashMap<TuioObject, TangibleObject> objectList = new HashMap<>();
    @FXML
    public BorderPane root;
    private double xOffset = 0;
    private double yOffset = 0;

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

        client.addTuioListener(new MarkerListener(this));
        client.connect();

        this.root.getChildren().add(layoutGroup);
        this.root.getChildren().add(objectGroup);
        this.root.getChildren().add(menuBar);
        this.root.getChildren().add(cursorGroup);

        root.setStyle("-fx-background-color: #005EAA");

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

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

    public void getKeyboardInput() {
        // Periodische Tastenabfragen
        // z.B. keys.isDown(KeyCode)

        // einmalige Tastenabfragen (innerhalb Anschlagverzögerung)
        if (keys.isPressed(ButtonConfig.toggleFullscreen)) toggleFullscreen();
        if (keys.isPressed(ButtonConfig.toggleCalibrationGrid)) toggleVerbose();
        if (keys.isPressed(ButtonConfig.exit)) System.exit(0);
    }

    private void getMenuBarInput(Circle fingerTouch) {
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

    private void getTangibleInput(double animationDuration) {

        for (Map.Entry<TuioObject, TangibleObject> object : this.objectList.entrySet()) {
            setObjectPosition(object, animationDuration);
        }
        this.objectGroup.getChildren().retainAll(this.objectList.values());


        for (Map.Entry<TuioCursor, FingerTouchObject> cursor : this.cursorList.entrySet()) {
            setCursorPosition(cursor);
            getMenuBarInput(cursor.getValue());
        }
        this.cursorGroup.getChildren().retainAll(this.cursorList.values());

    }

    public void setObjectPosition(Map.Entry<TuioObject, TangibleObject> object, double animationDuration) {

        TuioObject tuioObject = object.getKey();
        TangibleObject tangibleObject = object.getValue();

        if (!this.objectGroup.getChildren().contains(tangibleObject)) {
            this.objectGroup.getChildren().add(tangibleObject);
        }

        int ox = tuioObject.getScreenX((int) this.root.getWidth());
        int oy = tuioObject.getScreenY((int) this.root.getHeight());

        tangibleObject.setTranslateX(ox);
        tangibleObject.setTranslateY(oy);

        if(tangibleObject.getModule() instanceof ControlModule) {
            Group group = tangibleObject.getModule().getRotationGroup();
            group.getTransforms().clear();
            group.getTransforms().add(Transform.rotate(tuioObject.getAngleDegrees(), -100, 0));
            tangibleObject.getObjectPane().setRotate(tuioObject.getAngleDegrees());
        } else {
            tangibleObject.getTransforms().clear();
            tangibleObject.getTransforms().add(Transform.rotate(tuioObject.getAngleDegrees(), 0, 0));
        }

        tangibleObject.getModule().doAction(animationDuration);
    }

    public void setCursorPosition(Map.Entry<TuioCursor, FingerTouchObject> cursor) {
        if (!this.cursorGroup.getChildren().contains(cursor.getValue())) {
            this.cursorGroup.getChildren().add(cursor.getValue());
        }
        int cx = cursor.getKey().getScreenX((int) this.root.getWidth());
        int cy = cursor.getKey().getScreenY((int) this.root.getHeight());
        cursor.getValue().setTranslateX(cx);
        cursor.getValue().setTranslateY(cy);
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

    /*
    ENDE: Utility-Methoden
     */

}