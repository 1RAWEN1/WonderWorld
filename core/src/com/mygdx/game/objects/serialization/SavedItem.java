package com.mygdx.game.objects.serialization;

import java.io.Serializable;

public class SavedItem implements Serializable {
    private int itemIndex;
    private int endurance;

    public SavedItem(int itemIndex, int endurance) {
        this.itemIndex = itemIndex;
        this.endurance = endurance;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public int getEndurance() {
        return endurance;
    }
}
