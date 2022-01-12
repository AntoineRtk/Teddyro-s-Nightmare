package osoroshi.teddyro.game.animations;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class DustMonster extends MapElement {
    
    private BufferedImage image;
    private int count = 0;
    
    public DustMonster(GameLevel gl, TileMap tileMap, BufferedImage image, double x, double y) {
        super(gl, tileMap, x, y, null);
        this.image = image;
    }
    
    public void update() {
        y -= 1 + new java.util.Random().nextDouble() * 2;
        count++;
        if(count >= 100) {
            y -= 2 + new java.util.Random().nextDouble() * 3;
        }
        if(shouldBeRemoved()) {
            tileMap.removeObject(this);
        }
    }
    
    public boolean shouldBeRemoved() {
        return y <= 0;
    }
    
    public void paint(Graphics2D g) {
        g.drawImage(image, (int) x - tileMap.getX(), (int) y - tileMap.getY(), null);
    }
}