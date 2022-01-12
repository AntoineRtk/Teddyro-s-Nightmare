package osoroshi.teddyro.game.objects.camera;

import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class CameraControler extends MapElement {
    
    private int allignement;
    
    public CameraControler(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        if(params == null) return;
        width = Integer.parseInt(params.split("-")[0]);
        allignement = Integer.parseInt(params.split("-")[1]);
    }
    
    public void update() {
        int h = 0;
        for(;;) {
            h += 32;
            if(tileMap.isSolid(x, y + h)) break;
        }
        height = h;
        if(gl.getCameraXAdj() == 0 || gl.getCameraYOpp() == 0) {
            gl.setCameraTween(0.75);
        }
    }
    
    public void intersect(MapObject mo) {
        if(!(mo instanceof Hero)) return;
        gl.setCameraAllignement(allignement);
        gl.setCameraTween(0.1);
    }
}