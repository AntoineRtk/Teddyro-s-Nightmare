package osoroshi.teddyro.game.objects;

import java.awt.Rectangle;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public abstract class MapElement extends MapObject {
    
    public GameLevel gl;
    
    public MapElement(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(tileMap, x, y);
        this.gl = gl;
    }
    
    public void intersect(MapObject mo) {}
    
    public boolean onScreen() {
        return new Rectangle((int) getX(), (int) getY(), width, height).intersects(tileMap.getX(), tileMap.getY(), gl.getWidth(), gl.getHeight());
    }
}