package com.mygdx.game.objects.characters.withInventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.scenes.GameScreen;

public class Oven extends ObsWithInventory {
    private int type;
    public Oven(int type){
        super(new Rectangle3D(new Rectangle(0, 0, 32, 20), 0, 24));

        setMass(250);

        setRectDelta(10);

        this.type = type;

        getInventory().addNeedRes(91);
        getInventory().addNeedRes(92);
        getInventory().addNeedRes(93);
        getInventory().addNeedRes(95);
        getInventory().setInventorySize(50);

        if(type == 1){
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\inside_b.png"), 255, 287, 32, 124));

            setMaterials(new int[]{5, 94});
        }
        else{
            setTexture(new TextureRegion(GameScreen.loadTexture("decor\\stone_oven.png")));

            setMaterials(new int[]{7, 100});
        }

        setStatic(false);
    }

    public int getType() {
        return type;
    }

    private final TextureRegion animation = new TextureRegion(GameScreen.loadTexture("decor\\WaterAndFire.png"));
    private long lastCreateTime = 0;
    @Override
    public void act(float delta) {
        for (int i = 0; i < getInventory().getAllItems().size(); i++) {
            if (getInventory().getAllItems().get(i).getNeedFuelPower() != 0) {
                targetItem = getInventory().getAllItems().get(i);
            } else if (getInventory().getAllItems().get(i).getFuelPower() != 0) {
                fuel = getInventory().getAllItems().get(i);
            }
        }

        if (targetItem != null && fuel != null && fuel.getFuelPower() >= targetItem.getNeedFuelPower()) {
            if (prevTargetItem != targetItem) {
                lastCreateTime = TimeUtils.millis();
                prevTargetItem = targetItem;
            } else if (TimeUtils.timeSinceMillis(lastCreateTime) >= targetItem.getCreationTime()) {
                fuel.setFuel(fuel.getFuel() - 1);
                if (fuel.getFuel() <= 0) {
                    getInventory().getAllItems().remove(fuel);
                    fuel = null;
                }

                targetItem.setIndex(targetItem.getCreatedItem());

                targetItem = null;
            }

            anima = true;
        }
        else{
            anima = false;
        }

        updateZ();

        physics();
    }

    private int step;
    private boolean anima;
    @Override
    public void draw(Batch batch, float parentAlpha) {
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

            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, getTransparency());
            batch.draw(getTexture(), getX(), getY() + getZ(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            if(type == 1) {
                if (anima) {
                    step++;
                    step %= 30;

                    animation.setRegion(32 + (step / 15) * 32, 192, 32, 26);

                    batch.draw(animation, getX(), getY() + getZ(), getOriginX(), getOriginY(),
                            getWidth(), animation.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
                } else {
                    step = 0;
                }
            }

            if (drawAfter.size() > 0) {
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
