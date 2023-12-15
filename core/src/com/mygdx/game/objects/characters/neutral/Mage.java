package com.mygdx.game.objects.characters.neutral;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.Creature;
import com.mygdx.game.objects.characters.Projectile;
import com.mygdx.game.objects.characters.effects.DealtEffect;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

public class Mage extends Creature {
    private int projType;
    public Mage(){
        super(new TextureRegion(GameScreen.loadTexture("characters\\villager.png"), 0, 64 * 8, 64, 64),
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

        setCooldown(700);

        setCritChance(0.2f);

        setAttackDist(150);

        setDropDist(5);

        setAttackRange(60);

        setNeedDist(160);

        setMP(1200);

        if(Math.random() < 0.2)
            getDealtEffects().add(new DealtEffect(TimeUtils.millis(), -2));

        if(Math.random() < 0.2)
            getDealtEffects().add(new DealtEffect(TimeUtils.millis(), -1));

        if(Math.random() < 0.2)
            setWithFreezing(true);

        if(Math.random() < 0.2)
            getDealtEffects().add(new DealtEffect(TimeUtils.millis(), 1));

        if(Math.random() < 0.2)
            getDealtEffects().add(new DealtEffect(TimeUtils.millis(), 2));

        if(Math.random() < 0.2)
            getDealtEffects().add(new DealtEffect(TimeUtils.millis(), -3));

        if(Math.random() < 0.2)
            getDealtEffects().add(new DealtEffect(TimeUtils.millis(), -4));

        if(Math.random() < 0.2)
            getDealtEffects().add(new DealtEffect(TimeUtils.millis(), -5));

        setProjNum((int)((Math.random() * 5) + 1));
        setProjAngle((float)(Math.random() * (360 / getProjNum())));

        projType = (int)(Math.random() * 3);
    }

    private float targetX;
    private float targetY;
    @Override
    public void act(float delta) {
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

        moveToCords(targetX, targetY);

        if (getEnemy() != null && !cooldown()) {
            if (checkObject(getEnemy()) && getMagicPower() > 30) {
                if(projType < 2) {
                    Projectile projectile = GameScreen.addProjectile(new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 635, 465, 50, 50), 10, getZ() + (float) (getRect().getzSize() * 0.5), getMagDamage(), this, 2, 0, projType == 1 ? -1 : 1);
                    Color color = null;
                    for (DealtEffect dealtEffect : getDealtEffects()) {
                        if (color == null) {
                            color = dealtEffect.getColor().cpy();
                        } else {
                            color.lerp(dealtEffect.getColor(), 0.5f);
                        }
                    }
                    if (isWithFreezing()) {
                        if (color != null)
                            color.lerp(new Color(26f / 256, 175f / 256, 225f / 256, 1), 0.5f);
                        else
                            color = new Color(26f / 256, 175f / 256, 225f / 256, 1);
                    }
                    if (color == null) {
                        color = new Color(220f / 256, 220f / 256, 220f / 256, 1);
                    }
                    projectile.setColor(color);
                    projectile.setScale(20f / 50);
                    projectile.setAutoRotate(true);

                    projectile.setMaxDist(16);

                    setProjectile(projectile);

                    dealDamage(false, getEnemy());
                }
                else if(getMagicPower() > 90) {
                    dealDamage(false, getEnemy());
                }
            }
        }

        if(getHp() < 1){
            setHp(1);
            setEnemy(null);
        }

        animate();
    }
}
