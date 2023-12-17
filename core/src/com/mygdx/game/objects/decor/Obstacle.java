package com.mygdx.game.objects.decor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.objects.MyActor;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.objects.characters.Creature;
import com.mygdx.game.objects.characters.withInventory.ObsWithInventory;
import com.mygdx.game.objects.decor.obs.Block;
import com.mygdx.game.objects.mechanisms.Mech;
import com.mygdx.game.scenes.GameScreen;

public class Obstacle extends MyActor implements StaticObject {
    private boolean isStatic = true;
    private float mass;
    private int[] materials;
    private float slimeCof;
    private String picName;
    public Cell myCell;
    public Obstacle(String picName, TextureRegion textureRegion, Rectangle3D rect, int[] materials){
        super(textureRegion, rect);

        this.picName = picName;
        this.materials = materials;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public void colMove(float xSpeed, float ySpeed, float zSpeed, MyActor cr, boolean impulse){
        if(xSpeed != 0 || ySpeed != 0 || zSpeed != 0) {
            float startX = getX();
            float startY = getY();
            float startZ = getZ();

            if (xSpeed != 0) {
                setX(getX() + xSpeed);

                if (getRect().getX() < -100 * GameScreen.squareSize) {
                    setX(getX() - getRect().getX() - 100 * GameScreen.squareSize);
                } else if (getRect().getX() + getRect().getWidth() > 32 * (GameScreen.mapSize + 100)) {
                    setX(getX() - (getRect().getX() + getRect().getWidth() - 32 * (GameScreen.mapSize + 100)));
                }

                Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                if (cell != null && getZ() < cell.getZ(getCentralX(), getRectCenterY()) && Math.abs(getZ() - cell.getZ(getCentralX(), getRectCenterY())) >= getRect().getzSize() / 2) {
                    setX(startX);
                }
            }

            if (ySpeed != 0) {
                setY(getY() + ySpeed);

                if (getRect().getY() < -100 * GameScreen.squareSize) {
                    setY(getY() - getRect().getY() - 100 * GameScreen.squareSize);
                } else if (getRect().getY() + getRect().getHeight() > 32 * (GameScreen.mapSize + 100)) {
                    setY(getY() - (getRect().getY() + getRect().getHeight() - 32 * (GameScreen.mapSize + 100)));
                }

                Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                if (cell != null && getZ() < cell.getZ(getCentralX(), getRectCenterY()) && Math.abs(getZ() - cell.getZ(getCentralX(), getRectCenterY())) >= getRect().getzSize() / 2) {
                    setY(startY);
                }
            }

            if (zSpeed != 0) {
                setZ(getZ() + zSpeed);
                Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
                if (cell != null && getZ() < cell.getZ(getCentralX(), getRectCenterY())) {
                    setZ(startZ);
                }
            }

            Obstacle obs = (Obstacle) getOneIntersectingObject(Obstacle.class, ObsWithInventory.class);
            if (obs != null && obs.isStatic()) {
                float[] vector = getCollision(obs);
                setPosition(vector[0] + getX(), vector[1] + getY());
                setZ(vector[2] + getZ());

                if(obs.getSlimeCof() > 0 && vector[0] < vector[2] && vector[1] < vector[2]){
                    this.zSpeed *= -obs.getSlimeCof();
                }
            }

            if (impulse) {
                float[] vector = getCollision(cr);
                if(cr instanceof Creature)
                    ((Creature) cr).colMove(-vector[0], -vector[1], -vector[2], null, false);
            }
        }
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public String getPicName() {
        return picName;
    }

    public Obstacle(TextureRegion textureRegion, Rectangle3D rect){
        super(textureRegion, rect);
    }
    public Obstacle(Rectangle3D rect){
        super(rect);
    }

    public int[] getMaterials() {
        return materials;
    }

    public void setMaterials(int[] materials) {
        this.materials = materials;
    }

    public float getSlimeCof() {
        return slimeCof;
    }

    public void setSlimeCof(float slimeCof) {
        this.slimeCof = slimeCof;
    }

    public boolean overlapsPlayer() {
        return GameScreen.player.getX() < getX() + getTexture().getRegionWidth()
                && GameScreen.player.getX() + GameScreen.player.getTexture().getRegionWidth() > getX()
                && GameScreen.player.getY() + GameScreen.player.getZ() < getY() + getZ() + getTexture().getRegionHeight()
                && GameScreen.player.getY() + GameScreen.player.getZ() + GameScreen.player.getTexture().getRegionHeight() > getY() + getZ();
    }

    public float transparency = 1;

    private boolean withoutPhys = false;

    public void setWithoutPhys(boolean withoutPhys) {
        this.withoutPhys = withoutPhys;
    }
    private float lastZ;
    private float zSpeed;

    public void updateZ(){
        setVisible(true);
        Cell cell = GameScreen.getCellAt(getCentralX() - (getCentralX() < 0 ? 32 : 0), getRectCenterY() - (getCentralY() < 0 ? 32 : 0));
        if(isVisible() && cell != null && cell.getZ(getCentralX(), getRectCenterY()) != getZ() && !withoutPhys) {
            if(getStage() != null) {
                Array<Actor> actors = getStage().getActors();
                for (int i = actors.size - GameScreen.endEntities - 1; i > getZIndex(); i--) {
                    if (actors.get(i) instanceof MyActor &&
                            getRect().overlapsWZ(((MyActor) actors.get(i)).getRect()) &&
                            getZ() >= ((MyActor) actors.get(i)).getZ() + ((MyActor) actors.get(i)).getRect().getzSize()) {
                        setVisible(false);
                        ((MyActor) actors.get(i)).drawAfter.add(this);

                        break;
                    }
                }
            }

            zSpeed -= 0.4;
            int n = move(zSpeed);
            if (zSpeed < 0 && onGround() || n == 3) {
                zSpeed = 0;
            }
        }
    }

    public boolean onGround(){
        lastZ = getZ();
        move(-0.4f);
        boolean onGround = lastZ == getZ();
        setZ(lastZ);
        return onGround;
    }

    public void setzSpeed(float zSpeed) {
        this.zSpeed = zSpeed;
    }

    private int move(float zSpeed){
        int blockedCord = 0;
        if (zSpeed != 0) {
            setZ(getZ() + zSpeed);

            Obstacle obs = (Obstacle) getOneIntersectingObject(Obstacle.class);
            Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
            if (cell != null && getZ() < cell.getZ(getCentralX(), getRectCenterY())) {
                setZ(cell.getZ(getCentralX(), getRectCenterY()));

                blockedCord = 3;
            }
            else if(obs != null){
                float[] vector = getCollision(obs);
                setX(vector[0] + getX());
                setY(vector[1] + getY());
                setZ(vector[2] + getZ());
                blockedCord = 3;

                if(obs.getSlimeCof() > 0){
                    this.zSpeed *= -obs.getSlimeCof();
                    blockedCord = 0;
                }
            }
        }
        return blockedCord;
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    public float getTransparency(){
        return transparency;
    }
    @Override
    public void draw (Batch batch, float parentAlpha) {
        if (transparency > 0) {
            boolean delete = true;
            for (int i = 0; i < materials.length; i += 2) {
                if (materials[i] > 0) {
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
            if (cell != null)
                color.lerp(cell.getColor(), 1 - cell.getColor().r);

            batch.setColor(color.r, color.g, color.b, transparency);
            batch.draw(getTexture(), getX(), getY() + getZ(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        } else {
            if (this instanceof Mech) {
                GameScreen.mechs.remove(this);
            }
            remove();
        }

        if(drawAfter.size() > 0) {
            for (MyActor act : drawAfter) {
                act.setVisible(true);
                act.draw(batch, parentAlpha);
            }
            drawAfter.clear();
        }
    }

    public void physics(){
        Creature creature = (Creature) getOneIntersectingObject(Creature.class);
        if(creature != null) {
            float[] vector = getCollision(creature);
            creature.colMove(-vector[0] / 2 * (getMass() / creature.getMass()), -vector[1] / 2 * (getMass() / creature.getMass()), -vector[2] / 2 * (getMass() / creature.getMass()), this, true);
        }

        ObsWithInventory owi = (ObsWithInventory) getOneIntersectingObject(ObsWithInventory.class);
        if(owi != null) {
            float[] vector = getCollision(owi);
            owi.colMove(-vector[0] / 2 * (getMass() / owi.getMass()), -vector[1] / 2 * (getMass() / owi.getMass()), -vector[2] / 2 * (getMass() / owi.getMass()), this, true);
        }

        sort();
    }
}
