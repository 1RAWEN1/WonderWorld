package com.mygdx.game.objects.buttons.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.scenes.GameScreen;
import com.mygdx.game.scenes.Lobby;

import java.io.IOException;

public class Scroller extends Actor {
    private Pixmap pm = new Pixmap(260, 20, Pixmap.Format.RGBA4444);
    private float maxValue;

    public Scroller(float maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public void act(float delta) {
        float x = GameScreen.getMouseX(getStage());
        float y = GameScreen.getMouseY(getStage());

        if(Gdx.input.isTouched() && x > getX() && x < getX() + (pm.getWidth() - pm.getHeight()) && y >  getY() && y < getY() + pm.getHeight()){
            float valueX = (x - getX()) / (pm.getWidth() - pm.getHeight());

            updateImage(valueX);
        }
    }

    public void updateImage(float valueX){
        pm.setColor(Color.CLEAR);
        GameScreen.volume = valueX;

        try {
            GameScreen.saveButtons();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Lobby.music.setVolume(valueX);

        pm.fill();

        pm.setColor(0, 162f / 255, 232f / 255, 1);

        pm.fillRectangle(pm.getHeight() / 2, 7, pm.getWidth() - pm.getHeight(), 6);
        pm.fillCircle(pm.getHeight() / 2 + (int)(valueX * (pm.getWidth() - pm.getHeight())), pm.getHeight() / 2, pm.getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(new Texture(pm), getX() - pm.getHeight() / 2, getY());
    }
}
