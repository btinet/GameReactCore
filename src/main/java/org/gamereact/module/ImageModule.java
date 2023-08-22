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
        toolBarBuilder.addReactButton(new ReactButton(ReactButtonAction.AVERAGE, "ci-chart-average"));
        toolBarBuilder.addReactButton(new ReactButton(ReactButtonAction.MAXIMUM, "ci-chart-maximum"));

        GenericToolBar toolBar = toolBarBuilder.createGenericToolBar();
        buttonList.addAll(toolBar.getButtonList());

        addCancelConnectionButton();
        getRotationGroup().getChildren().add(imageList.get(0));
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
                    switch (button.getName()) {
                        case AVERAGE:
                            System.out.println("Durchschnitt");
                            break;
                        case MAXIMUM:
                            System.out.println("Maximum");
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
