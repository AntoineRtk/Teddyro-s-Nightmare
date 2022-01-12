package osoroshi.teddyro.game.tilemap;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.animations.Animation;
import osoroshi.teddyro.game.objects.MapObject;

public class AnimatedTile extends SpecialTile {
    
    private Animation animation;
    
    public AnimatedTile(boolean solid, Animation animation) {
        super(null, solid);
        this.animation = animation;
    }
    
    public void update() {
        if(animation == null) return;
        animation.update();
    }
    
    public BufferedImage getImage() {
        if(animation == null) return null;
        return animation.getImage();
    }
    
    public void doAction(MapObject mo, boolean x, int dir) {}
}