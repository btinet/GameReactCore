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
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.engine.AppTimer;
import org.engine.ButtonConfig;
import org.engine.KeyPolling;
import org.engine.Transitions;
import org.gamereact.gamereactcore.CoreApplication;

import java.io.IOException;
import java.net.URL;
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
    private final Text infoText = new Text();
    private final Circle middleCircle = new Circle(20, Color.WHITE);
    private final Circle transitionCircle = new Circle(10);
    private final Circle cursor = new Circle(20,Color.WHITE);
    private final ScaleTransition st = Transitions.createScaleTransition(1000,transitionCircle,1,120);
    private final FadeTransition ft = Transitions.createFadeTransition(2000,middleCircle,0,.5);
    private final TuioClient client = new TuioClient();

    private final HashMap<TuioCursor, Circle> cursorList = new HashMap<>();

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
        this.transitionCircle.setStroke(new Color(1,1,1,.4));
        this.transitionCircle.setStrokeWidth(.1);

        this.infoText.setTranslateX(10);
        this.root.getChildren().add(this.infoText);


        this.root.getChildren().add(this.transitionCircle);
        this.root.getChildren().add(this.middleCircle);


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
        this.infoText.setText("abgelaufene Zeit: " + Math.round(animationDurationProperty().get()) + "s");

        this.infoText.setTranslateY(root.getHeight()-10);
        middleCircle.setTranslateX(root.getWidth()/2);
        middleCircle.setTranslateY(root.getHeight()/2);
        transitionCircle.setTranslateX(root.getWidth()/2);
        transitionCircle.setTranslateY(root.getHeight()/2);

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
        for ( Map.Entry<TuioCursor,Circle> cursor:
                this.cursorList.entrySet()) {
            int cx = cursor.getKey().getScreenX((int) this.root.getWidth());
            int cy = cursor.getKey().getScreenY((int) this.root.getHeight());
            if(!this.root.getChildren().contains(cursor.getValue())) this.root.getChildren().add(cursor.getValue());

            cursor.getValue().setTranslateX(cx);
            cursor.getValue().setTranslateY(cy);
        }

        this.root.getChildren().retainAll(this.cursorList.values());
        this.root.getChildren().addAll(this.transitionCircle,this.middleCircle);
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
        System.out.println("Object added!");
    }

    @Override
    public void updateTuioObject(TuioObject tobj) {

    }

    @Override
    public void removeTuioObject(TuioObject tobj) {
        System.out.println("Object removed!");
    }

    @Override
    public void addTuioCursor(TuioCursor tcur) {
        System.out.println("Cursor added!");
        Circle circle = new Circle(15,Color.YELLOW);
        ScaleTransition cst = Transitions.createScaleTransition(50,circle,.5,1);
        this.cursorList.put(tcur,circle);
        cst.play();
    }

    @Override
    public void updateTuioCursor(TuioCursor tcur) {

    }

    @Override
    public void removeTuioCursor(TuioCursor tcur) {
        System.out.println("Cursor removed!");
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