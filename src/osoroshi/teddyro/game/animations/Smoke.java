package osoroshi.teddyro.game.animations;

import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Smoke extends MapElement {
    
    private int alpha = 255;
    
    public Smoke(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        animation.setFrames(GameResource.getSmoke());
        animation.setDelay(3);
        width = 256;
        height = 256;
        JukeBox.play("smokeexplosion");
    }
    
    public void update() {
        super.update();
        if(alpha == 0) tileMap.removeObject(this);
        if(animation.hasPlayedOnce()) {
            alpha -= 5;
            if(alpha < 0) alpha = 0;
            setOpacity(1.0f / 255.0f * alpha);
        }
    }
}