package com.mygdx.game.objects.characters.peaceful;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.Creature;
import com.mygdx.game.objects.decor.Cell;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

public class Fish extends Creature {

    public Fish() {
        super(new TextureRegion(GameScreen.loadTexture("characters/lpc_fish.png"), 0, 0, 32 ,32),
                new Rectangle3D(new Rectangle(0, 0, 10, 10), 0, 10));

        getRect().setCircle(true);

        setDefaultPhysDamage(0);

        setSpeed(3f);

        setMaxHp(5, true);

        setMass(10);

        setMaxAgility(30);

        setRectDelta(16);

        setPeople(false);
        setCanLookInv(false);

        setAnimation(0, 4, 5);

        Item item = new Item(95);
        getInventory().add(item, this);
        getInventory().addNeedRes(0);
    }

    private float targetX;
    private float targetY;
    private boolean changeTarget = false;

    @Override
    public void act(float delta) {
        if (getRot() == 3 || getRot() == 1) {
            if (getRot() == 1) {
                setStartCords(0, 32);
            } else {
                setStartCords(0, 64);
            }
        } else {
            if (getRot() == 0) {
                setStartCords(0, 0);
            } else {
                setStartCords(0, 96);
            }
        }

        Cell cell = GameScreen.getCellAt(getCentralX(), getCentralY());
        if(cell != null && cell.getDepth() >= -20){
            for (int i = 0; i < 4; i++) {
                Cell cell1 = GameScreen.getCellAt((float) (cell.getX() + 16 + (Math.cos(Math.toRadians(i * 90)) * 32)), (float) (cell.getY() + 16 + (Math.sin(Math.toRadians(i * 90)) * 32)));
                if (cell1 != null && cell1.getDepth() < cell.getDepth()) {
                    targetX = cell1.getX() + 16;
                    targetY = cell1.getY() + 16;

                    sum = 0;

                    changeTarget = true;
                    break;
                }
            }
        }
        else if (getEnemy() != null) {
            float targetRot = getOppositeRot(getRotTo(getEnemy()));
            targetX = getX() + (float) (Math.cos(Math.toRadians(targetRot)) * 64);
            targetY = getY() + (float) (Math.sin(Math.toRadians(targetRot)) * 64);
        }
        else if (targetX == 0 && targetY == 0 || getDist(targetX, targetY) < 10 || changeTarget) {
            targetX = getCentralX() + (float) (Math.random() * 32 * 10) - 32 * 5;
            targetY = getY() + (float) (Math.random() * 32 * 10) - 32 * 5;

            changeTarget = false;
        }

        if(getZ() > -10)
            setZ(getZ() - 2);

        if(getDist(targetX, targetY) >= 10) {
            moveToCords(targetX, targetY);
        }

        animate();
    }
}
