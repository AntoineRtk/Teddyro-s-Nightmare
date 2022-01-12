package osoroshi.teddyro.game.tilemap;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.objects.MapObject;

public class Ladder extends SpecialTile {
    
    private boolean used = false;
    
    public Ladder(BufferedImage image) {
        super(image, false);
    }
    
    public void update() {
        used = false;
    }
    
    public void doAction(MapObject mo, boolean x, int dir) {
        if(!(mo instanceof Hero) || used) return;
        used = true;
        if(mo.getDirY() == -1) {
            mo.dy = -6.4;
        }
        else if(mo.getDirY() == 1) {
            mo.dy = 6.4;
        }
    }
}