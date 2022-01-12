package osoroshi.teddyro.game.objects.platforms;

import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class InvisiblePlatform extends Platform {
    public InvisiblePlatform(GameLevel gl, TileMap tileMap, double x, double y, int w, int h) {
        super(gl, tileMap, x, y, null);
        this.width = w;
        this.height = h;
    }
    
    public void update() {
        super.update();
    }
}