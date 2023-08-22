package org.gamereact.component;

import java.util.ArrayList;

public class GenericToolBarBuilder {
    private ArrayList<ReactButton> reactButtons = new ArrayList<>();

    public GenericToolBarBuilder setReactButtons(ArrayList<ReactButton> reactButtons) {
        this.reactButtons = reactButtons;
        return this;
    }

    public GenericToolBarBuilder addReactButton(ReactButton reactButton) {
        this.reactButtons.add(reactButton);
        return this;
    }

    public GenericToolBar createGenericToolBar() {
        return new GenericToolBar(reactButtons);
    }
}