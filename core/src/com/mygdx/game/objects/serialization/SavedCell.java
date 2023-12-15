package com.mygdx.game.objects.serialization;

import java.io.Serializable;

public class SavedCell implements Serializable {
    private float x;
    private float y;
    private float biomeVal;
    private int cellType;
    private float temperature;
    private float waterLevel;

    public SavedCell(float x, float y, float biomeVal, int cellType, float temperature, float waterLevel) {
        this.x = x;
        this.y = y;
        this.biomeVal = biomeVal;
        this.cellType = cellType;
        this.temperature = temperature;
        this.waterLevel = waterLevel;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getBiomeVal() {
        return biomeVal;
    }

    public int getCellType() {
        return cellType;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getWaterLevel() {
        return waterLevel;
    }
}
