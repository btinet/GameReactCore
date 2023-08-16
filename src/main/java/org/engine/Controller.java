package org.engine;

import com.tuio.TuioClient;
import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;

import static org.gamereact.gamereactcore.CoreApplication.stage;

public abstract class Controller extends AppTimer implements Initializable {

    public static final HashMap<TuioCursor, FingerTouchObject> cursorList = new HashMap<>();
    public static final HashMap<TuioObject, TangibleObject> objectList = new HashMap<>();
    public static final ArrayList<Line> connectionLineList = new ArrayList<>();
    protected final KeyPolling keys = KeyPolling.getInstance();
    protected final TuioClient client = new TuioClient();
    protected double xOffset = 0;
    protected double yOffset = 0;

    protected void enableDraggable(Node node) {
        node.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        node.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

}
