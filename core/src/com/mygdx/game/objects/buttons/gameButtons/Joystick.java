package com.mygdx.game.objects.buttons.gameButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.scenes.GameScreen;

public class Joystick extends Actor {
    public static int index;
    private float rot;
    private float radius;
    @Override
    public void act(float delta) {
        setPosition(GameScreen.player.getX() - 400, GameScreen.player.getY() + GameScreen.player.getZ() - 250);

        if(Gdx.input.isTouched() && touchThis()){
            float touchX = GameScreen.getMouseX();
            float touchY = GameScreen.getMouseY();
            rot = (float) (180 * Math.atan2(touchY - 170, touchX - 240) / Math.PI);

            radius = Math.min(size / 2 - (int)(size * 0.2), (float) Math.sqrt(Math.pow(touchX - 240, 2) + Math.pow(touchY - 170, 2)));

            GameScreen.player.setMovingRot(rot);

            index++;
        }
        else{
            radius = 0;
        }

        setVisible(!GameScreen.player.isSeeInventory() && GameScreen.dialogReader.getTalker() == null);
    }

    public boolean touchThis(){
        float touchX = GameScreen.getMouseX();
        float touchY = GameScreen.getMouseY();
        radius = (float) Math.sqrt(Math.pow(touchX - 240, 2) + Math.pow(touchY - 170, 2));


        return radius <= 200;
    }

    private final int size = 150;
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(GameScreen.isHelpLabelVisible() && GameScreen.getButIndex() == 1){
            setColor(1, 0.8f, 0.1f, 1);
        }
        else{
            setColor(1, 1, 1, 1);
        }

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);

        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA4444);
        pm.setColor(new Color(46f / 256, 55f / 256, 67f / 256, 1));
        pm.drawCircle(size / 2, size / 2, size / 2 - 1);
        pm.fillCircle(size / 2 + (int)(Math.cos(Math.toRadians(rot)) * radius), size / 2 - (int)(Math.sin(Math.toRadians(rot)) * radius), (int)(size * 0.2));
        batch.draw(new Texture(pm), getX(), getY());
        pm.dispose();
    }
}
