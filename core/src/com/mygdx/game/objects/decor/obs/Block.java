package com.mygdx.game.objects.decor.obs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.decor.Cell;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.scenes.GameScreen;

public class Block extends Obstacle {
    private static TextureRegion texture = new TextureRegion(new Texture("decor\\cave_back.png"));
    public Block() {
        super(new Rectangle3D(new Rectangle(1, 1, 10, 10) , 1, 10));

        setTexture(texture);

        setMaterials(new int[]{0, 100});

        calcMat();
    }

    public void calcMat(){
        float v = getRect().getWidth() * getRect().getHeight() * getRect().getzSize();
        int prevMat = getMaterials()[0];
        getMaterials()[0] = (int)(v / 500);

        setBoundsForTexture((int) getRect().getWidth(), (int) (getRect().getHeight() + getRect().getzSize()));
        setRectDelta((int) (getRect().getHeight() / 2));

        if(getStage() != null) {
            GameScreen.buildingObject.setMaterials(getMaterials());
            GameScreen.buildingObject.getMaterials()[0] -= prevMat - getMaterials()[0];

            sort();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (transparency > 0) {
            boolean delete = true;
            for (int i = 0; i < getMaterials().length; i += 2) {
                if (getMaterials()[i] > 0) {
                    delete = false;
                    break;
                }
            }

            if (overlapsPlayer() && transparency > 0.3 && getY() < GameScreen.player.getY() &&
                    GameScreen.player.getZ() < getRect().getZ() + getRect().getzSize() ||
                    overlapsPlayer() && transparency > 0.3 && getZ() > GameScreen.player.getZ() + GameScreen.player.getRect().getzSize()) {
                transparency -= 0.02;
            } else if (delete) {
                transparency -= 0.02;
            } else if (transparency < 1) {
                transparency += 0.02;
            }
            Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
            Color color = getColor().cpy();
            if (cell != null)
                color.lerp(cell.getColor(), 1 - cell.getColor().r);

            batch.setColor(color.r, color.g, color.b, transparency);
            float x;
            float y = getY() + getZ();
            for(int i = (int)getRect().getzSize() + (int)getRect().getHeight(); i > 0; i -= getTexture().getRegionHeight()){
                x = getX();
                for(int j = (int)getRect().getWidth(); j > 0; j -= getTexture().getRegionWidth()){
                    int h = Math.min(i, 32);
                    int w = Math.min(j, 32);

                    getTexture().setRegion(0, 0, w, h);
                    batch.draw(getTexture(), x, y, getOriginX(), getOriginY(),
                            w, h, getScaleX(), getScaleY(), getRotation());

                    x += w;
                }
                y += Math.min(i, getTexture().getRegionHeight());
            }

            batch.setColor(0.5f, 0.5f, 0.5f, transparency);
            batch.draw(GameScreen.loadRegion("cave"), getX(), getY() + getZ() + getRect().getzSize(), getOriginX(), getOriginY(),
                    getRect().getWidth(), 1, getScaleX(), getScaleY(), getRotation());
        } else {
            remove();
        }

        if(drawAfter.size() > 0) {
            for (MyActor act : drawAfter) {
                act.setVisible(true);
                act.draw(batch, parentAlpha);
            }
            drawAfter.clear();
        }
    }
}
