package com.mygdx.game.objects.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.objects.Rectangle3D;

import java.util.ArrayList;

public class Talker extends Creature{
    public int phraseNum = 0;
    public final ArrayList<String> phrases = new ArrayList<>();

    public Talker(TextureRegion textureRegion, Rectangle3D rect) {
        super(textureRegion, rect);
    }

    public Talker(Rectangle3D rect) {
        super(rect);
    }

    public String getPhrase(){
        return "";
    }

    public void updatePhraseNum(){}

    public void setPhraseNum(int phraseNum) {
        this.phraseNum = phraseNum;
    }
}
