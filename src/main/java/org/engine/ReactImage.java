package org.engine;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
        imageView.setFitWidth(800);

        setTranslateX(-140);
        setTranslateY(-590);

        getChildren().add(imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getName() {
        return name;
    }

}
