package com.mygdx.game.objects.buttons.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.scenes.GameScreen;
import com.mygdx.game.scenes.Lobby;

import java.io.IOException;

public class ChangeControlButton extends Actor {
    private final TextureRegion texture = new TextureRegion(new Texture("buttons\\Counter.png"));
    private final Label label = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
    private boolean mousePressed = false;
    private boolean changeControl = false;
    private int index;
    public ChangeControlButton(int index){
        this.index = index;

        label.setText(getButName());
    }

    public String getButName(){
        if(GameScreen.buttons.get(index) > 1){
            return Input.Keys.toString(GameScreen.buttons.get(index));
        }
        else{
            return GameScreen.buttons.get(index) == 0 ? "LMB" : "RBM";
        }
    }

    @Override
    public void act(float delta) {
        label.setPosition(600 - label.getPrefWidth() / 2, getY() + 15);

        if(changeControl){
            setColor(Color.GREEN);

            boolean hasChange = false;
            for(int i = 0; i < 256; i++)
                if(Gdx.input.isKeyPressed(i)) {
                    GameScreen.buttons.set(index, i);
                    hasChange = true;
                }

            if(!hasChange && !mousePressed && touchThis()){
                for(int i = 0; i < 2; i++)
                    if(Gdx.input.isButtonPressed(i)) {
                        GameScreen.buttons.set(index, i);
                        hasChange = true;
                    }
            }

            if(hasChange){
                changeControl = false;
                label.setText(getButName());

                try {
                    GameScreen.saveButtons();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else{
            setColor(1, 1, 1, 1);
        }

        if(Gdx.input.isTouched() && !touchThis() && !mousePressed){
            changeControl = false;
            mousePressed = true;
        }
        else if(Gdx.input.isTouched() && touchThis() && !mousePressed){
            changeControl = true;
            mousePressed = true;
        }
        else if(!Gdx.input.isTouched() || !touchThis()){
            mousePressed = false;
        }
    }

    public void reload(){
        changeControl = false;
        label.setText(getButName());
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

        label.draw(batch, parentAlpha);
    }
}
