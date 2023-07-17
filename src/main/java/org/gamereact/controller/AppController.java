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
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.engine.*;
import org.engine.Module;
import org.gamereact.component.MenuBar;
import org.gamereact.component.ReactButton;
import org.gamereact.gamereactcore.CoreApplication;
import org.gamereact.module.AudioPlayerModule;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static org.gamereact.gamereactcore.CoreApplication.stage;

public class AppController extends AppTimer implements Initializable, TuioListener {

    @FXML
    public BorderPane root;
    private final KeyPolling keys = KeyPolling.getInstance();
    private double xOffset = 0;
    private double yOffset = 0;
    private final Circle middleCircle = new Circle(20, Color.WHITE);
    private final Circle transitionCircle = new Circle(10);
    private final Circle cursor = new Circle(20,Color.WHITE);
    private final ScaleTransition st = Transitions.createScaleTransition(4000,transitionCircle,1,140);
    private final FadeTransition ft = Transitions.createFadeTransition(2000,middleCircle,0,.5);
    private final TuioClient client = new TuioClient();

    private final Group layoutGroup = new Group();
    private final Group objectGroup = new Group();
    private final Group cursorGroup = new Group();
    private final MenuBar menuBar = new MenuBar();

    private final HashMap<TuioCursor, Circle> cursorList = new HashMap<>();
    private final HashMap<TuioObject, Group> objectList = new HashMap<>();

    /**
     * Setup des Controllers, sobald die Stage vollständig verarbeitet wurde.
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        client.addTuioListener(this);
        client.connect();



        this.transitionCircle.setFill(Color.TRANSPARENT);
        this.transitionCircle.setStroke(new Color(1,1,1,.2));
        this.transitionCircle.setStrokeWidth(.1);


        this.layoutGroup.getChildren().add(this.transitionCircle);
        this.layoutGroup.getChildren().add(this.middleCircle);
        this.layoutGroup.getChildren().add(this.menuBar);

        this.root.getChildren().add(layoutGroup);
        this.root.getChildren().add(objectGroup);
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
        middleCircle.setTranslateX(root.getWidth()/2);
        middleCircle.setTranslateY(root.getHeight()/2);
        transitionCircle.setTranslateX(root.getWidth()/2);
        transitionCircle.setTranslateY(root.getHeight()/2);
        menuBar.setTranslateX(root.getWidth()/2);
        menuBar.setTranslateY(root.getHeight()-50);

        if(!this.isPaused() && Math.round(this.animationDurationProperty().get()) % 4 == 0) {
            st.playFromStart();
        }


        getKeyboardInput();
        getTangibleInput();

    }

    /*
    BEGINN: Methoden, die während des gestarteten Timers aufgerufen werden.
     */

    public void getKeyboardInput () {
        // Periodische Tastenabfragen
        // z.B. keys.isDown(KeyCode)

        // einmalige Tastenabfragen (innerhalb Anschlagverzögerung)
        if (keys.isPressed(ButtonConfig.toggleFullscreen)) toggleFullscreen();
        if (keys.isPressed(ButtonConfig.toggleCalibrationGrid)) toggleVerbose();
        if (keys.isPressed(ButtonConfig.exit)) System.exit(0);
    }

