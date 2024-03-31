package org.gamereact.module;

import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import org.engine.Controller;
import org.engine.FingerTouchObject;
import org.engine.ReactImage;
import org.engine.TangibleObject;
import org.gamereact.component.GenericToolBar;
import org.gamereact.component.GenericToolBarBuilder;
import org.gamereact.component.ReactButton;
import org.gamereact.component.ReactButtonAction;

import java.util.ArrayList;
import java.util.Map;

public class ImageModule extends ControllableModule {

    private final String title;
    private final ArrayList<ReactImage> imageList;
    public ImageModule(TangibleObject tangibleObject, String title, ArrayList<ReactImage> images) {
        super(tangibleObject);
        this.title = title;
        this.imageList = images;

        GenericToolBarBuilder toolBarBuilder = new GenericToolBarBuilder();
        toolBarBuilder.addReactButton(new ReactButton(ReactButtonAction.PREVIOUS, "jam-set-backward-square"));
        toolBarBuilder.addReactButton(new ReactButton(ReactButtonAction.NEXT, "jam-set-forward-square"));

        GenericToolBar toolBar = toolBarBuilder.createGenericToolBar();
        buttonList.addAll(toolBar.getButtonList());

        addCancelConnectionButton();
        getRotationGroup().getChildren().add(0,imageList.get(0));
        getChildren().add(rotationGroup);
        getChildren().add(toolBar);
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
        for (Map.Entry<TuioCursor, FingerTouchObject> finger : Controller.cursorList.entrySet()) {
            for (ReactButton button : getButtonList()) {
                if (button.isEnabled() && button.intersects(finger.getValue())) {
                    ReactImage current;
                    int currentIndex;
                    switch (button.getName()) {

                        case PREVIOUS:
                            current = (ReactImage) getRotationGroup().getChildren().get(0);
                            currentIndex = imageList.lastIndexOf(current);
                            if(currentIndex > 0) {
                                int nextIndex = currentIndex - 1;
                                getRotationGroup().getChildren().set(0,imageList.get(nextIndex));
                                System.out.println("Previous");
                            }
                            break;

                        case NEXT:
                            current = (ReactImage) getRotationGroup().getChildren().get(0);
                            currentIndex = imageList.lastIndexOf(current);
                            if(currentIndex < (imageList.size()-1)) {
                                int nextIndex = currentIndex + 1;
                                getRotationGroup().getChildren().set(0,imageList.get(nextIndex));
                                System.out.println("Next");
                            }
                            break;

                        default:
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onTuioObjectRemoved(TuioObject tobj) {

    }

}
