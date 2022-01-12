package osoroshi.teddyro.game.tilemap;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.MapObject;

public abstract class SpecialTile extends Tile {
    
    public SpecialTile(BufferedImage image, boolean solid) {
        super(image, solid);
    }
    
    public void update() {}
    
    public abstract void doAction(MapObject mo, boolean x, int dir);
}