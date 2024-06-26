package org.gamereact.module;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import org.engine.ModuleInterface;
import org.engine.TangibleObject;
import org.engine.Transitions;
import org.gamereact.component.ReactButton;
import org.gamereact.component.ReactButtonAction;

import java.text.DecimalFormat;
import java.util.*;

public abstract class Module extends Group implements ModuleInterface {

    public static final String slash = System.getProperty("file.separator");
    public static final String resources = "." + slash + "music" + slash;

    protected TangibleObject tangibleObject;
    protected Group rotationGroup = new Group();
    protected ArrayList<ReactButton> buttonList = new ArrayList<>();
    private final FadeTransition connectIndicator;
    protected Color moduleColor = Color.GREEN;
    private Boolean connectable = false;
    private Boolean connectionScheduled = false;
    private Boolean connected = false;
    private Module controlModule;
    protected ArrayList<Module> moduleList = new ArrayList<>();

    private final Line connectionLine = new Line();
    protected ReactButton cancelConnectionButton = new ReactButton(ReactButtonAction.CANCEL, "ci-center-circle");
    protected DecimalFormat df;
    Rectangle fillLeft = new Rectangle(80, 80, new Color(0.4, 0.6, 0.8, .2));

    public Module(TangibleObject tangibleObject) {
        this.tangibleObject = tangibleObject;
        connectIndicator = Transitions.createFadeTransition(500, getIntersectPane(), 0, .4);

        connectionLine.setStroke(new Color(0.4, 0.6, 0.8, .2));
        connectionLine.setStrokeLineCap(StrokeLineCap.ROUND);
        connectionLine.setStrokeWidth(80);
        connectionLine.setOpacity(0);

        df = new DecimalFormat("#.##");

        this.fillLeft.setTranslateY(-40);
        this.fillLeft.setStrokeWidth(0);
        this.fillLeft.setTranslateX(-320);
        this.fillLeft.setArcHeight(20);
        this.fillLeft.setArcWidth(20);

        cancelConnectionButton.setBackground(new Color(0.9, 0.2, 0.5, .4));
        cancelConnectionButton.setTranslateX(-280);
        cancelConnectionButton.setEnabled(false);

    }

    public Line getConnectionLine() {
        return connectionLine;
    }

    public Rectangle getFillLeft() {
        return fillLeft;
    }

    public DecimalFormat getDf() {
        return df;
    }

    public Group getRotationGroup() {
        return rotationGroup;
    }

    protected Color createRandomColor() {
        Random rand = new Random(System.currentTimeMillis());

        int red = rand.nextInt(255);
        int green = 255;
        int blue = rand.nextInt(255);

        return Color.rgb(red,green,blue,.5);
    }

    public ArrayList<Module> getModuleList() {
        return moduleList;
    }

    public void setModuleColor(Color moduleColor) {
        this.moduleColor = moduleColor;
        getIntersectPane().setFill(moduleColor);
    }

    public void addCancelConnectionButton() {
        buttonList.add(cancelConnectionButton);
        getRotationGroup().getChildren().add(fillLeft);
    }

    public TangibleObject getTangibleObject() {
        return tangibleObject;
    }

    public Boolean isConnectionScheduled() {
        return connectionScheduled;
    }

    public void setConnectionScheduled(Boolean connectionScheduled) {
        this.connectionScheduled = connectionScheduled;
    }

    public void scheduleConnection(Module module) {
        setConnectionScheduled(true);
        setControlModule(module);
        getConnectIndicator().play();
    }

    public Circle getIntersectPane() {
        return this.tangibleObject.getIntersectPane();
    }

    public FadeTransition getConnectIndicator() {
        return connectIndicator;
    }

    public ArrayList<ReactButton> getButtonList() {
        return buttonList;
    }

    public void setControlModule(Module module) {
        this.controlModule = module;
    }

    public Module getControlModule() {
        return controlModule;
    }

    public void unsetControlModule() {
        this.controlModule = null;
    }

    public Boolean isConnectable() {
        return !isConnected() && connectable && !isConnectionScheduled();
    }

    public void setConnectable(Boolean connectable) {
        this.connectable = connectable;
    }

    public Boolean isConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public ReactButton getCancelConnectionButton() {
        return cancelConnectionButton;
    }

    public void disconnect() {
        cancelConnectionButton.setEnabled(false);
        unsetControlModule();
        setConnected(false);
        setConnectionScheduled(false);
        getConnectIndicator().stop();
        getIntersectPane().setOpacity(0);
        getConnectionLine().setOpacity(0);
    }

    public void lock() {
        setConnected(true);
        setConnectionScheduled(false);
        getConnectIndicator().stop();
        getIntersectPane().setOpacity(.6);
        if(this instanceof ControllableModule) {
            getConnectionLine().setOpacity(1);
        }
    }
}
