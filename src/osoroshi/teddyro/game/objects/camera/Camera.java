package osoroshi.teddyro.game.objects.camera;

import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class Camera {
    
    private GameLevel gl;
    private TileMap tileMap;
    private MapObject mo;
    private double x, y, tween = 0.75, adj, opp;
    private int allignement = 1;
    public static final int LEFT = 0, MID = 1, RIGHT = 2;
    
    public Camera(GameLevel gl, TileMap tileMap) {
        this.gl = gl;
        this.tileMap = tileMap;
    }
    
    public void update() {
        if(mo == null) return;
        double px = 0;
        switch(allignement) {
            case 0:
                px = gl.getWidth() / 4.0;
                break;
            case 1:
                px = gl.getWidth() / 2.0;
                break;
            case 2:
                px = gl.getWidth() / 4.0 * 3.0;
                break;
        }
        adj = (mo.getX() + mo.width / 2.0 - px) - x; 
        opp = (mo.getY() + mo.height / 2.0 - gl.getHeight() / 2.0) - y;
        x += adj * tween;
        y += opp * tween;
        tileMap.setPosition(x, y);
    }
    
    public void setTween(double tween) {
        this.tween = tween;
    }
    
    public void setMapObject(MapObject mo) {
        this.mo = mo;
    }
    
    public void setAllignement(int allignement) {
        this.allignement = allignement;
    }
    
    public int getXAdj() {
        return (int) adj;
    }
    
    public int getYOpp() {
        return (int) opp;
    }
    
    public double getTween() {
        return tween;
    }
    
    public MapObject getObject() {
        return mo;
    }

    public void center() {
        x = mo.getX() + mo.width / 2.0 - gl.getWidth() / 2.0;
        y = mo.getY() + mo.height / 2.0 - gl.getHeight() / 2.0;
        tileMap.setPosition(x, y);
    }
    
    public void goX(int i) {
        x += 6 * i;
    }
    
    public void goY(int i) {
        y += 6 * i;
    }
}