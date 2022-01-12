package osoroshi.teddyro.game.objects;

import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class SnowGenerator extends MapElement {
    
    public SnowGenerator(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, null);
    }
    
    public void update() {
        tileMap.setSnowing(true);
    }
}