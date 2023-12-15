package com.mygdx.game.objects.serialization;

import java.io.Serializable;
import java.util.ArrayList;

public class SettingsSave implements Serializable {
    private float volume;
    private ArrayList<Integer> buttons;

    public SettingsSave(ArrayList<Integer> buttons, float volume) {
        this.buttons = buttons;
        this.volume = volume;
    }

    public ArrayList<Integer> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Integer> buttons) {
        this.buttons = buttons;
    }

    public float getVolume() {
        return volume;
    }
}
