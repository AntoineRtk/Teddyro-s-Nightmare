package osoroshi.teddyro.game.animations;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class ShootingStars {
    
    private TileMap tileMap;
    private BufferedImage image;
    private double x, y, dx, dy, rotation;
    private int count = 0;
    private float opacity = 1.0f;
    
    public ShootingStars(TileMap tileMap, double x, double y, int dir) {
        this.tileMap = tileMap;
        this.x = x;
        this.y = y;
        image = GameResource.getStarImage(16, 16);
        Color color = new Color(new java.util.Random().nextInt(256), new java.util.Random().nextInt(256), new java.util.Random().nextInt(256));
        for(int h = 0; h < image.getHeight(); h++) {
            for(int w = 0; w < image.getWidth(); w++) {
                if(image.getRGB(w, h) != 0) {
                    image.setRGB(w, h, color.getRGB());
                }
            }
        }
        rotation = Math.toRadians(new java.util.Random().nextInt(361));
        dx = new java.util.Random().nextDouble() * (dir * 4.5);
        dy = new java.util.Random().nextGaussian();
    }
    
    public void update() {
        rotation += (Math.PI / 180.0) * (1 + new java.util.Random().nextInt(3));
        x += dx;
        y += dy;
        count++;
        if(count >= 90) {
            opacity -= 0.05;
            if(opacity <= 0.0) {
                opacity = 0.0f;
            }
        }
        for(int i = 0; i < tileMap.mobs[tileMap.getArea()].size(); i++) {
            if(tileMap.mobs[tileMap.getArea()].get(i).intersects(getBounds())) {
                tileMap.mobs[tileMap.getArea()].get(i).hit(3);
            }
        }
    }
    
    public void paint(Graphics2D g) {
        AffineTransform af = g.getTransform();
        Composite c = g.getComposite();
        g.setTransform(AffineTransform.getRotateInstance(rotation, x + 8 - tileMap.getX(), y + 8 - tileMap.getY()));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g.drawImage(image, (int) x - tileMap.getX(), (int) y - tileMap.getY(), null);
        g.setComposite(c);
        g.setTransform(af);
    }
    
    public boolean shouldBeRemoved() {
        return opacity == 0.0f;
    }
    
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, 16, 16);
    }
}