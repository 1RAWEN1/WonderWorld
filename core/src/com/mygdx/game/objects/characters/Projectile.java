package com.mygdx.game.objects.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.decor.Cell;
import com.mygdx.game.objects.decor.Obstacle;
import com.mygdx.game.objects.inventory.FallenItem;
import com.mygdx.game.objects.inventory.Item;
import com.mygdx.game.scenes.GameScreen;

import java.util.ArrayList;
import java.util.Iterator;

public class Projectile extends MyActor implements MovableObject, Pool.Poolable {
    private Creature owner;
    private float rot;
    private float xSpeed;
    private float ySpeed;
    private float zSpeed;
    private float speed;
    private float damage;
    private int damageType;
    private int itemIndex;
    public boolean active;
    private int stacks = 0;
    private int currentStacks = 0;
    public Projectile() {
        super(new Rectangle3D(new Rectangle(0, 0, 1, 1), 0, 1));
    }

    public float getDamage() {
        return damage;
    }

    public Creature getOwner() {
        return owner;
    }

    public void init(TextureRegion textureRegion, float speed, float z, float damage
            , Creature owner, int damageType, int itemIndex, int stacks) {
        this.stacks = stacks;
        currentStacks = 0;

        setTexture(textureRegion);

        this.damageType = damageType;
        this.owner = owner;
        this.damage = damage;
        this.speed = speed;

        if(damageType == 2){
            getRect().getRect().setSize(getScaleX() * 50, getScaleY() * 50);
            getRect().setzSize(getScaleX() * 50);

            getRect().setCircle(true);
        }
        else{
            getRect().getRect().setSize(1, 1);
            getRect().setzSize(1);
            getRect().setCircle(false);
        }

        setZ(z);

        this.itemIndex = itemIndex;

        if(damage == 4){
            setDropChance(0.1f);
        }

        zSpeed = 0;
        dist = 0;
        autoRotate = false;

        active = true;
    }

    public int getStacks() {
        return stacks;
    }

    public void setStacks(int stacks) {
        this.stacks = stacks;
    }

    private float dist;
    private float maxDist;

    public void setMaxDist(float maxDist) {
        this.maxDist = maxDist;
    }

    public void setDropChance(float dropChance) {
        this.dropChance = dropChance;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
        targetRot = getRotByDelta(xSpeed, ySpeed);
    }

