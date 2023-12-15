package com.mygdx.game.objects.buttons.gameButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.objects.buttons.Button;
import com.mygdx.game.scenes.GameScreen;

public class HandButton extends Button {
    private final TextureRegion texture = new TextureRegion(new Texture("buttons\\hand.png"));
    public HandButton(){
        super(new TextureRegion(new Texture("buttons\\hand.png")), 0.7f, 6);
        setxDelta(480);
        setyDelta(-160);
    }
}
