package com.mygdx.game.objects.serialization;

import java.io.Serializable;

public class SavedFallenItem implements Serializable {
    private SavedItem savedItem;
    private float x;
    private float y;
    private float z;

    public SavedFallenItem(SavedItem savedItem, float x, float y, float z) {
        this.savedItem = savedItem;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SavedItem getSavedItem() {
        return savedItem;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
