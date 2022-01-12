package osoroshi.teddyro.game.tilemap;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.objects.mobs.Monster;

public class SpikeTile extends SpecialTile {
    
    public SpikeTile(BufferedImage image, boolean solid) {
        super(image, solid);
    }
    
    public void doAction(MapObject mo, boolean x, int dir) {
        if(mo.dy < 0) return;
        if(mo instanceof Hero) {
            ((Hero) mo).hit(3);
        }
        if(mo instanceof Monster) {
            ((Monster) mo).hit(3);
        }
    }
}