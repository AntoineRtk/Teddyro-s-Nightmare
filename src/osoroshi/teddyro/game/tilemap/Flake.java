
package osoroshi.teddyro.game.tilemap;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import osoroshi.teddyro.game.utils.GameResource;

public class Flake {
    
    private TileMap tileMap;
    private double x, y = -100, rotation = new java.util.Random().nextInt(360);
    
    public Flake(TileMap tileMap, double x) {
        this.tileMap = tileMap;
        this.x = x;
    }
    
    public void update() {
        rotation += Math.random() * 5.0;
        y += 6.4;
    }
    
    public void paint(Graphics2D g) {
        AffineTransform af = g.getTransform();
        g.setTransform(AffineTransform.getRotateInstance(rotation * Math.PI / 180.0, (int) x - tileMap.getX() + 15, (int) y - tileMap.getY() + 15));
        g.drawImage(GameResource.getFlake(), (int) x - tileMap.getX(), (int) y - tileMap.getY(), null);
        g.setTransform(af);
    }
    
    public double getY() {
        return y;
    }
}