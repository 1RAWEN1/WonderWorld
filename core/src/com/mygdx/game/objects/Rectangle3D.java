package com.mygdx.game.objects;

import com.badlogic.gdx.math.Rectangle;

public class Rectangle3D {
    private boolean isDoor = false;
    private boolean isCircle = false;
    private Rectangle rect;
    private float z;
    private float zSize;

    private boolean material = true;

    public Rectangle3D(Rectangle rect, float z, float zSize) {
        this.rect = rect;
        this.z = z;
        this.zSize = zSize;
    }

    public boolean isDoor() {
        return isDoor;
    }

    public void setDoor(boolean door) {
        isDoor = door;
    }

    public void setMaterial(boolean material) {
        this.material = material;
    }

    public boolean isMaterial() {
        return material;
    }
    public void setCircle(boolean circle){
        isCircle = circle;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getzSize() {
        return zSize;
    }

    public void setzSize(float zSize) {
        this.zSize = zSize;
    }

    public void setPosition(float x, float y, float z){
        rect.setPosition(x, y);
        this.z = z;
    }

    public void setWidth(float width){
        rect.setWidth(width);
    }

    public void setHeight(float height){
        rect.setHeight(height);
    }
    public float getWidth(){
        return rect.getWidth();
    }
    public float getHeight(){
        return rect.getHeight();
    }

    public float getX(){
        return rect.getX();
    }

    public float getY(){
        return rect.getY();
    }

    public boolean overlaps(Rectangle3D r){
        if(material && r.isMaterial()) {
            if (!isCircle && !r.isCircle)
                return rect.overlaps(r.rect) && overlapsZ(r);
            else if (isCircle && r.isCircle)
                return overlapsCC(r) && overlapsZ(r);
            else if (!r.isCircle)
                return overlapsCR(r) && overlapsZ(r);
            else
                return r.overlapsCR(this) && overlapsZ(r);
        }
        else{
            return false;
        }
    }

    public boolean overlaps(float zCord, Rectangle3D r){
        if(material && r.isMaterial()) {
            if (!isCircle && !r.isCircle)
                return rect.overlaps(r.rect) && overlapsZ(zCord, r);
            else if (isCircle && r.isCircle)
                return overlapsCC(r) && overlapsZ(zCord, r);
            else if (!r.isCircle)
                return overlapsCR(r) && overlapsZ(zCord, r);
            else
                return r.overlapsCR(this) && overlapsZ(zCord, r);
        }
        else{
            return false;
        }
    }

    public boolean overlapsWZ(Rectangle3D r){
        if(material && r.isMaterial()) {
            if (!isCircle && !r.isCircle)
                return rect.overlaps(r.rect);
            else if (isCircle && r.isCircle)
                return overlapsCC(r);
            else if (!r.isCircle)
                return overlapsCR(r);
            else
                return r.overlapsCR(this);
        }
        else{
            return false;
        }
    }

    public boolean overlapsZ(Rectangle3D r){
        return this.z < r.z + r.getzSize() && this.z + this.getzSize() > r.z;
    }

    public boolean overlapsZ(float zCord, Rectangle3D r){
        return zCord < r.z + r.getzSize() && this.z + this.getzSize() > r.z;
    }

    private boolean overlapsCC(Rectangle3D r){
        return Math.sqrt(Math.pow(r.getCenterX() - getCenterX(), 2) + Math.pow(r.getCenterY() - getCenterY(), 2)) < getRadius(r.getCenterX(), r.getCenterY()) + r.getRadius(getCenterX(), getCenterY());
    }

    public double getRadius(float x, float y){
        float rot = getRotTo(x, y);
        return Math.sqrt(Math.pow(rect.getWidth() * Math.cos(Math.toRadians(rot)) / 2, 2) + Math.pow(rect.getHeight() * Math.sin(Math.toRadians(rot)) / 2, 2));
    }

    public float getRotTo(float x, float y){
        return (float) (180 * Math.atan2(y - getCenterY(), x - getCenterX()) / Math.PI);
    }

    private boolean overlapsCR(Rectangle3D r) {
        float circleDistanceX = Math.abs(getCenterX() - r.getCenterX());
        float circleDistanceY = Math.abs(getCenterY() - r.getCenterY());

        if (circleDistanceX >= (r.getWidth() / 2 + getWidth() / 2)) { return false; }
        if (circleDistanceY >= (r.getHeight() / 2 + getHeight() / 2)) { return false; }

        if (circleDistanceX < (r.getWidth() / 2 + getWidth() / 2) && circleDistanceY <= (r.getHeight() / 2)) {
            return true;
        }
        if (circleDistanceY < (r.getHeight() / 2 + getHeight() / 2) && circleDistanceX <= (r.getWidth() / 2)) {
            return true;
        }

        double cornerDistance = Math.pow(Math.max(0, circleDistanceX - r.getWidth() / 2), 2) +
                Math.pow(Math.max(circleDistanceY - r.getHeight() / 2, 0), 2);

        return (cornerDistance < Math.pow(getRadius(r.getCenterX(), r.getCenterY()), 2));
    }

    public boolean contains(float x, float y, float z){
        if(!isCircle)
            return rect.contains(x, y) && this.z <= z && this.z + this.getzSize() >= z;
        else
            return Math.sqrt(Math.pow(getCenterX() - x, 2) + Math.pow(getCenterY() - y, 2)) <= getRadius(x, y) && this.z <= z && this.z + this.getzSize() >= z;
    }

    public float getCenterX(){
        return rect.getX() + rect.getWidth() / 2;
    }

    public float getCenterY(){
        return rect.getY() + rect.getHeight() / 2;
    }

    public float getCenterZ(){
        return getZ() + getzSize() / 2;
    }

    public float[] getCollision(Rectangle3D r){
        if(!isCircle && !r.isCircle)
            return getCollisionVector(r);
        else if(isCircle && r.isCircle)
            return getCollisionCC(r);
        else if(!r.isCircle)
            return getCollisionCR(r, false);
        else {
            return r.getCollisionCR(this, true);
        }
    }

    public float[] getCollisionVector(Rectangle3D r){
        float colX1 = Math.abs((r.getX() + r.getWidth()) - getX());
        float colX2 = Math.abs(r.getX() - (getX() + getWidth()));

        float colX = Math.min(colX1, colX2);

        float colY1 = Math.abs((r.getY() + r.getHeight()) - getY());
        float colY2 = Math.abs(r.getY() - (getY() + getHeight()));

        float colY = Math.min(colY1, colY2);

        float[] vector = new float[3];
        if(Math.abs(colX) <= Math.abs(colY) &&
                Math.abs(colX) <= Math.abs(getZCollision(r)))
            vector[0] = getCenterX() < r.getCenterX() ? -colX : colX;
        else if(Math.abs(colY) <= Math.abs(getZCollision(r))) {
            vector[1] = getCenterY() < r.getCenterY() ? -colY : colY;
        }
        else
            vector[2] = getZCollision(r);

        return vector;
    }

    public float[] get2DCollisionVector(Rectangle3D r){
        float colX1 = Math.abs((r.getX() + r.getWidth()) - getX());
        float colX2 = Math.abs(r.getX() - (getX() + getWidth()));

        float colX = Math.min(colX1, colX2);

        float colY1 = Math.abs((r.getY() + r.getHeight()) - getY());
        float colY2 = Math.abs(r.getY() - (getY() + getHeight()));

        float colY = Math.min(colY1, colY2);

        float[] vector = new float[2];
        if(Math.abs(colX) <= Math.abs(colY))
            vector[0] = getCenterX() < r.getCenterX() ? -colX : colX;
        else {
            vector[1] = getCenterY() < r.getCenterY() ? -colY : colY;
        }

        return vector;
    }

    public float getZCollision(Rectangle3D r){
        float col1 = Math.abs((r.getZ() + r.getzSize()) - getZ());
        float col2 = Math.abs(r.getZ() - (getZ() + getzSize()));

        float col = Math.min(col1, col2);
        return getCenterZ() < r.getCenterZ() ? -col : col;
    }

    public float[] getCollisionCC(Rectangle3D r){
        double col = -Math.sqrt(Math.pow(r.getCenterX() - getCenterX(), 2) + Math.pow(r.getCenterY() - getCenterY(), 2)) + getRadius(r.getCenterX(), r.getCenterY()) + r.getRadius(getCenterX(), getCenterY());
        float rot = r.getRotTo(getCenterX(), getCenterY());
        float[] vector = new float[3];
        float zCol = getZCollision(r);

        if(col / (getRadius(r.getCenterX(), r.getCenterY()) + r.getRadius(getCenterX(), getCenterY())) < Math.abs(zCol) / getzSize()){
            vector[0] = (float) (col * Math.cos(Math.toRadians(rot)));
            vector[1] = (float) (col * Math.sin(Math.toRadians(rot)));
        }
        else{
            vector[2] = getZCollision(r);
        }

        return vector;
    }

    public float[] getCollisionCR(Rectangle3D r, boolean back){
        float circleDistanceX = Math.abs(getCenterX() - r.getCenterX());
        float circleDistanceY = Math.abs(getCenterY() - r.getCenterY());

        if (circleDistanceX < (r.getWidth() / 2 + getWidth() / 2) && circleDistanceY <= (r.getHeight() / 2) ||
                circleDistanceY < (r.getHeight() / 2 + getHeight() / 2) && circleDistanceX <= (r.getWidth() / 2)){
            if(back)
                return r.getCollisionVector(this);
            else
                return getCollisionVector(r);
        }

        if(back)
            return r.getCornerCollision(this);
        else
            return getCornerCollision(r);
    }

    public float[] getCornerCollision(Rectangle3D r){
        float cornerX;
        float cornerY;
        if(getX() < r.getX()){
            cornerX = r.getX();
        }
        else{
            cornerX = r.getX() + r.getWidth();
        }

        if(getY() < r.getY()){
            cornerY = r.getY();
        }
        else{
            cornerY = r.getY() + r.getHeight();
        }

        return getCornerCollision(cornerX, cornerY, r);
    }

    public float[] getCornerCollision(float x, float y, Rectangle3D r){
        double col = -Math.sqrt(Math.pow(x - getCenterX(), 2) + Math.pow(y - getCenterY(), 2)) + getRadius(x, y) + 0.5;
        float rot = getOppositeRot(getRotTo(x, y));
        float[] vector = new float[3];
        float zCol = getZCollision(r);

        if(col / (getRadius(x, y) + 0.5) < Math.abs(zCol) / getzSize()){
            vector[0] = (float) (col * Math.cos(Math.toRadians(rot)));
            vector[1] = (float) (col * Math.sin(Math.toRadians(rot)));
        }
        else{
            vector[2] = getZCollision(r);
        }

        return vector;
    }

    public float getOppositeRot(float rot){
        return (rot + 180) % 360;
    }
}
