package com.mygdx.game.objects.buttons.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.scenes.GameScreen;
import com.mygdx.game.scenes.Lobby;

public class PlayButton extends Actor {
    private final TextureRegion texture = new TextureRegion(new Texture("buttons\\play.png"));

    @Override
    public void act(float delta) {
        if(Gdx.input.isTouched() && touchThis()){
            Lobby.loadingLabel.setVisible(true);
        }
    }

    public boolean touchThis(){
        float touchX = GameScreen.getMouseX(getStage());
        float touchY = GameScreen.getMouseY(getStage());

        return touchX >= getX() && touchX <= getX() + texture.getTexture().getWidth()
                && touchY >= getY() && touchY <= getY() + texture.getTexture().getHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                texture.getRegionWidth(), texture.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
