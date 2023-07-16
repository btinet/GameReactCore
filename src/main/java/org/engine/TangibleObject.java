package org.engine;

import com.tuio.TuioObject;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.gamereact.module.AudioPlayerModule;

public class TangibleObject extends Group {

    private final Rectangle objectPane = new Rectangle(80,80,new Color(0.894,0.007,0.454,1));
    private final Circle dashPane = new Circle(20,new Color(0.3,0.8,0.9,1));
    private final Text idText = new Text();
    private Module module;

    public TangibleObject(TuioObject tuioObject) {
        module = new AudioPlayerModule();
        module.setTranslateX(100);
        objectPane.setArcWidth(20);
        objectPane.setArcHeight(20);
        objectPane.setTranslateX(-40);
        objectPane.setTranslateY(-40);
        idText.setTranslateX(70);
        idText.setText(String.valueOf(tuioObject.getSymbolID()));
        getChildren().add(objectPane);
        getChildren().add(dashPane);
        getChildren().add(module);

        ScaleTransition cst = Transitions.createScaleTransition(50,this,.5,1);
        ScaleTransition cst2 = Transitions.createScaleTransition(100,dashPane,0,3);
        FadeTransition cft2 = Transitions.createFadeTransition(200,dashPane,1,0,1);

        cst.play();
        cst2.play();
        cft2.play();
        cft2.setOnFinished(e -> this.getChildren().remove(dashPane));

    }

    public Module getModule() {
        return module;
    }
}
