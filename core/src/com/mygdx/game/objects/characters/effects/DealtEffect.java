package com.mygdx.game.objects.characters.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.objects.characters.Creature;

import java.io.Serializable;

public class DealtEffect implements Serializable {
    private int type;
    private Effect effect;
    private long dealtTime;
    private int effectTime;

    public DealtEffect(long dealtTime, int type) {
        this.dealtTime = dealtTime;
        this.type = type;

        setEffect();
    }

    private Color color;
    public void setEffect(){
        effectTime = 1000;

        switch (type){
            case -2:
                effect = (c, m) ->
                        c.setAgility(c.getAgility() * 2f);

                break;
            case -1:
                effect = (c, m) ->
                        c.setSpeedBoost(c.getSpeedBoost() * 2f);

                break;
            case 1:
                effect = (c, m) -> {
                    if (m % 200 > 180) {
                        c.dealDamage(0.5f, 100, c);
                    }
                };
                effectTime = 800;

                break;
            case 2:
                    effect = (c, m) ->
                            c.setSpeedBoost(c.getSpeedBoost() * 0.5f);

                break;
            case 3:
                effect = (c, m) -> {
                    if(m % 200 > 180){
                        c.setLastTimeDealStan(TimeUtils.millis());
                        c.setStanTime(50, false);
                    }
                };
                effectTime = 400;

                break;
            case -3:
                effect = (c, m) -> {
                    if(m % 1000 > 980){
                        c.dealDamage(-0.2f, 0, c);
                    }
                };
                effectTime = 5000;

                break;
            case -4:
                effect = (c, m) -> {
                    if(m % 200 > 180){
                        c.setLastTimeDealDamage(c.getLastTimeDealDamage() - (long)(0.2f * (m % 200)));
                        if(!c.isDealDamage() && TimeUtils.timeSinceMillis(c.getLastTimeDealDamage()) > (c.getCooldown() / c.getAttackSpeedBoost())){
                            c.setLastTimeDealDamage(TimeUtils.millis() - (long)((c.getCooldown() / c.getAttackSpeedBoost())) + 100);
                        }
                    }
                };

                break;
            case -5:
                effect = (c, m) ->
                        c.setCritCof(1.2f);

                break;
        }

        if(type == 1){
            color = new Color(245f / 256, 140f / 256, 31f / 256, 1);
        }
        //color = new Color(99f / 256, 205f / 256, 246f / 256, 1);
        else if(type == -3 || type == -4){
            color = new Color(84f / 256, 185f / 256, 71f / 256, 1);
        }
        else if(type == -5 || type == 3){
            color = new Color(245f / 256, 185f / 256, 21f / 256, 1);
        }
        else {
            color = new Color(220f / 256, 220f / 256, 220f / 256, 1);
        }
    }

    public Color getColor() {
        return color;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public long getDealtTime() {
        return dealtTime;
    }

    public void setDealtTime(long dealtTime) {
        this.dealtTime = dealtTime;
    }

    public int getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(int effectTime) {
        this.effectTime = effectTime;
    }

    public void effect(Creature cr, long millis){
        effect.dealEffect(cr, millis);
    }

    public int getType() {
        return type;
    }
}
