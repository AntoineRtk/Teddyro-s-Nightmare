package osoroshi.teddyro.game.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class BlackScreen extends MapElement {
    
    private int count = -1;
    
    public BlackScreen(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, null);
        width = 32;
        height = 32;
        gl.addToPaintAtEnd(this);
    }
    
    public void update() {
        if(count != -1) count++;
        if(count == 61) {
            gl.setArea(tileMap.getArea() + 1, 0);
        }
    }
    
    public void paint(Graphics2D g) {
        if(count != -1) {
            g.setColor(Color.black);
            g.setColor(new Color(0, 0, 0, (int) (255.0 / 60.0 * count)));
            g.fillRect(0, 0, gl.getWidth(), gl.getHeight());
        }
    }
    
    public void intersect(MapObject mo) {
        if(mo instanceof Hero) { 
            count = 0;
        }
    }
}