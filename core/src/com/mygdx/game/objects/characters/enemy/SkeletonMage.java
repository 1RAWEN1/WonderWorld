package com.mygdx.game.objects.characters.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.Creature;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

public class SkeletonMage extends Creature {
    public SkeletonMage(){
        super(new TextureRegion(GameScreen.loadTexture("characters\\skeleton.png"), 0, 64 * 8, 64, 64),
                new Rectangle3D(new Rectangle(0, 0, 20, 10), 0, 50));
        getRect().setCircle(true);

        setSpeed(2);

        setMass(60);

        setRectDelta(3);

        setMaxHp(15, true);

        setCanLookInv(false);

        setMaxAgility(15);

        setMagDamage(2);

        Item item = new Item(65);
        getInventory().getItems()[1] = item;

        Item item2 = new Item(66);
        getInventory().getItems()[2] = item2;
        Item item3 = new Item(67);
        getInventory().getItems()[3] = item3;
        Item item4 = new Item(68);
        getInventory().getItems()[4] = item4;

        getInventory().add(new Item(101), this);

        setCooldown(700);

        setCritChance(0.2f);

        setAttackDist(150);

        setDropDist(5);

        setAttackRange(60);

        setMP(1200);
    }

    private float targetX;
    private float targetY;
    private long lastSpawnTime;
    private int spawnTime = 30000;
    @Override
    public void act(float delta) {
        if(getDist(GameScreen.player) <= 300){
            setEnemy(GameScreen.player);
        }
        else {
            Creature creature = (Creature) getNearestObjectInRange(300, Creature.class);
            if (creature != null && !(creature instanceof Skeleton)) {
                setEnemy(creature);
            }
        }

        if (getEnemy() != null && !(getEnemy() instanceof Skeleton)) {
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

        moveToCords(targetX, targetY);

        if (getEnemy() != null && !cooldown()) {
            if (checkObject(getEnemy()) && getMagicPower() > 60) {
                dealDamage(false, getEnemy());
            }
        }

        animate();

        if(TimeUtils.timeSinceMillis(lastSpawnTime) >= spawnTime){
            for(int i = 45; i < 360; i += 90){
                Skeleton skeleton = new Skeleton();
                skeleton.setPosition((float) (getX() + Math.cos(Math.toRadians(i)) * 32), (float) (getY() + Math.sin(Math.toRadians(i)) * 32));
                getStage().addActor(skeleton);

                skeleton.sort();
            }

            lastSpawnTime = TimeUtils.millis();
        }
    }
}
