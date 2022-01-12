package osoroshi.teddyro.game.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import osoroshi.teddyro.game.tilemap.TileMap;

public class JumpDust {
    
    private TileMap tileMap;
    private double bx, x, by, y, dirx, diry;
    private int count = -1;
    
    public JumpDust(TileMap tileMap, double x, double y) {
        this.tileMap = tileMap;
        this.x = bx = x;
        this.y = by = y;
        this.dirx = (tileMap.hero.getX() + tileMap.hero.width / 2.0 - x) / 24.0;
        this.diry = (tileMap.hero.getY() + tileMap.hero.height / 2.0 - y) / 24.0;
    }
    
    public void update() {
        x = bx + dirx * count;
        y = by + diry * count;
        count++;
    }
    
    public void paint(Graphics2D g) {
        g.setColor(Color.yellow);
        if(new java.util.Random().nextBoolean()) g.setColor(Color.white); 
        //g.fillOval((int) x - tileMap.getX(), (int) y - tileMap.getY(), 1, 1);
        g.fillRect((int) x - tileMap.getX() + 2, (int) y - tileMap.getY(), 1, 5);
        g.fillRect((int) x - tileMap.getX(), (int) y - tileMap.getY() + 2, 5, 1);
    }
    
    public boolean shouldBeRemoved() {
        return count == 25;
    }
}