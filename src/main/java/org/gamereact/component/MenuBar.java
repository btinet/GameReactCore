package org.gamereact.component;

import javafx.scene.Group;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MenuBar extends Group {

    private Rectangle background = new Rectangle(800,50, new Color(0,0,0,.6));

    private final ReactButton startButton = new ReactButton("start","jam-play-square");

    public MenuBar() {
        startButton.setBackground(new Color(0.2,0.2,0.2,.6));

        background.setArcHeight(50);
        background.setArcWidth(50);
        background.setTranslateX(-400);
        background.setTranslateY(-25);
        background.setEffect(new Bloom());
        getChildren().add(background);
        getChildren().add(startButton);
    }

}
