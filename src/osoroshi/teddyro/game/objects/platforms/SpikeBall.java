
package osoroshi.teddyro.game.objects.platforms;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.objects.mobs.boss.Snado;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class SpikeBall extends MapElement {
    
    private int count = 0;
    
    public SpikeBall(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, null);
        animation.setFrames(new BufferedImage[]{GameResource.getSpikeBall()});
        width = 64;
        height = 64;
        fallingSpeed = 0.45;
        maxFallingSpeed = 12.8;
    }
    
    public void update() {
        if(count == 0) super.checkCollisions();
        rotation += 15.0 / 12.8 * dy;
        if(getOpacity() != 1) {
            count++;
            setOpacity(1f - 1.0f/30.0f * count);
        }
        if(getOpacity() <= 0f) {
            tileMap.removeObject(this);
        }
        if(dy == 0 && getOpacity() == 1.0) {
            ((Snado) tileMap.mobs[area].get(0)).hit();
            JukeBox.play("fallingspikeball");
            setOpacity(1f - 1.0f/30.0f);
            count = 1;
            tileMap.removePlatform(getPlatform());
        }
    }
}