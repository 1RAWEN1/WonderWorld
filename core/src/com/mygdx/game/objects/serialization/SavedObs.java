package com.mygdx.game.objects.serialization;

import java.io.Serializable;

public class SavedObs implements Serializable {
    private String picName;
    private final int[] materials;
    private final int obsType;
    private final float x;
    private final float y;
    private final float z;
    private final int width;
    private final int height;
    private final int zSize;
    private int type;
    private int size;

    private int rectDelta;
    private boolean isBuilding = false;

    public SavedObs(int[] materials, int obsType, float x, float y, float z, int width, int height, int zSize, boolean isBuilding) {
        this.materials = materials;
        this.obsType = obsType;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.zSize = zSize;
        this.isBuilding = isBuilding;
    }

    public boolean isBuilding() {
        return isBuilding;
    }

    public int getRectDelta() {
        return rectDelta;
    }

    public void setRectDelta(int rectDelta) {
        this.rectDelta = rectDelta;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getzSize() {
        return zSize;
    }

    public int[] getMaterials() {
        return materials;
    }

    public int getObsType() {
        return obsType;
    }
}