    private void getTangibleInput() {

        for ( Map.Entry<TuioObject,Group> object:
                this.objectList.entrySet()) {
            if(!this.objectGroup.getChildren().contains(object.getValue())) this.objectGroup.getChildren().add(object.getValue());
            int cx = object.getKey().getScreenX((int) this.root.getWidth());
            int cy = object.getKey().getScreenY((int) this.root.getHeight());
            object.getValue().setTranslateX(cx);
            object.getValue().setTranslateY(cy);

            Group group = object.getValue();

            group.getTransforms().clear();
            group.getTransforms().add(Transform.rotate(object.getKey().getAngleDegrees(), 0,0));

            //group.getChildren().get(0).setRotate(object.getKey().getAngleDegrees());
        }
        this.objectGroup.getChildren().retainAll(this.objectList.values());

        for ( Map.Entry<TuioCursor,Circle> cursor:
                this.cursorList.entrySet()) {
            if(!this.cursorGroup.getChildren().contains(cursor.getValue())) this.cursorGroup.getChildren().add(cursor.getValue());
            int cx = cursor.getKey().getScreenX((int) this.root.getWidth());
            int cy = cursor.getKey().getScreenY((int) this.root.getHeight());
            cursor.getValue().setTranslateX(cx);
            cursor.getValue().setTranslateY(cy);
            Circle fingerTouch = cursor.getValue();

            for ( Map.Entry<TuioObject,Group> object:
                    this.objectList.entrySet()) {
                ArrayList<ReactButton> buttons = ((TangibleObject) object.getValue()).getModule().getButtonList();
                Module module = ((TangibleObject) object.getValue()).getModule();

                if(module instanceof AudioPlayerModule) {
                    for (Track track:
                         ((AudioPlayerModule) module).getTracks()) {
                        if(track.getPlayButton().isEnabled()) {
                            if(track.getPlayButton().localToScene(track.getPlayButton().getBoundsInLocal()).intersects(fingerTouch.getBoundsInParent())) {
                                ((AudioPlayerModule) module).gotoAndPlay(track.getStartDuration());
                            }
                        }
                    }
                }

                for (ReactButton button:
                     buttons) {
                    if (button.localToScene(button.getBoundsInLocal()).intersects(fingerTouch.getBoundsInParent())) {
                        if(CoreApplication.verbose) {
                            System.out.printf("Hit auf %s%n",button.getName());
                        }


                        if(button.isEnabled()) {
                            ((FontIcon)button.getChildren().get(1)).setFill(new Color(0.4,0.6,0.8,.6));

                            if(module instanceof AudioPlayerModule) {
                                switch (button.getName()) {
                                    case "play":
                                        ((AudioPlayerModule) module).play();
                                        break;
                                    case "pause":
                                        ((AudioPlayerModule) module).pause();
                                        break;
                                    case "back":
                                        ((AudioPlayerModule) module).rewind();
                                        break;
                                    case "next":
                                        ((AudioPlayerModule) module).forward();
                                        break;
                                    case "toggleTrackView":
                                        ((AudioPlayerModule) module).toggleTrackView();
                                        break;
                                }
                            }

                        }
                    } else {
                        if(button.isEnabled()) {
                            ((FontIcon) button.getChildren().get(1)).setFill(new Color(1, 1, 1, .9));
                        }
                    }
                }
            }

        }
        this.cursorGroup.getChildren().retainAll(this.cursorList.values());

    }

    /*
    ENDE: Methoden, die während des gestarteten Timers aufgerufen werden.
     */


    /*
    BEGINN: Utility-Methoden
     */

    protected void toggleFullscreen() {
        stage.setFullScreen(!stage.isFullScreen());
    }

    protected void toggleVerbose() {
        CoreApplication.verbose = !CoreApplication.verbose;
    }

    /*
    ENDE: Utility-Methoden
     */

    /*
    START: TuioObject Listener für Objekte und Fingercursor
     */

    @Override
    public void addTuioObject(TuioObject tobj) {
        if(CoreApplication.verbose) {
            System.out.println("Object added!");
        }

        this.objectList.put(tobj,new TangibleObject(tobj));
    }

    @Override
    public void updateTuioObject(TuioObject tobj) {

    }

    @Override
    public void removeTuioObject(TuioObject tobj) {
        if(CoreApplication.verbose) System.out.println("Object removed!");
        this.objectList.remove(tobj);
    }

    @Override
    public void addTuioCursor(TuioCursor tcur) {
        if(CoreApplication.verbose) System.out.println("Cursor added!");
        Circle circle = new Circle(15,Color.WHITE);
        ScaleTransition cst = Transitions.createScaleTransition(50,circle,.5,1);
        this.cursorList.put(tcur,circle);
        cst.play();
    }

    @Override
    public void updateTuioCursor(TuioCursor tcur) {

    }

    @Override
    public void removeTuioCursor(TuioCursor tcur) {
        if(CoreApplication.verbose) System.out.println("Cursor removed!");
        this.cursorList.remove(tcur);
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

    /*
    ENDE: TuioObject Listener für Objekte und Fingercursor
     */

}