package com.mygdx.game.objects.characters.peaceful;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.Projectile;
import com.mygdx.game.objects.characters.Talker;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

public class Rabbit extends Talker {
    private final boolean shadow;
    private boolean follow;
    public Rabbit(boolean shadow){
        super(GameScreen.loadRegion("characters\\rabbit.png", 22, 46, 37, 36),
                new Rectangle3D(new Rectangle(0, 0, 15, 15), 0, 15));
        getRect().setCircle(true);

        setDefaultPhysDamage(0);

        setSpeed(2.5f);

        setMaxHp(7, true);

        setMass(40);

        setMaxAgility(20);

        setPeople(false);
        setCanLookInv(false);

        setAnimation(0, 4, 5);
        this.shadow = shadow;

        if(!isShadow()) {
            Item item = new Item(95);
            getInventory().add(item, this);
        }
        else{
            Item item = new Item(105);
            getInventory().add(item, this);
        }

        getInventory().addNeedRes(0);

        if(shadow) {
            setEnemy(GameScreen.player);

            setMagDamage(3);
            setPhysDamage(2);

            setMaxHp(50, true);
            setColor(new Color(25f / 256, 0, 25f / 256, 1));

            phrases.add("Hello, my name is Shadow. It's been a long time since any\nwizard could contact me. As a reward, I will give you a\ngift. But I have to evaluate your strength to give\nyou a suitable one.");
            phrases.add("Your strength is not enough yet. But this is not our last\nmeeting. Everything is still ahead. Here's your present.");
            phrases.add("You're a strong magician. And I am ready to help you in\nyour further development.");
        }
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
        setTeam(follow ? 1 : 0);
    }

    public boolean isShadow() {
        return shadow;
    }

    private float targetX;
    private float targetY;

    private long timer;
    private final int abilityTime = 10000;
    private int num;

    @Override
    public void act(float delta) {
        setBoundsForTexture(37, 36);
        if(!isLookInv()) {
            if (getRot() == 3 || getRot() == 1) {
                if (getRot() == 1) {
                    setStartCords(19, 287);
                } else {
                    setStartCords(15, 216);
                }
            } else {
                if (getRot() == 0) {
                    setStartCords(23, 133);
                } else {
                    setStartCords(23, 46);
                }
            }

            if(follow){
                if(GameScreen.player.getEnemy() != null && GameScreen.player.getEnemy() != GameScreen.player){
                    setEnemy(GameScreen.player.getEnemy());
                }
            }

            if (getEnemy() != null && !shadow) {
                float targetRot = getOppositeRot(getRotTo(getEnemy()));
                targetX = getX() + (float) (Math.cos(Math.toRadians(targetRot)) * 64);
                targetY = getY() + (float) (Math.sin(Math.toRadians(targetRot)) * 64);
            } else if (getEnemy() != null) {
                targetX = getEnemy().getCentralX();
                targetY = getEnemy().getRectCenterY();
            } else if(shadow && follow){
                targetX = GameScreen.player.getCentralX();
                targetY = GameScreen.player.getRectCenterY();
            } else {
                if (targetX == 0 && targetY == 0 || getDist(targetX, targetY) < 50) {
                    targetX = getCentralX() + (float) (Math.random() * 32 * 10) - 32 * 5;
                    targetY = getY() + (float) (Math.random() * 32 * 10) - 32 * 5;
                }
            }

            if(getDist(targetX, targetY) >= 50)
                moveToCords(targetX, targetY);

            if (shadow && TimeUtils.timeSinceMillis(timer) < abilityTime) {
                setCooldown(700);

                setCritChance(0.2f);

                setAttackDist(150);

                setDropDist(5);

                setAttackRange(60);

                if (checkObject(getEnemy())) {
                    dealDamage(false, getEnemy());
                }
            } else if (shadow && getEnemy() != null) {
                setCooldown(500);

                setCritChance(0.2f);

                setDealtStanTime(500);

                setDropDist(5);

                setAttackDist(500);

                setAttackRange(180);

                Projectile projectile = GameScreen.addProjectile(new TextureRegion(GameScreen.loadTexture("inventory\\mine.png"), 635, 465, 50, 50), 10, getZ() + (float) (getRect().getzSize() * 0.5), getMagDamage(), this, 2, 0, 1);
                projectile.setColor(new Color(150f / 256, 0, 150f / 256, 1));
                projectile.setScale(10f / 50);
                setProjectile(projectile);
                projectile.setMaxDist(getAttackDist() / 10);

                dealDamage(true, getEnemy());
                num++;

                if (num == 75) {
                    num = 0;
                    timer = TimeUtils.millis();
                }
            }
            /*else{
                float rot = 0;
                if (tree != null)
                    rot = getRotTo(tree);

                MyActor tree1 = getObject(rot, Tree.class);

                if (tree1 != null && !cooldown()) {
                    dealPhysDamage(rot);
                }
            }*/

            if(getHp() < 0 && getEnemy() != null && !follow && shadow){
                setEnemy(null);

                setPhraseNum(2);
                GameScreen.dialogReader.setTalker(this);

                giveGift(getHp());

                setHp(0.01f);

                setFollow(true);
            }

            animate();
        }
    }

    private boolean giveGift = false;

    public String getPhrase(){
        return phrases.get(phraseNum);
    }

    public void giveGift(float hp){
        if(!giveGift) {
            if (hp < getMaxHp() / 2) {
                GameScreen.player.getInventory().add(new Item(101), this);
            }
            if (hp < getMaxHp() / 4) {
                GameScreen.player.getInventory().add(new Item(108), this);
            }

            giveGift = true;
        }
    }

    public void updatePhraseNum() {
        if(phraseNum < 1){
            phraseNum++;

            setEnemy(GameScreen.player);
            setLookInv(false);

            GameScreen.dialogReader.setTalker(null);
        }
        else{
            if(phraseNum == 2){
                setHp(getMaxHp());
            }
            else {
                setHp(-1);
            }
        }
    }
}
