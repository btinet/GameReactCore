package org.gamereact.component;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class ReactIcon extends Group {
    protected FontIcon icon;
    public ReactIcon(String iconCode) {
        icon = new FontIcon(iconCode);
        icon.setIconColor(Color.WHITE);
        icon.setIconSize(30);
        icon.setTranslateX(-15);
        icon.setTranslateY(13);
        getChildren().add(icon);
    }


    public void setIcon(String iconLiteral) {
        this.icon.setIconLiteral(iconLiteral);
    }

}
