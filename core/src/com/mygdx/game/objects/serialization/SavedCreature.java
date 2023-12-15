package com.mygdx.game.objects.serialization;

import java.io.Serializable;
import java.util.ArrayList;

public class SavedCreature implements Serializable {
    private String picName;
    private int objectType;
    private float hp;
    private SavedItem[] inv;
    private ArrayList<SavedItem> mainItems;
    private float x;
    private float y;
    private float z;
    private boolean isBuilding;

    public SavedCreature(int objectType, float hp, SavedItem[] inv, ArrayList<SavedItem> mainItems, float x, float y, float z, boolean isBuilding) {
        this.objectType = objectType;
        this.hp = hp;
        this.inv = inv;
        this.mainItems = mainItems;
        this.x = x;
        this.y = y;
        this.z = z;

        this.isBuilding = isBuilding;
    }

    public boolean isBuilding() {
        return isBuilding;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public int getObjectType() {
        return objectType;
    }

    public float getHp() {
        return hp;
    }

    public SavedItem[] getInv() {
        return inv;
    }

    public ArrayList<SavedItem> getMainItems() {
        return mainItems;
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
