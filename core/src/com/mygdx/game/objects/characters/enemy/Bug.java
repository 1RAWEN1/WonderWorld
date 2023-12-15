package com.mygdx.game.objects.characters.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.Creature;
import com.mygdx.game.objects.characters.Projectile;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

public class Bug extends Creature {
    private boolean queen;
    public static int colonyThinking = 0;
    public Bug(boolean queen){
        super(new TextureRegion(GameScreen.loadTexture("characters\\bug.png"), 0, 0, 32, 32),
                new Rectangle3D(new Rectangle(0, 0, 10, 10), 0, 10));
        getRect().setCircle(true);

        setSpeed(1.5f);

        setPhysArmor(1);

        setCooldown(1000);

        setAttackDist(250);

        setPhysDamage(1);

        setCritChance(0.1f);

        setMaxAgility(10);

        setMass(60);

        setDelay(0.9f);

        setDealtStanTime(100);

        this.queen = queen;
        if(queen){
            setColor(new Color(1, 200f / 256, 100f / 256, 1));
            setMaxHp(50, true);

            int num = 1 + (int) (Math.random() * 2 + 0.1);
            for(int i = 0; i < num; i++)
                getInventory().add(new Item(103), this);
        }
        else{
            setMaxHp(10, true);
        }

        setAnimation(0, 6, 7);

        setCanLookInv(false);
        setPeople(false);

        int startSize = (3 + (int)(Math.random() + 0.1));
        for(int i = 0; i < startSize; i++) {
            Item item = new Item(102);
            getInventory().add(item, this);
        }
    }

    public boolean isQueen() {
        return queen;
    }

    private float targetX;
    private float targetY;

    @Override
    public void act(float delta) {
        if(getDist(GameScreen.player) <= 300 && colonyThinking < 50){
            setEnemy(GameScreen.player);
        }
        else {
            Creature creature = (Creature) getNearestObjectInRange(300, Creature.class);
            if (creature != null && !(creature instanceof Bug)) {
                setEnemy(creature);
            }
        }

        if(getRot() == 3 || getRot() == 1){
            setBoundsForTexture(36, 27);

            setRectDelta(0);

            if (getRot() == 1) {
                setStartCords(4, 99);
            } else {
                setStartCords(5, 1);
            }
        }
        else{
            setBoundsForTexture(25, 26);

            setRectDelta(5);

            if (getRot() == 0) {
                setStartCords(12, 73);
            } else {
                setStartCords(12, 40);
            }
        }

        if(getEnemy() == GameScreen.player && colonyThinking >= 50){
            setEnemy(null);
        }

        if (getEnemy() != null) {
            targetX = getEnemy().getRectCenterX();
            targetY = getEnemy().getRectCenterY();
        }
        else {
            if (targetX == 0 && targetY == 0 || getDist(targetX, targetY) < 50) {
                targetX = getCentralX() + (float) (Math.random() * 32 * 10) - 32 * 5;
                targetY = getY() + (float) (Math.random() * 32 * 10) - 32 * 5;

                targetX = Math.min(32 * 69, Math.max(16, targetX));
                targetY = Math.min(32 * 69, Math.max(16, targetY));
            }
        }

        moveToCords(targetX, targetY);

        if (getEnemy() != null && !cooldown()) {
            if (checkObject(getEnemy())) {
                Projectile projectile = GameScreen.addProjectile(new TextureRegion(GameScreen.loadTexture("inventory\\arrow.png"), 26, 0, 4, 4), 10, getZ() + (float) (getRect().getzSize() * 0.7), 4, this, 1, 107, 1);
                setProjectile(projectile);

                projectile.setDropChance(0.1f);

                dealDamage(true, getEnemy());
            }
        }

        animate();
    }
}
