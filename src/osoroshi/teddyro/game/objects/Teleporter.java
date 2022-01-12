package osoroshi.teddyro.game.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class Teleporter extends MapElement {
    
    private double tpx = -1, tpy = -1;
    private int count = 0;
    private boolean hasIntersected = false;
    
    public Teleporter(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        if(params != null) {
            tpx = Integer.parseInt(params.split("-")[0]);
            tpy = Integer.parseInt(params.split("-")[1]);
        }
        width = 64;
        height = 64;
        gl.addToPaintAtEnd(this);
    }
    
    public void update() {
        if(hasIntersected) count++;
        if(count > 30) {
            tileMap.hero.setX(tpx);
            for(;;) {
                if(tileMap.isSolid(tpx, tpy)) {
                    tpy = (int) ((tpy) / 32.0) * 32;
                    break;
                }
                else {
                    tpy += 32;
                }
            }
            tileMap.hero.setY((int) tpy - tileMap.hero.height);
            count = 0;
            hasIntersected = false;
        }
    }
    
    public void intersect(MapObject mo) {
        if(tpx == -1 && tpy == -1 && !(mo instanceof Hero)) return;
        hasIntersected  = true;
    }
    
    public void paint(Graphics2D g) {
        if(count != 0) {
            g.setColor(new Color(0, 0, 0, (int) (255.0 / 30.0 * count)));
            g.fillRect(0, 0, gl.getWidth(), gl.getHeight());
        }
    }
}