package org.gamereact.controller;

import com.tuio.*;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import org.engine.*;
import org.engine.Module;
import org.gamereact.component.MenuBar;
import org.gamereact.component.ReactButton;
import org.gamereact.gamereactcore.CoreApplication;
import org.gamereact.module.*;
import org.gamereact.module.MultimediaModule;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static org.gamereact.gamereactcore.CoreApplication.stage;

public class AppController extends AppTimer implements Initializable {

    private final KeyPolling keys = KeyPolling.getInstance();
    private final Circle middleCircle = new Circle(20, Color.WHITE);
    private final Circle transitionCircle = new Circle(10);
    private final ScaleTransition st = Transitions.createScaleTransition(4000, transitionCircle, 1, 140);
    private final FadeTransition ft = Transitions.createFadeTransition(2000, middleCircle, 0, .5);
    private final TuioClient client = new TuioClient();
    private final Group layoutGroup = new Group();
    private final Group objectGroup = new Group();
    private final Group cursorGroup = new Group();
    private final MenuBar menuBar = new MenuBar();
    public final HashMap<TuioCursor, Circle> cursorList = new HashMap<>();
    public final HashMap<TuioObject, Group> objectList = new HashMap<>();
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


        this.transitionCircle.setFill(Color.TRANSPARENT);
        this.transitionCircle.setStroke(new Color(1, 1, 1, .2));
        this.transitionCircle.setStrokeWidth(.1);


        //this.layoutGroup.getChildren().add(this.transitionCircle);

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
        ft.play();

    }

    /**
     * Hauptschleife des Timers
     */
    @Override
    public void tick(float secondsSinceLastFrame) {
        middleCircle.setTranslateX(root.getWidth() / 2);
        middleCircle.setTranslateY(root.getHeight() / 2);
        transitionCircle.setTranslateX(root.getWidth() / 2);
        transitionCircle.setTranslateY(root.getHeight() / 2);
        menuBar.setTranslateX(root.getWidth() / 2);
        menuBar.setTranslateY(root.getHeight() - 50);

        if (!this.isPaused() && Math.round(this.animationDurationProperty().get()) % 4 == 0) {
            st.playFromStart();
        }


        getKeyboardInput();
        getTangibleInput(getAnimationDuration());

    }

    /*
    BEGINN: Methoden, die während des gestarteten Timers aufgerufen werden.
     */

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

    private void getTangibleInput(double AnimationDuration) {
        for (Map.Entry<TuioObject, Group> object : this.objectList.entrySet()) {

            if (!this.objectGroup.getChildren().contains(object.getValue())) {
                this.objectGroup.getChildren().add(object.getValue());
            }

            int cx = object.getKey().getScreenX((int) this.root.getWidth());
            int cy = object.getKey().getScreenY((int) this.root.getHeight());
            object.getValue().setTranslateX(cx);
            object.getValue().setTranslateY(cy);
            Group group = object.getValue();
            group.getTransforms().clear();
            group.getTransforms().add(Transform.rotate(object.getKey().getAngleDegrees(), 0, 0));

            Module module = ((TangibleObject) object.getValue()).getModule();
            module.doAction(AnimationDuration);
        }

        this.objectGroup.getChildren().retainAll(this.objectList.values());

        /*
        Für jeden Finger-Touch Aktionen ausführen
         */
        for (Map.Entry<TuioCursor, Circle> cursor :
                this.cursorList.entrySet()) {
            if (!this.cursorGroup.getChildren().contains(cursor.getValue()))
                this.cursorGroup.getChildren().add(cursor.getValue());
            int cx = cursor.getKey().getScreenX((int) this.root.getWidth());
            int cy = cursor.getKey().getScreenY((int) this.root.getHeight());
            cursor.getValue().setTranslateX(cx);
            cursor.getValue().setTranslateY(cy);

            /*
            START: Finger-Touch-Markierung zuweisen
             */
            Circle fingerTouch = cursor.getValue();
            /*
            ENDE: Finger-Touch-Markierung zuweisen
             */
            getMenuBarInput(fingerTouch);

        }
        this.cursorGroup.getChildren().retainAll(this.cursorList.values());

    }

    /*
    ENDE: Methoden, die während des gestarteten Timers aufgerufen werden.
     */


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