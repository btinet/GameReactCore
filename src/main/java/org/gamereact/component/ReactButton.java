package org.gamereact.component;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;

public class ReactButton extends Group {

    protected ReactButtonAction name;
    protected FontIcon icon;
    protected Rectangle background = new Rectangle(40,40,new Color(0.4,0.6,0.8,.4));
    private Boolean enabled = true;
    public ReactButton(ReactButtonAction name, String iconCode) {
        this.name = name;
        icon = new FontIcon(iconCode);
        toggleEnabled();
        icon.setIconSize(30);
        icon.setTranslateX(-15);
        icon.setTranslateY(13);
        background.setTranslateX(-20);
        background.setTranslateY(-20);
        background.setArcWidth(12);
        background.setArcHeight(12);
        background.setEffect(new Bloom());
        getChildren().addAll(background,icon);
    }

    public ReactButtonAction getName() {
        return name;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
        toggleEnabled();
    }

    public Boolean isEnabled() {
        return enabled;
    }

    private void toggleEnabled() {
        if(this.enabled) {
            icon.setIconColor(new Color(1,1,1,.9));
        } else {
            icon.setIconColor(new Color(1,1,1,.2));
        }
    }

    public void setIcon(ReactButtonAction name, String iconLiteral) {
        this.icon.setIconLiteral(iconLiteral);
        this.name = name;
    }

    public void setBackground(Color color) {
        background.setFill(color);
    }

    public boolean intersects(Node node) {
        return localToScene(getBoundsInLocal()).intersects(node.getBoundsInParent());
    }
}
