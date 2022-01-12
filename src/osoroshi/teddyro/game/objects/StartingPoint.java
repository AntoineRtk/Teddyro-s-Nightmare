package osoroshi.teddyro.game.objects;

import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class StartingPoint extends MapElement {
    
    private int index = -1;
    
    public StartingPoint(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        if(params != null) {
            this.index = Integer.parseInt(params);
        }
    }
    
    public int getIndex() {
        return index;
    }
}