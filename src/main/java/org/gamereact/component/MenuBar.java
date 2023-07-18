package org.gamereact.component;

import javafx.scene.Group;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MenuBar extends Group {

    private final ArrayList<ReactButton> buttonList = new ArrayList<>();

    public MenuBar() {
        ReactButton startButton = new ReactButton("start", "jam-play-square");
        startButton.setBackground(new Color(0.2,0.2,0.2,.2));

        ReactButton toggleFullscreenButton = new ReactButton("fullscreen", "jam-arrows-fullscreen");
        toggleFullscreenButton.setBackground(new Color(0.2,0.2,0.2,.2));

        ReactButton exitApplication = new ReactButton("exit", "jam-log-out");
        exitApplication.setBackground(new Color(0.2,0.2,0.2,.2));

        buttonList.add(startButton);
        buttonList.add(toggleFullscreenButton);
        buttonList.add(exitApplication);

        Group buttonGroup = new Group();
        buttonGroup.setTranslateX(-360);
        buttonGroup.getChildren().addAll(buttonList);

        startButton.setTranslateX(0);
        toggleFullscreenButton.setTranslateX(60);
        exitApplication.setTranslateX(120);

        Rectangle background = new Rectangle(800, 50, new Color(0, 0, 0, .6));
        background.setArcHeight(50);
        background.setArcWidth(50);
        background.setTranslateX(-400);
        background.setTranslateY(-25);
        background.setEffect(new Bloom());

        getChildren().add(background);
        getChildren().add(buttonGroup);
    }

    public ArrayList<ReactButton> getButtonList() {
        return buttonList;
    }

}
