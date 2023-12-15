package com.mygdx.game.objects.buttons.gameButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.scenes.GameScreen;

public class PortalButton extends Actor {
    private final TextureRegion texture = new TextureRegion(new Texture("buttons\\portal.png"));
    public PortalButton(){
        setScale(0.7f);
    }

    @Override
    public void act(float delta) {
        if(Gdx.input.isTouched(Joystick.index) && touchThis()) {
            GameScreen.pressedKey = 9;
        }

        setVisible(!GameScreen.player.isSeeInventory() && GameScreen.dialogReader.getTalker() == null && GameScreen.player.getMagicType() >= 1);

        setPosition(GameScreen.player.getX() + 410, GameScreen.player.getY() + GameScreen.player.getZ() - 180);
    }

    public boolean touchThis(){
        float touchX = GameScreen.getMouseX(Joystick.index);
        float touchY = GameScreen.getMouseY(Joystick.index);

        return touchX >= (getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2) && touchX <= getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2 + texture.getTexture().getWidth() * getScaleX()
                && touchY >= (getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2) && touchY <= getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2 + texture.getTexture().getHeight() * getScaleY();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(GameScreen.isHelpLabelVisible() && GameScreen.getButIndex() == 9){
            setColor(0.9f, 0.8f, 0.6f, 1);
        }
        else{
            setColor(1, 1, 1, 1);
        }

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                texture.getRegionWidth(), texture.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
