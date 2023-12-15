package com.mygdx.game.objects.buttons.gameButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.objects.buttons.Button;
import com.mygdx.game.scenes.GameScreen;

public class JumpButton extends Button {
    public JumpButton(){
        super(new TextureRegion(new Texture("buttons\\jump.png")), 0.7f, 4);
        setxDelta(300);
        setyDelta(-100);
    }
}