    public void setzSpeed(float zSpeed) {
        this.zSpeed = zSpeed;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDamageType() {
        return damageType;
    }

    private float dropChance = 0.9f;
    private ArrayList<MyActor> touching = new ArrayList<>();
    private boolean autoRotate = false;
    private float targetRot;

    public boolean isAutoRotate() {
        return autoRotate;
    }

    public void setAutoRotate(boolean autoRotate) {
        this.autoRotate = autoRotate;
    }

    @Override
    public void act(float delta) {
        //Obstacle obstacle = ((Obstacle) getOneIntersectingObject(Obstacle.class));
        Projectile proj = (Projectile) getOneIntersectingObject(Projectile.class);
        if(isTouching(Obstacle.class) && !getRect().overlaps(owner.getRect()) && stacks != -1 ||
        proj != null && proj.getDamageType() == 2){
            if(Math.random() < dropChance && itemIndex != 0) {
                FallenItem fallenItem = new FallenItem(new Item(itemIndex));
                fallenItem.setPosition(getX(), getY());
                fallenItem.setZ(getZ());
                getStage().addActor(fallenItem);

                fallenItem.sort();
            }

            /*int i;
            for(i = 0; i < obstacle.getMaterials().length; i += 2){
                if(obstacle.getMaterials()[i + 1] == 91 && obstacle.getMaterials()[i] > 0){
                    break;
                }
            }
            obstacle.getMaterials()[i]--;*/

            remove();
            active = false;
        }
        else {
            if(damageType == 1) {
                zSpeed -= 0.4;
            }

            setZ(getZ() + zSpeed);

            Cell cell = GameScreen.getCellAt(getRectCenterX(), getRectCenterY());
            if (cell != null && getZ() + getRect().getzSize() / 2 <= cell.getZ(getRectCenterX(), getRectCenterY())) {
                //setZ(cell.getZ(getRectCenterX(), getRectCenterY()));

                if(Math.random() < dropChance && damageType == 1) {
                    FallenItem fallenItem = new FallenItem(new Item(itemIndex));
                    fallenItem.setPosition(getX(), getY());
                    fallenItem.setZ(getZ());
                    getStage().addActor(fallenItem);

                    fallenItem.sort();
                }

                remove();
                active = false;
            } else {
                if(autoRotate){
                    Creature creature = (Creature) getNearestObjectInRange(100, Creature.class, owner);
                    if(creature != null && creature.getTeam() != owner.getTeam()) {
                        if (getZ() + getRect().getzSize() / 2 != creature.getZ() + creature.getRect().getzSize()) {
                            zSpeed = (getZ() + getRect().getzSize() / 2 > creature.getZ() + creature.getRect().getzSize()) ?
                                    -2f : 2f;
                        }

                        float targetRot = getRotTo(creature );
                        if(targetRot != this.targetRot) {
                            if(targetRot > this.targetRot && targetRot - this.targetRot <= 180
                                    || targetRot < this.targetRot && this.targetRot - targetRot > 180){
                                this.targetRot += 5;
                            }
                            else if(targetRot > this.targetRot && targetRot - this.targetRot > 180
                                    || targetRot < this.targetRot && this.targetRot - targetRot <= 180){
                                this.targetRot -= 5;
                            }

                            setxSpeed((float) (Math.cos(Math.toRadians(this.targetRot)) * getSpeed()));
                            setySpeed((float) (Math.sin(Math.toRadians(this.targetRot)) * getSpeed()));
                        }
                    }
                }

                setPosition(getX() + xSpeed, getY() + ySpeed);
                dist ++;

                if(damageType == 1) {
                    rot = getRotByDelta(xSpeed, ySpeed);

                    float rotDelta = getRotByDelta(speed, zSpeed);

                    rot += rotDelta * Math.cos(Math.toRadians(rot));

                    setRotation(rot);
                }

                sort();

                Creature creature = (Creature) getOneIntersectingObject(Creature.class, owner);
                if(creature != null && !creature.isStaticObj() && stacks > 0){
                    if(damageType == 1)
                        owner.dealDamageWithDelay(damage, creature, damageType, true, this);
                    else
                        explode(stacks);

                    currentStacks++;
                    if(currentStacks >= stacks) {
                        remove();
                        active = false;
                    }
                }
                else if(creature != null && !creature.isStaticObj()){
                    boolean dealDamage = true;
                    for(MyActor actor : touching){
                        if (actor == creature) {
                            dealDamage = false;
                            break;
                        }
                    }

                    if(dealDamage){
                        if(damageType == 1)
                            owner.dealDamageWithDelay(damage, creature, damageType, true, this);
                        else
                            explode(stacks);

                        currentStacks ++;
                        touching.add(creature);
                    }
                }
                else if(damageType == 2 && dist >= maxDist){
                    remove();
                    active = false;
                }

                Iterator<MyActor> it = touching.iterator();
                while(it.hasNext()) {
                    MyActor actor = it.next();
                    if(!getRect().overlaps(actor.getRect()))
                        it.remove();
                }
            }
        }
    }

    public float getMaxDist() {
        return maxDist;
    }

    private void explode(int stacks){
        if(stacks > 0 && currentStacks >= stacks) {
            getRect().setWidth(getRect().getWidth() * 2);
            getRect().setHeight(getRect().getHeight() * 2);
            getRect().setzSize(getRect().getzSize() * 2);

            getRect().setPosition(getRect().getX() - getRect().getWidth() / 4, getRect().getY() - getRect().getHeight() / 4,
                    getRect().getZ() - getRect().getzSize() / 4);
        }

        for(MyActor act : getIntersectingObjects(Creature.class)) {
            if(act != owner)
                owner.dealDamageWithDelay(damage, (Creature) act, damageType, true, this);
        }

        if(stacks > 0 && currentStacks >= stacks) {
            remove();
            active = false;
        }
    }

    public void setNeedZSpeed(double dist){
        double angle = Math.toDegrees(Math.asin(dist * 0.4 / Math.pow(speed, 2))) / 2;
        angle = Double.isNaN(angle) ? 45 : angle;
        setzSpeed((float) (Math.sin(Math.toRadians(angle)) * speed));

        speed *= Math.cos(Math.toRadians(angle));
    }

    @Override
    public void reset() {

    }
}
