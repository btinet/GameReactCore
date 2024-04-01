package org.engine;

import com.tuio.TuioClient;
import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Line;
import org.gamereact.module.electronic.PCBLaneModule;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.JOptionPane.showConfirmDialog;
import static org.gamereact.controller.AppController.calibrationGrid;
import static org.gamereact.gamereactcore.CoreApplication.stage;

public abstract class Controller extends AppTimer implements Initializable {

    public static final HashMap<TuioCursor, FingerTouchObject> cursorList = new HashMap<>();
    public static final HashMap<TuioObject, TangibleObject> objectList = new HashMap<>();
    public static final ArrayList<Line> connectionLineList = new ArrayList<>();
    public static final ArrayList<PCBLaneModule> PCB_LANE_MODULES = new ArrayList<>();
    public static  PCBLaneModule CURRENT_PCB_LANE;
    protected final KeyPolling keys = KeyPolling.getInstance();
    protected final TuioClient client = new TuioClient();
    protected double xOffset = 0;
    protected double yOffset = 0;

    protected void mouseAction(Node node) {
        node.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                stage.setFullScreen(!stage.isFullScreen());
                calibrationGrid.setPosition();
            }
            if(event.isControlDown() && event.getButton() == MouseButton.PRIMARY) {
                calibrationGrid.toggleView();
            }
            if(event.getButton() == MouseButton.SECONDARY) {
                JFrame jf=new JFrame();
                jf.setLocationRelativeTo(null);
                jf.setAlwaysOnTop(true);
                int input = showConfirmDialog(jf,"Soll GameReact Core wirklich beendet werden?","Anwendung beenden", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(input == 0) {
                    Platform.exit();
                    System.exit(0);
                }
            }
        });

        node.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown()) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        node.setOnMouseDragged(event -> {
            if(event.isPrimaryButtonDown()) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

}
