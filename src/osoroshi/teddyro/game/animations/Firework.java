package osoroshi.teddyro.game.animations;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class Firework {
    
    private TileMap tileMap;
    private double x, y, toY;
    private BufferedImage image;
    private int width, height;
    private float opacity = 1;
    
    public Firework(TileMap tileMap, double x, double y) {
        this.tileMap = tileMap;
        this.x = x;
        this.y = y;
        image = GameResource.getFirework(new java.util.Random().nextInt(7));
        width = 5;
        height = 5;
        toY = this.tileMap.getY() + 30 + new java.util.Random().nextDouble() * 100;
    }
    
    public void update() {
        if(y <= toY) {
            double value = new java.util.Random().nextDouble() * 7;
            width += 5 + value;
            height += 5 + value;
        }
        else {
            y -= 8 + new java.util.Random().nextDouble() * 5;
        }
        if(y <= toY && width > 150) {
            opacity -= 0.2f;
            if(opacity < 0) {
                opacity = 0.1f;
            }
        }
    }
    
    public boolean shouldBeRemoved() {
        return opacity == 0.1f;
    }
    
    public void paint(Graphics2D g) {
        Composite c = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g.drawImage(image, (int) x - width / 2 - tileMap.getX(), (int) y - height / 2 - tileMap.getY(), width, height, null);
        g.setComposite(c);
    }
}