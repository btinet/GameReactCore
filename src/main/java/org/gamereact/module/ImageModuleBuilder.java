package org.gamereact.module;

import org.engine.ReactImage;
import org.engine.TangibleObject;

import java.util.ArrayList;

public class ImageModuleBuilder {
    private TangibleObject tangibleObject;
    private ArrayList<ReactImage> images = new ArrayList<>();
    private String title;

    public ImageModuleBuilder setTangibleObject(TangibleObject tangibleObject) {
        this.tangibleObject = tangibleObject;
        return this;
    }

    public ImageModuleBuilder setImages(ArrayList<ReactImage> images) {
        this.images = images;
        return this;
    }

    public ImageModuleBuilder addImage(String imageFile, String name) {
        this.images.add(new ReactImage(imageFile, name));
        return this;
    }

    public ImageModuleBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ImageModule createImageModule() {
        return new ImageModule(tangibleObject, title, images);
    }
}