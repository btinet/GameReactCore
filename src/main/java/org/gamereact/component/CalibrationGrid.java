package org.gamereact.component;

import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.gamereact.controller.AppController;

public class CalibrationGrid extends Group {

    protected final AppController controller;
    private final Color strokeColor = Color.WHITE;
    private final int strokeWidth = 4;
    private final Line horizontalLine = new Line();
    private final Rectangle r1 = new Rectangle(100,100);
    private final Rectangle r2 = new Rectangle(100,100);
    private final Rectangle r3 = new Rectangle(100,100);
    private final Rectangle r4 = new Rectangle(100,100);
    private final Line verticalLine = new Line();

    public CalibrationGrid(AppController controller) {
        this.controller = controller;
        r1.setStrokeWidth(strokeWidth);
        r1.setStroke(strokeColor);
        r1.setFill(Color.TRANSPARENT);
        r2.setStrokeWidth(strokeWidth);
        r2.setStroke(strokeColor);
        r2.setFill(Color.TRANSPARENT);
        r3.setStrokeWidth(strokeWidth);
        r3.setStroke(strokeColor);
        r3.setFill(Color.TRANSPARENT);
        r4.setStrokeWidth(strokeWidth);
        r4.setStroke(strokeColor);
        r4.setFill(Color.TRANSPARENT);

        setOpacity(0);

        horizontalLine.setStroke(strokeColor);
        horizontalLine.setStrokeWidth(strokeWidth);
        getChildren().add(horizontalLine);

        verticalLine.setStroke(strokeColor);
        verticalLine.setStrokeWidth(strokeWidth);
        getChildren().add(verticalLine);
        getChildren().addAll(r1,r2,r3,r4);
    }


    public void toggleView() {
        BorderPane borderPane = controller.root;
        if(getOpacity() == 0) {
            setOpacity(1);
        } else {
            setOpacity(0);
        }
        setPosition();
    }

    public void setPosition() {
        BorderPane borderPane = controller.root;
        horizontalLine.setStartX(0);
        horizontalLine.setStartY(borderPane.getHeight()/2);
        horizontalLine.setEndX(borderPane.getWidth());
        horizontalLine.setEndY(borderPane.getHeight()/2);
        verticalLine.setStartX(borderPane.getWidth()/2);
        verticalLine.setStartY(0);
        verticalLine.setEndX(borderPane.getWidth()/2);
        verticalLine.setEndY(borderPane.getHeight());

        r1.setTranslateX(50);
        r1.setTranslateY(50);
        r2.setTranslateX(borderPane.getWidth()-150);
        r2.setTranslateY(50);
        r3.setTranslateX(50);
        r3.setTranslateY(borderPane.getHeight()-150);
        r4.setTranslateX(borderPane.getWidth()-150);
        r4.setTranslateY(borderPane.getHeight()-150);
    }
}
