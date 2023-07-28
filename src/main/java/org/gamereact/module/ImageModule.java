package org.gamereact.module;

import org.engine.ReactImage;
import org.engine.TangibleObject;
import java.util.ArrayList;

public class ImageModule extends ControllableModule {

    private final String title;
    private final ArrayList<ReactImage> imageList;
    public ImageModule(TangibleObject tangibleObject, String title, ArrayList<ReactImage> images) {
        super(tangibleObject);
        this.title = title;
        this.imageList = images;

        addCancelConnectionButton();
        getRotationGroup().getChildren().add(imageList.get(0));
        getChildren().add(rotationGroup);
        getChildren().addAll(buttonList);

    }

    public String getTitle() {
        return title;
    }

    public ArrayList<ReactImage> getImageList() {
        return imageList;
    }

    @Override
    public void doAction(double animationDuration) {

    }

}
