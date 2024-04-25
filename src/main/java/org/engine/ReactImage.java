package org.engine;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.gamereact.component.GenericToolBarBuilder;
import org.gamereact.component.ReactButton;
import org.gamereact.component.ReactButtonAction;

import java.io.File;

public class ReactImage extends Group {

    private final Image image;
    private final ImageView imageView = new ImageView();
    private final String name;
    private final String slash = System.getProperty("file.separator");
    private final String resources = "." + slash + "images" + slash;

    public ReactImage(String imageFile, String name) {
        File mediaFile = new File(resources + imageFile);
        image = new Image(mediaFile.toURI().toString());
        this.imageView.setImage(image);
        this.name = name;

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(500);

        setTranslateX(60);
        setTranslateY(-440);

        getChildren().add(imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getName() {
        return name;
    }

}
