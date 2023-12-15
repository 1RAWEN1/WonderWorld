package com.mygdx.game.objects.buttons.magicButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.scenes.GameScreen;

public class NumberButton extends Actor {
    private Label label = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
    public static float[] magicPar = {1, 4, 16, 20, 10, 1, 0};
    private int number;
    private String str;

    private TextureRegion texture = GameScreen.loadRegion("buttons/upArrow.png");

    private float deltaX;
    private float deltaY;

    public NumberButton(int number, String str, float deltaX, float deltaY) {
        this.number = number;

        this.str = str;
        label.setText(str);

        this.deltaX = deltaX;
        this.deltaY = deltaY;

        label.setFontScale(1.5f);
    }

    private float delta = 0;
    private boolean start = true;
    @Override
    public void act(float delta) {
        if(start){
            getStage().addActor(label);
            GameScreen.sortCells();

            start = false;
        }
        if(Gdx.input.isTouched()){
            float x = GameScreen.getMouseX();
            float y = GameScreen.getMouseY();
            float myx = getX() - GameScreen.player.getCentralX() + getStage().getViewport().getWorldWidth() / 2;
            float myy = getY() - GameScreen.player.getCentralY() - GameScreen.player.getZ() + getStage().getViewport().getWorldHeight() / 2;
            if(x > myx && x < myx + texture.getRegionWidth()){
                if(y > myy && y < myy + texture.getRegionHeight() && this.delta <= 0){
                    this.delta = 0.01f;
                }
                else if (y < myy && y > myy - texture.getRegionHeight() && this.delta >= 0){
                    this.delta = -0.01f;
                }

                if(this.delta < -1){
                    this.delta = -1;
                }
                else if(this.delta > 1){
                    this.delta = 1;
                }
            }

            magicPar[number] += this.delta;
            if(this.delta != 0){
                this.delta *= 1.2;
            }

            if(magicPar[number] < 0.01){
                magicPar[number] = 0.01f;
            }

            label.setText(str + magicPar[number]);
        }
        else if(this.delta != 0){
            this.delta = 0;
        }

        setVisible(GameScreen.dialogReader.getTalker() == null && GameScreen.player.isSeeMagic());
        label.setVisible(GameScreen.dialogReader.getTalker() == null && GameScreen.player.isSeeMagic());

        setPosition(GameScreen.player.getX() + deltaX, GameScreen.player.getY() + GameScreen.player.getZ() + deltaY);
        label.setPosition(getX() - label.getPrefWidth(), getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                texture.getRegionWidth(), texture.getRegionHeight(), getScaleX(), getScaleY(), getRotation());

        batch.draw(texture, getX() + texture.getRegionWidth(), getY(), getOriginX(), getOriginY(),
                texture.getRegionWidth(), texture.getRegionHeight(), getScaleX(), getScaleY(), 180);
    }
}
