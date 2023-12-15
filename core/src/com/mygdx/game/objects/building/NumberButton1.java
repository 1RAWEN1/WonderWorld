package com.mygdx.game.objects.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.objects.decor.obs.Block;
import com.mygdx.game.scenes.GameScreen;

public class NumberButton1 extends Actor {
    private Label label = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
    private float value = 10;
    private String str;
    private int type;

    private TextureRegion texture = GameScreen.loadRegion("buttons/upArrow.png");
    public float deltaX;
    public float deltaY;

    public NumberButton1(int type, String str, float deltaX, float deltaY) {
        this.str = str;
        label.setText(str);

        label.setFontScale(1.5f);

        this.deltaX = deltaX;
        this.deltaY = deltaY;

        this.type = type;
    }

    public void setValue(float value) {
        this.value = value;
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
            float value1 = value;
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

            value += this.delta;
            if(this.delta != 0){
                this.delta *= 1.2;
            }

            if(value < 1){
                value = 1f;
            }

            if((int)value1 != (int)value && GameScreen.buildingObject.getBuilding() != null && GameScreen.buildingObject.getBuilding() instanceof Block) {
                switch (type) {
                    case 1:
                        GameScreen.buildingObject.getBuilding().getRect().setWidth((int) value);
                        ((Block) GameScreen.buildingObject.getBuilding()).calcMat();
                        break;
                    case 2:
                        GameScreen.buildingObject.getBuilding().getRect().setHeight((int) value);
                        ((Block) GameScreen.buildingObject.getBuilding()).calcMat();
                        break;
                    case 3:
                        float z = GameScreen.buildingObject.getBuilding().getRect().getzSize();
                        GameScreen.buildingObject.getBuilding().getRect().setzSize((int) value);
                        ((Block) GameScreen.buildingObject.getBuilding()).calcMat();
                        GameScreen.buildingObject.getBuilding().setCentralY(GameScreen.buildingObject.getBuilding().getCentralY() +
                                ((int)value - z) / 2);
                        break;
                }
            }

            label.setText(str + value);
        }
        else if(this.delta != 0){
            this.delta = 0;
        }

        if(isVisible())
            label.setPosition(getX() - label.getPrefWidth(), getY());
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        label.setVisible(visible);
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

