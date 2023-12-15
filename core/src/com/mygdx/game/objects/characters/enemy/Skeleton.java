package com.mygdx.game.objects.characters.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.Creature;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

public class Skeleton extends Creature {
    public Skeleton() {
        super(new TextureRegion(GameScreen.loadTexture("characters\\skeleton.png"), 0, 64 * 8, 64, 64),
                new Rectangle3D(new Rectangle(0, 0, 20, 10), 0, 50));
        getRect().setCircle(true);

        setSpeed(2);

        setMass(60);

        setCooldown(500);

        setCritChance(0.2f);

        setRectDelta(3);

        setMaxHp(15, true);

        setCanLookInv(false);

        setHeal(0.01f);

        setMaxAgility(15);

        if(Math.random() < 0.5) {
            Item item = new Item(1);
            item.setEndurance((int) ((0.2 + (Math.random() * 0.5)) * item.getEndurance()));
            getInventory().getItems()[0] = item;
        }
        if(Math.random() < 0.5) {
            Item item2 = new Item(62);
            item2.setEndurance((int) ((0.2 + (Math.random() * 0.5)) * item2.getEndurance()));
            getInventory().getItems()[2] = item2;
        }
        if(Math.random() < 0.5) {
            Item item4 = new Item(64);
            item4.setEndurance((int) ((0.2 + (Math.random() * 0.5)) * item4.getEndurance()));
            getInventory().getItems()[4] = item4;
        }
    }

    private float targetX;
    private float targetY;
    @Override
    public void act(float delta) {
        if(getDist(GameScreen.player) <= 300){
            setEnemy(GameScreen.player);
        }
        else {
            Creature creature = (Creature) getNearestObjectInRange(300, Creature.class);
            if (creature != null && !(creature instanceof Skeleton) && !(creature instanceof SkeletonMage)) {
                setEnemy(creature);
            }
        }


        if (getEnemy() != null) {
            targetX = getEnemy().getRectCenterX();
            targetY = getEnemy().getRectCenterY();
        }
        else{
            if (targetX == 0 && targetY == 0 || getDist(targetX, targetY) < 50) {
                targetX = getCentralX() + (float) (Math.random() * 32 * 10) - 32 * 5;
                targetY = getY() + (float) (Math.random() * 32 * 10) - 32 * 5;

                targetX = Math.min(32 * 69, Math.max(16, targetX));
                targetY = Math.min(32 * 69, Math.max(16, targetY));
            }
        }

        if(getDist(targetX, targetY) >= 50 || getEnemy() != null && !checkObject(getEnemy()))
            moveToCords(targetX, targetY);

        if (getEnemy() != null && !cooldown()) {
            if (checkObject(getEnemy())) {
                dealDamage(true, getEnemy());
            }
        }

        animate();
    }
}
