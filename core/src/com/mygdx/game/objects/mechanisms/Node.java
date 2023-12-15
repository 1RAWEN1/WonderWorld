package com.mygdx.game.objects.mechanisms;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.objects.Rectangle3D;
import com.mygdx.game.scenes.GameScreen;

import java.util.ArrayList;

public class Node extends Mech {
    private final ArrayList<Mech> connections = new ArrayList<>();
    public Node(){
        super(GameScreen.loadRegion("decor\\node.png"), new Rectangle3D(new Rectangle(0, 0, 32, 32), 0, 32),
                new int[]{5, 100, 1, 109});
    }

    public void check(){
        connections.clear();
        for(Mech mech : GameScreen.mechs){
            if(mech.getRect().getRect().overlaps(getRect().getRect()) ||
                    getRect().getX() < mech.getRect().getX() + mech.getRect().getWidth() && getRect().getX() + getRect().getWidth() > mech.getRect().getX() && getRect().overlapsZ(mech.getRect()) ||
                    getRect().getY() < mech.getRect().getY() + mech.getRect().getHeight() && getRect().getY() + getRect().getHeight() > mech.getRect().getY() && getRect().overlapsZ(mech.getRect())){
                connections.add(mech);
            }
        }
    }

    private boolean start = true;
    @Override
    public void act(float delta){
        if(start){
            check();
            start = false;
        }
        System.out.println(connections.size());
        for(Mech mech : connections){
            if(power > mech.power)
                mech.power = power;
        }
    }
}
