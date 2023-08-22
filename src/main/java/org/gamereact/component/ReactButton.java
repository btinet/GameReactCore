package org.gamereact.component;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.engine.Transitions;
import org.kordamp.ikonli.javafx.FontIcon;

public class ReactButton extends Group {

    protected ReactButtonAction name;
    protected FontIcon icon;
    protected Rectangle background = new Rectangle(40,40,new Color(0.4,0.6,0.8,.4));
    protected ScaleTransition hitTransition = Transitions.createScaleTransition(150,background,1,.9,2);
    private Boolean enabled = true;
    private Boolean pull = true;
    public ReactButton(ReactButtonAction name, String iconCode) {
        this.name = name;
        icon = new FontIcon(iconCode);
        toggleEnabled();


        hitTransition.setOnFinished(e -> setEnabled(true));

        icon.setIconSize(30);
        icon.setTranslateX(-15);
        icon.setTranslateY(13);
        background.setTranslateX(-20);
        background.setTranslateY(-20);
        background.setArcWidth(12);
        background.setArcHeight(12);
        getChildren().addAll(background,icon);
    }

    public Boolean isPull() {
        return pull;
    }

    public void setPull(Boolean pull) {
        this.pull = pull;
    }

    public ReactButtonAction getName() {
        return name;
    }

    public EventHandler<ActionEvent> setEnabled(Boolean enabled) {
        this.enabled = enabled;
        toggleEnabled();
        return null;
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
        boolean intersects = false;
        if(localToScene(getBoundsInLocal()).intersects(node.getBoundsInParent())) {
            if(pull) {
                intersects = true;
                setEnabled(false);
                hitTransition.play();
            } else {
                return true;
            }

        }
        return intersects;
    }
}
