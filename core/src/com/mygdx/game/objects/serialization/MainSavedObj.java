package com.mygdx.game.objects.serialization;

import java.io.Serializable;
import java.util.ArrayList;

public class MainSavedObj implements Serializable {
    private int[] materials;
    private int buildingType;
    private float magDamage;
    private int magicType;
    private int playerSatiety;
    private int playerWater;
    private float plMagicPower;
    private float plMaxMagicPower;
    private int money;
    private float darkCof;
    private int colonyThinking;
    private float[] magicPar;
    private ArrayList<Integer> effects;
    private boolean freeze;
    private boolean autoRot;

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public boolean isAutoRot() {
        return autoRot;
    }

    public void setAutoRot(boolean autoRot) {
        this.autoRot = autoRot;
    }

    public float[] getMagicPar() {
        return magicPar;
    }

    public void setMagicPar(float[] magicPar) {
        this.magicPar = magicPar;
    }

    public ArrayList<Integer> getEffects() {
        return effects;
    }

    public void setEffects(ArrayList<Integer> effects) {
        this.effects = effects;
    }

    public float getPlMagicPower() {
        return plMagicPower;
    }

    public void setPlMagicPower(float plMagicPower) {
        this.plMagicPower = plMagicPower;
    }

    public float getPlMaxMagicPower() {
        return plMaxMagicPower;
    }

    public void setPlMaxMagicPower(float plMaxMagicPower) {
        this.plMaxMagicPower = plMaxMagicPower;
    }

    public int[] getMaterials() {
        return materials;
    }

    public void setMaterials(int[] materials) {
        this.materials = materials;
    }

    public int getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(int buildingType) {
        this.buildingType = buildingType;
    }

    private ArrayList<SavedCell> cells;
    private ArrayList<SavedCreature> creatures;
    private ArrayList<SavedObs> obstacles;
    private ArrayList<SavedFallenItem> fallenItems;

    public ArrayList<SavedCell> getCells() {
        return cells;
    }

    public void setCells(ArrayList<SavedCell> cells) {
        this.cells = cells;
    }

    public ArrayList<SavedCreature> getCreatures() {
        return creatures;
    }

    public void setCreatures(ArrayList<SavedCreature> creatures) {
        this.creatures = creatures;
    }

    public ArrayList<SavedObs> getObstacles() {
        return obstacles;
    }

    public void setObstacles(ArrayList<SavedObs> obstacles) {
        this.obstacles = obstacles;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public float getDarkCof() {
        return darkCof;
    }

    public void setDarkCof(float darkCof) {
        this.darkCof = darkCof;
    }

    public int getColonyThinking() {
        return colonyThinking;
    }

    public void setColonyThinking(int colonyThinking) {
        this.colonyThinking = colonyThinking;
    }

    public ArrayList<SavedFallenItem> getFallenItems() {
        return fallenItems;
    }

    public void setFallenItems(ArrayList<SavedFallenItem> fallenItems) {
        this.fallenItems = fallenItems;
    }

    public int getPlayerSatiety() {
        return playerSatiety;
    }

    public void setPlayerSatiety(int playerSatiety) {
        this.playerSatiety = playerSatiety;
    }

    public int getPlayerWater() {
        return playerWater;
    }

    public void setPlayerWater(int playerWater) {
        this.playerWater = playerWater;
    }

    public float getMagDamage() {
        return magDamage;
    }

    public void setMagDamage(float magDamage) {
        this.magDamage = magDamage;
    }

    public int getMagicType() {
        return magicType;
    }

    public void setMagicType(int magicType) {
        this.magicType = magicType;
    }
}
