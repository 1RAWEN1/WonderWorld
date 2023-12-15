package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.objects.characters.Player;
import com.mygdx.game.objects.characters.Projectile;
import com.mygdx.game.objects.decor.*;
import com.mygdx.game.objects.inventory.FallenItem;
import com.mygdx.game.scenes.GameScreen;

import java.util.ArrayList;

public class MyActor extends Actor {
    private float z = 0;
    private TextureRegion texture;
    private Rectangle3D rect;

    public MyActor(TextureRegion textureRegion, Rectangle3D rect){
        texture = textureRegion;
        setBounds(0, 0,
                texture.getRegionWidth(), texture.getRegionHeight());

        setBoundsForTexture(getTexture().getRegionWidth(), getTexture().getRegionHeight());

        this.rect = rect;
    }

    public MyActor(TextureRegion textureRegion){
        texture = textureRegion;
        setBounds(0, 0,
                texture.getRegionWidth(), texture.getRegionHeight());
    }

    public MyActor(Rectangle3D rect){
        this.rect = rect;
    }

    private int picWidth;
    private int picHeight;

    public void setBoundsForTexture(int width, int height){
        picWidth = width;
        picHeight = height;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    @Override
    public void setX(float x) {
        super.setX(x + (getScaleX() * getPicWidth() / 2));
        updateRectPos();
    }

    public void setCentralX(float x){
        super.setX(x);
        updateRectPos();
    }

    public void setCentralY(float y){
        super.setY(y);
        updateRectPos();
    }

    public void setRectCentralY(float y){
        setY(y - getRectDelta());
    }

    @Override
    public void setY(float y) {
        super.setY(y + (getScaleY() * getPicHeight() / 2));
        updateRectPos();
    }

    @Override
    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    @Override
    public float getX() {
        return super.getX() - (getScaleX() * getPicWidth() / 2);
    }

    @Override
    public float getY() {
        return super.getY() - (getScaleY() * getPicHeight() / 2);
    }

    public float[] getCollision(MyActor actor){
        return getRect().getCollision(actor.getRect());
    }

    public float getZCollision(MyActor actor){
        return getRect().getZCollision(actor.getRect());
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
        updateRectPos();
    }

    private int rectDelta = 0;
    private int rectDeltaZ = 0;
    public Cell myCell;
    public void updateRectPos(){
        if(rect != null)
            rect.setPosition(getCentralX() - rect.getWidth() / 2, getY() + rectDelta - (rect.getHeight() / 2), getZ() + rectDeltaZ);

        Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
        if(cell != null && myCell != cell){
            if(myCell != null){
                myCell.actors.remove(this);
            }

            myCell = cell;

            myCell.actors.add(this);
        }
    }

    public int getRectDelta() {
        return rectDelta;
    }

    public void setRectDeltaZ(int rectDeltaZ) {
        this.rectDeltaZ = rectDeltaZ;
    }

    public void setRectDelta(int rectDelta){
        this.rectDelta = rectDelta;
    }

    public float getCentralX(){
        return super.getX();
    }

    public float getCentralY(){
        return super.getY();
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;

        setBoundsForTexture(getTexture().getRegionWidth(), getTexture().getRegionHeight());

        setBounds(getCentralX(), getCentralY(),
                texture.getRegionWidth(), texture.getRegionHeight());
    }

    public Rectangle3D getRect() {
        return rect;
    }

    public void setRect(Rectangle3D rect) {
        this.rect = rect;
        updateRectPos();
    }

    public void sort(){
        Array<Actor> actors = getStage().getActors();
        int index = getZIndex();
        while (index - 1 >= 0 && !(actors.get(index - 1) instanceof MyActor) &&
                !(actors.get(index - 1) instanceof Cell)) {
            actors.get(index - 1).toFront();
            index--;
        }

        boolean hasChange = false;

        if(index + 1 < actors.size - GameScreen.endEntities) {
            float height = getY();
            Actor act2 = actors.get(index + 1);
            float actorHeight = act2.getY();
            if(act2 instanceof Cell) {
                actorHeight += 27;
            }
            /*else if(act2 instanceof CaveWall && ((CaveWall) act2).getHeight1() > 0){
                actorHeight += 26f;
            }*/
            /*if(act2 instanceof Cell) {
                if (getZ() >= ((Cell) act2).getNormalZ())
                    actorHeight = height + 1;
            }
            else if(Math.abs(getRect().getY() - ((MyActor) act2).getRect().getY()) <= getRect().getHeight() + ((MyActor) act2).getRect().getHeight()
            && Math.abs(getRect().getX() - ((MyActor) act2).getRect().getX()) <= getRect().getWidth() + ((MyActor) act2).getRect().getWidth()) {
                if (getZ() >= ((MyActor) act2).getZ() + ((MyActor) act2).getRect().getzSize())
                    actorHeight = height + 1;
                else if (getZ() + getRect().getzSize() <= ((MyActor) act2).getZ())
                    actorHeight = height - 1;
            }*/
            int index1 = index;

            while (actorHeight > height){
                index1 ++;
                hasChange = true;

                if(index1 + 1 < actors.size - GameScreen.endEntities) {
                    act2 = actors.get(index1 + 1);
                    actorHeight = act2.getY();
                    if(act2 instanceof Cell) {
                        actorHeight += 27;
                    }
                    /*if(act2 instanceof Cell) {
                        if (getZ() >= ((Cell) act2).getNormalZ())
                            actorHeight = height + 1;
                    }
                    else if(Math.abs(getRect().getY() - ((MyActor) act2).getRect().getY()) <= getRect().getHeight() + ((MyActor) act2).getRect().getHeight()
                            && Math.abs(getRect().getX() - ((MyActor) act2).getRect().getX()) <= getRect().getWidth() + ((MyActor) act2).getRect().getWidth()) {
                        if (getZ() >= ((MyActor) act2).getZ() + ((MyActor) act2).getRect().getzSize())
                            actorHeight = height + 1;
                        else if (getZ() + getRect().getzSize() <= ((MyActor) act2).getZ())
                            actorHeight = height - 1;
                    }*/
                }
                else{
                    break;
                }
            }

            if(hasChange) {
                setZIndex(act2.getZIndex() - 1);
            }
        }
        if(!hasChange && index - 1 >= 0) {
            float height = getY();
            Actor act2 = actors.get(index - 1);
            float actorHeight = act2.getY();
            if(act2 instanceof Cell) {
                actorHeight += 27;
            }
            /*if(act2 instanceof Cell) {
                if (getZ() >= ((Cell) act2).getNormalZ())
                    actorHeight = height + 1;
            }
            else if(Math.abs(getRect().getY() - ((MyActor) act2).getRect().getY()) <= getRect().getHeight() + ((MyActor) act2).getRect().getHeight()
                    && Math.abs(getRect().getX() - ((MyActor) act2).getRect().getX()) <= getRect().getWidth() + ((MyActor) act2).getRect().getWidth()) {
                if (getZ() >= ((MyActor) act2).getZ() + ((MyActor) act2).getRect().getzSize())
                    actorHeight = height + 1;
                else if (getZ() + getRect().getzSize() <= ((MyActor) act2).getZ())
                    actorHeight = height - 1;
            }*/
            int index2 = index;

            boolean toBack = false;
            while (actorHeight < height){
                hasChange = true;
                index2 --;

                if(index2 - 1 >= 0) {
                    act2 = actors.get(index2 - 1);
                    actorHeight = act2.getY();
                    if(act2 instanceof Cell) {
                        actorHeight += 27;
                    }
                    /*if(act2 instanceof Cell) {
                        if (getZ() >= ((Cell) act2).getNormalZ())
                            actorHeight = height + 1;
                    }
                    else if(Math.abs(getRect().getY() - ((MyActor) act2).getRect().getY()) <= getRect().getHeight() + ((MyActor) act2).getRect().getHeight()
                            && Math.abs(getRect().getX() - ((MyActor) act2).getRect().getX()) <= getRect().getWidth() + ((MyActor) act2).getRect().getWidth()) {
                        if (getZ() >= ((MyActor) act2).getZ() + ((MyActor) act2).getRect().getzSize())
                            actorHeight = height + 1;
                        else if (getZ() + getRect().getzSize() <= ((MyActor) act2).getZ())
                            actorHeight = height - 1;
                    }*/
                }
                else{
                    toBack = true;
                    break;
                }
            }

            if(hasChange) {
                if (toBack)
                    toBack();
                else
                    setZIndex(act2.getZIndex() + 1);
            }
        }

        /*if(!hasChange) {
            while (!(actors.get(getZIndex() - 1) instanceof MyActor) &&
                    !(actors.get(getZIndex() - 1) instanceof Cell)) {
                actors.get(getZIndex() - 1).toFront();
            }
        }*/
    }

    public float getRectCenterX(){
        return getRect().getCenterX();
    }

    public float getRectCenterY(){
        return getRect().getCenterY();
    }

    public float getRotTo(MyActor act){
        return (float) (180 * Math.atan2(act.getRectCenterY() - getRectCenterY(), act.getRectCenterX() - getRectCenterX()) / Math.PI);
    }

    public float getRotTo(float x, float y){
        return (float) (180 * Math.atan2(y - getRectCenterY(), x - getRectCenterX()) / Math.PI);
    }

    public float getRotByDelta(float x, float y){
        return (float) (180 * Math.atan2(y, x) / Math.PI);
    }

    public float getRotDelta(float rot1, float rot2){
        float v = Math.abs(rot1 - rot2);
        return Math.abs(rot1 - rot2) <= 180 ? v : (360 - v);
    }

    public float getRotDeltaWithoutAbs(float rot1, float rot2){
        float v = rot2 - rot1;
        return Math.abs(v) <= 180 ? v : -(360 - Math.abs(v));
    }

    public int getRectDeltaZ() {
        return rectDeltaZ;
    }

    public float getOppositeRot(float rot){
        return (rot + 180) % 360;
    }

    public boolean isTouching(Class<?> c){
        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                        ((MyActor) getStage().getActors().get(i)).getRect().overlaps(getRect())){
                return true;
            }
        }
        return false;
    }

    public MyActor getOneIntersectingObject(Class<?> c){
        for (int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i++) {
            if (c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    ((MyActor) getStage().getActors().get(i)).getRect().overlaps(getRect())) {
                return (MyActor) getStage().getActors().get(i);
            }
        }
        return null;
    }

    public MyActor getOneIntersectingObject(Class<?> c, Class<?> c1){
        for (int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i++) {
            if (c.isInstance(getStage().getActors().get(i)) && !c1.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    ((MyActor) getStage().getActors().get(i)).getRect().overlaps(getRect())) {
                return (MyActor) getStage().getActors().get(i);
            }
        }
        return null;
    }

    public MyActor getOneIntersectingObject(Class<?> c, MyActor act){
        for (int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i++) {
            if (c.isInstance(getStage().getActors().get(i)) && act != getStage().getActors().get(i) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    ((MyActor) getStage().getActors().get(i)).getRect().overlaps(getRect())) {
                return (MyActor) getStage().getActors().get(i);
            }
        }
        return null;
    }

    public MyActor getOneIntersectingObject(boolean canOpenDoor, Class<?> c){
        for (int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i++) {
            if (c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    (!canOpenDoor && ((MyActor) getStage().getActors().get(i)).getRect().isDoor() ? ((MyActor) getStage().getActors().get(i)).getRect().overlaps(getZ(), getRect()) :
                            ((MyActor) getStage().getActors().get(i)).getRect().overlaps(getRect()))) {
                return (MyActor) getStage().getActors().get(i);
            }
        }
        return null;
    }

    public ArrayList<MyActor> getIntersectingObjects(Class<?> c){
        ArrayList<MyActor> creatures = new ArrayList<>();
        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    ((MyActor) getStage().getActors().get(i)).getRect().overlaps(getRect())){
                creatures.add((MyActor) getStage().getActors().get(i));
            }
        }
        return creatures;
    }

    public MyActor getOneObjectAtCords(float x, float y, float z, Class<?> c){
        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    ((MyActor) getStage().getActors().get(i)).getRect().contains(x, y, z)){
                return (MyActor) getStage().getActors().get(i);
            }
        }
        return null;
    }

    public MyActor getObjectInRange(float radius, Class<?> c){
        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    Math.pow(((MyActor) getStage().getActors().get(i)).getCentralX() - getCentralX(), 2) +
                    Math.pow(getStage().getActors().get(i).getY() - getY(), 2) <= Math.pow(radius, 2)){
                return (MyActor) getStage().getActors().get(i);
            }
        }
        return null;
    }

    public MyActor getObjectInRange(float radius, Class<?> c, MyActor act){
        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor && getStage().getActors().get(i) != act &&
                    Math.pow(((MyActor) getStage().getActors().get(i)).getCentralX() - getCentralX(), 2) +
                            Math.pow(getStage().getActors().get(i).getY() - getY(), 2) <= Math.pow(radius, 2)){
                return (MyActor) getStage().getActors().get(i);
            }
        }
        return null;
    }

    public MyActor getNearestObjectInRange(float radius, Class<?> c){
        double distance = 0;
        MyActor cr = null;

        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    Math.pow(((MyActor) getStage().getActors().get(i)).getCentralX() - getCentralX(), 2) +
                            Math.pow(getStage().getActors().get(i).getY() - getY(), 2) <= Math.pow(radius, 2)){
                if(distance > getDist((MyActor) getStage().getActors().get(i)) || cr == null) {
                    cr = (MyActor) getStage().getActors().get(i);
                    distance = getDist((MyActor) getStage().getActors().get(i));
                }
            }
        }
        return cr;
    }

    public MyActor getNearestObjectInRange(float radius, Class<?> c, MyActor act){
        double distance = 0;
        MyActor cr = null;

        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    Math.pow(((MyActor) getStage().getActors().get(i)).getCentralX() - getCentralX(), 2) +
                            Math.pow(getStage().getActors().get(i).getY() - getY(), 2) <= Math.pow(radius, 2)){
                if((distance > getDist((MyActor) getStage().getActors().get(i)) || cr == null) && act != getStage().getActors().get(i)) {
                    cr = (MyActor) getStage().getActors().get(i);
                    distance = getDist((MyActor) getStage().getActors().get(i));
                }
            }
        }
        return cr;
    }

    public MyActor getNearestObject(Class<?> c){
        double distance = 0;
        MyActor cr = null;
        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    overlapsTexture((MyActor) getStage().getActors().get(i))){
                if(distance > getDist((MyActor) getStage().getActors().get(i)) || cr == null) {
                    cr = (MyActor) getStage().getActors().get(i);
                    distance = getDist((MyActor) getStage().getActors().get(i));
                }
            }
        }
        return cr;
    }

    public double getDist(MyActor actor){
        return Math.sqrt(Math.pow(getCentralX() - actor.getCentralX(), 2) + Math.pow(getRectCenterY() - actor.getRectCenterY(), 2) + Math.pow(getZ() - actor.getZ(), 2));
    }

    public double getDist(float x, float y){
        return Math.sqrt(Math.pow(getCentralX() - x, 2) + Math.pow(getRectCenterY() - y, 2));
    }

    public ArrayList<MyActor> getObjectsOverlapsTexture(Class<?> c) {
        ArrayList<MyActor> creatures = new ArrayList<>();
        for(int i = 0; i < getStage().getActors().size - GameScreen.endEntities; i ++){
            if(c.isInstance(getStage().getActors().get(i)) && getStage().getActors().get(i) != this &&
                    getStage().getActors().get(i) instanceof MyActor &&
                    overlapsTexture((MyActor) getStage().getActors().get(i))){
                creatures.add((MyActor) getStage().getActors().get(i));
            }
        }
        return creatures;
    }

    public boolean overlapsTexture(MyActor cr){
        return cr.getX() <= getX() + getTexture().getRegionWidth()
                && cr.getX() + cr.getTexture().getRegionWidth() >= getX()
                && cr.getY() + cr.getZ() <= getY() + getZ() + getTexture().getRegionHeight()
                && cr.getY() + cr.getZ() + cr.getTexture().getRegionHeight() >= getY() + getZ();
    }

    public boolean overlapsTextureY(MyActor cr){
        return cr.getY() + cr.getZ() <= getY() + getZ() + getTexture().getRegionHeight()
                && cr.getY() + cr.getZ() + cr.getTexture().getRegionHeight() >= getY() + getZ();
    }

    public final ArrayList<MyActor> drawAfter = new ArrayList<>();
    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY() + getZ(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public boolean remove() {
        Cell cell = GameScreen.getCellAt(getCentralX(), getRectCenterY());
        if(cell != null){
            cell.actors.remove(this);
        }
        return super.remove();
    }

    public boolean remove1() {
        return super.remove();
    }
}
