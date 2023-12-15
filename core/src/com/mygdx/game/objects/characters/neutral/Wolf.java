package com.mygdx.game.objects.characters.neutral;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.Creature;
import com.mygdx.game.objects.characters.peaceful.Rabbit;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

public class Wolf extends Creature {
    private final int startSize;
    private boolean tamed = false;
    public Wolf(){
        super(GameScreen.loadRegion("characters\\wolf.png", 319, 96, 64, 32),
                new Rectangle3D(new Rectangle(0, 0, 20, 20), 0, 30));
        getRect().setCircle(true);

        setSpeed(3);

        setMaxHp(15, true);

        setCooldown(500);

        setPhysDamage(2);

        setCritChance(0.3f);

        setMaxAgility(25);

        setMass(60);

        setCanOpenDoor(true);
        setCanLookInv(false);
        setPeople(false);

        startSize = (3 + (int)(Math.random() + 0.1));
        for(int i = 0; i < startSize; i++) {
            Item item = new Item(95);
            getInventory().add(item, this);
        }

        getInventory().addNeedRes(95);
    }

    public boolean isTamed() {
        return tamed;
    }

    public void setTamed(boolean tamed) {
        this.tamed = tamed;

        if(tamed){
            setTeam(1);
        }

        setEnemy(null);
    }

    private float targetX;
    private float targetY;

    @Override
    public void act(float delta) {
        if (getRot() == 3 || getRot() == 1) {
            setBoundsForTexture(64, 32);

            setAnimation(0, 5, 7);

            setRectDelta(0);

            if (!cooldown()) {
                if (getRot() == 1) {
                    setStartCords(320, 288);
                } else {
                    setStartCords(320, 96);
                }
            } else {
                if (getRot() == 1) {
                    setStartCords(320, 351);
                } else {
                    setStartCords(320, 161);
                }
            }
        } else {
            if (!cooldown()) {
                setBoundsForTexture(32, 60);
                setAnimation(0, 4, 7);

                setRectDelta(14);

                if (getRot() == 0) {
                    setStartCords(160, 132);
                } else {
                    setStartCords(0, 132);
                }
            } else {
                setBoundsForTexture(32, 64);

                setRectDelta(16);

                setAnimation(0, 5, 7);

                if (getRot() == 0) {
                    setStartCords(160, 255);
                } else {
                    setStartCords(0, 255);
                }
            }
        }


        if (!isTamed()) {
            Creature creature = (Creature) getNearestObjectInRange(300, Rabbit.class);
            if (creature != null) {
                setEnemy(creature);
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
        }
        else{
            if (getEnemy() != null) {
                targetX = getEnemy().getRectCenterX();
                targetY = getEnemy().getRectCenterY();
            }
            else {
                targetX = GameScreen.player.getCentralX();
                targetY = GameScreen.player.getRectCenterY();
            }

            if(GameScreen.player.getEnemy() != null && GameScreen.player.getEnemy() != GameScreen.player){
                setEnemy(GameScreen.player.getEnemy());
            }
        }

        if(getDist(targetX, targetY) >= 50 || getEnemy() != null && !checkObject(getEnemy()))
            moveToCords(targetX, targetY);

        if (getEnemy() != null && !cooldown()) {
            if (checkObject(getEnemy())) {
                dealDamage(true, getEnemy());
            }
        }

        if (getInventory().getAllItems().size() > startSize) {
            getInventory().getAllItems().get(startSize).use(this);
            getInventory().getAllItems().remove(startSize);
        }

        animate();
    }
}
