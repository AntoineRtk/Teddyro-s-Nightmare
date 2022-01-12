package osoroshi.teddyro.game.tilemap;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.animations.Animation;

public class AnimatedSlopeTile extends Slope {
    
    private Animation animation;
    
    public AnimatedSlopeTile(Animation animation, int startY, int endY) {
        super(animation.getFrames()[0], startY, endY);
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
}