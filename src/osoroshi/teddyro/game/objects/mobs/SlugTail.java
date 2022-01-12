package osoroshi.teddyro.game.objects.mobs;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.tilemap.TileMap;

public class SlugTail extends MapObject {
    
    public SlugTail(TileMap tileMap, double x, double y, BufferedImage image) {
        super(tileMap, x, y);
        animation.setFrames(new BufferedImage[]{image});
    }
}