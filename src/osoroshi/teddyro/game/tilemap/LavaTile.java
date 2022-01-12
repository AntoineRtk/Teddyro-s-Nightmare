package osoroshi.teddyro.game.tilemap;

import osoroshi.teddyro.game.animations.Animation;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.objects.mobs.Monster;

public class LavaTile extends AnimatedTile {
    
    public LavaTile(Animation animation) {
        super(false, animation);
    }
    
    public void doAction(MapObject mo, boolean x, int dir) {
        if(mo instanceof Hero) {
            ((Hero) mo).kill();
        }
        if(mo instanceof Monster) {
            ((Monster) mo).hit(((Monster) mo).getLife()+((Monster) mo).getDef());
        }
    }
}