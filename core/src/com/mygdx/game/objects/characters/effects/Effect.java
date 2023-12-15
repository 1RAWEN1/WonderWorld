package com.mygdx.game.objects.characters.effects;

import com.mygdx.game.objects.characters.Creature;

public interface Effect {
    void dealEffect(Creature cr, long millis);
}
