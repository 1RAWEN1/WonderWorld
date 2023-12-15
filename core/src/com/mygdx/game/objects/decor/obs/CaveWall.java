package com.mygdx.game.objects.decor.obs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.decor.Cell;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.scenes.GameScreen;

public class CaveWall extends Obstacle {
    int height = -1;
    public CaveWall(int xSize, int ySize, int zSize){
        super(new Rectangle3D(new Rectangle(0, 0, xSize, ySize), 0, zSize));

        if(zSize == 32) {
            setRectDelta(4);
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\rocks.png"), 262, 32, xSize, 32));

            getRect().setCircle(true);

            int[] materials = new int[]{0, 92, 0, 93, 0, 100};
            for(int i = 0; i < 7; i++){
                if(Math.random() < 0.2){
                    materials[0]++;
                }
                else if(Math.random() < 0.2){
                    materials[2]++;
                }
                else{
                    materials[4]++;
                }
            }
            setMaterials(materials);
        }
        else if(zSize == 20) {
            setScaleX(106f / 96);
            setRectDeltaZ(54);
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\caves.png"), 160, 8, 96, 74));
        }
        else if(zSize == 10){
            //, 270, 90 - z / 4, xSize, ySize + z / 4
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\basalt.png")));
            setRectDelta(21);

            setWithoutPhys(true);

            setMaterials(new int[]{7, 100});
        }
        else if(xSize == 32){
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\bas.png")));
            setRectDelta(15);

            setMaterials(new int[]{40, 100});
        }
    }

    public CaveWall(int height){
        super(new Rectangle3D(new Rectangle(0, 0, 32, 32), 0, height + 10));
        this.height = height;

        setTexture(new TextureRegion(GameScreen.loadTexture("decor\\bas.png")));
        setRectDelta(15);

        setScaleX(32 / 55f);
        setScaleY(getScaleX());

        setMaterials(new int[]{height + 10, 100});
    }

    public int getHeight1() {
        return height;
    }

    public void setHeight1(int height1) {
        height = height1;
        getRect().setzSize(height);
        getMaterials()[0] = height;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (height == -1){
            super.draw(batch, parentAlpha);
        }
        else{
            if(transparency > 0) {
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
                if(cell != null)
                    color.lerp(cell.getColor(), 1 - cell.getColor().r);

                batch.setColor(color.r, color.g, color.b, getTransparency());

                int y = 0;
                getTexture().setRegion(0, 106, 55, 8);
                draw(y, batch);
                y += 8 * getScaleY();

                getTexture().setRegion(0, 47, 55, 59);
                for(int i = 0; i < height / 59; i++){
                    draw(y, batch);
                    y += 59 * getScaleY();
                }

                getTexture().setRegion(0, 47, 59, height % 59);
                draw(y, batch);
                y += (height % 59) * getScaleY();

                getTexture().setRegion(0, 0, 55, 47);
                draw(y, batch);

                if(drawAfter.size() > 0) {
                    for (MyActor act : drawAfter) {
                        act.setVisible(true);
                        act.draw(batch, parentAlpha);
                    }
                    drawAfter.clear();
                }
            }
            else{
                remove();
            }
        }
    }

    public void draw(int y, Batch batch){
        batch.draw(getTexture(), getX(), getY() + getZ() + y, getOriginX(), getOriginY(), getTexture().getRegionWidth(), getTexture().getRegionHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
