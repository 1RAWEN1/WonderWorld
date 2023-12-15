package com.mygdx.game.objects.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.objects.buttons.gameButtons.Joystick;
import com.mygdx.game.scenes.GameScreen;

public class Button extends Actor {
    private final TextureRegion texture;

    public Button(TextureRegion texture, float scale, int index){
        this.index = index;
        setScale(scale);
        this.texture = texture;
    }

    private float xDelta;
    private float yDelta;

    public void setxDelta(float xDelta) {
        this.xDelta = xDelta;
    }

    public void setyDelta(float yDelta) {
        this.yDelta = yDelta;
    }

    private final int index;
    @Override
    public void act(float delta) {
        if(Gdx.input.isTouched(Joystick.index) && touchThis()){
            GameScreen.pressedKey = index;
        }

        setPosition(GameScreen.player.getX() + xDelta, GameScreen.player.getY() + GameScreen.player.getZ() + yDelta);

        setVisible(!GameScreen.player.isSeeInventory() && GameScreen.dialogReader.getTalker() == null);
    }

    public boolean touchThis(){
        float touchX = GameScreen.getMouseX(Joystick.index);
        float touchY = GameScreen.getMouseY(Joystick.index);

        return touchX >= (getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2) && touchX <= getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2 + texture.getTexture().getWidth()
                && touchY >= (getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2) && touchY <= getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2 + texture.getTexture().getHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(GameScreen.isHelpLabelVisible() && GameScreen.getButIndex() == index){
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
