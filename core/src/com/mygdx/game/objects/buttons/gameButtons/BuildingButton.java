package com.mygdx.game.objects.buttons.gameButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.objects.buttons.ButtonInterface;
import com.mygdx.game.scenes.GameScreen;

public class BuildingButton extends Actor implements ButtonInterface {
    private final TextureRegion texture = new TextureRegion(new Texture("buttons\\build.png"));

    private boolean tabPush = false;
    @Override
    public void act(float delta) {
        if(Gdx.input.isTouched(Joystick.index) && !tabPush){
            if(touchThis())
                GameScreen.player.setSeeBuilding(!GameScreen.player.isSeeBuilding());
            tabPush = true;
        }
        else if(!Gdx.input.isTouched() && tabPush){
            tabPush = false;
        }

        setVisible(GameScreen.dialogReader.getTalker() == null);

        setPosition(GameScreen.player.getX() + 300, GameScreen.player.getY() + GameScreen.player.getZ() - 290);
    }

    public boolean touchThis(){
        float touchX = GameScreen.getMouseX(Joystick.index);
        float touchY = GameScreen.getMouseY(Joystick.index);

        return touchX >= (getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2) && touchX <= getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2 + texture.getTexture().getWidth()
                && touchY >= (getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2) && touchY <= getY() - GameScreen.player.getZ() - GameScreen.player.getCentralY() + getStage().getViewport().getWorldHeight() / 2 + texture.getTexture().getHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(texture, getX(), getY());
    }
}
