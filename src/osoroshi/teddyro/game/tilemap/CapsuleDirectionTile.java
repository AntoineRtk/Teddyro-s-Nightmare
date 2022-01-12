package osoroshi.teddyro.game.tilemap;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.Capsule;
import osoroshi.teddyro.game.objects.MapObject;

public class CapsuleDirectionTile extends SpecialTile {
    
    private int dir;
    
    public CapsuleDirectionTile(BufferedImage image, int dir) {
        super(image, false);
        this.dir = dir;
    }
    
    public void doAction(MapObject mo, boolean x, int dir) {
        if(mo instanceof Capsule) {
            ((Capsule) mo).changeDirection(this.dir);
        }
    }
}