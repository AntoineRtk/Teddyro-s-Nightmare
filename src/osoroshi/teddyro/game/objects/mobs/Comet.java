package osoroshi.teddyro.game.objects.mobs;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class Comet {
    
    private TileMap tileMap;
    private BufferedImage image;
    private ArrayList<Tail> tails = new ArrayList<>();
    private double x = -3000, y = -3000, dx, dy;
    
    public Comet(TileMap tileMap) {
        this.tileMap = tileMap;
        image = GameResource.getFirework(7).getSubimage(0, 0, 73, 73);
    }
    
    public void spawn() {
        dx = 17 + new java.util.Random().nextDouble() * 10;
        dy = 10 + new java.util.Random().nextDouble() * 10;
        x = tileMap.getX();
        y = tileMap.getY();
        if(new java.util.Random().nextInt(2) == 1) {
            x += 700;
            dx = -dx;
        }
    }
    
    public void update() {
        if(dx > 0) {
            BufferedImage flipped = getFlipped();
            int wm = 0;
            for(int h = 10; h < flipped.getHeight(); h++) {
                for(int w = flipped.getWidth() / 2 - wm; w > -1; w--) {
                    tails.add(new Tail(tileMap, x + w, y + h, flipped.getRGB(w, h)));
                }
            }
        }
        else {
            int wp = 0;
            for(int h = 10; h < image.getHeight(); h++) {
                for(int w = image.getWidth() / 2 + wp; w < image.getWidth(); w++) {
                    tails.add(new Tail(tileMap, x + w, y + h, image.getRGB(w, h)));
                }
            }
        }
        for(int i = 0; i < tails.size(); i++) {
            tails.get(i).update();
            if(tails.get(i).shouldBeRemoved()) {
                tails.remove(i);
            }
        }
        x += dx;
        y += dy;
    }
    
    public BufferedImage getFlipped() {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, image.getWidth(), 0, -image.getWidth(), image.getHeight(), null);
        g.dispose();
        return newImage;
    }
    
    public void paint(Graphics2D g) {
        for(int i = 0; i < tails.size(); i++) {
            tails.get(i).paint(g);
        }
        if(dx < 0) {
            g.drawImage(image, (int) x - tileMap.getX(), (int) y - tileMap.getY(), null);
        }
        else {
            g.drawImage(image, (int) x - tileMap.getX() + image.getWidth(), (int) y - tileMap.getY(), -image.getWidth(), image.getHeight(), null);
        }
    }
    
    public class Tail {
        
        private TileMap tileMap;
        private final double x, y;
        private int rgb;
        private float opacity = 1;
        
        public Tail(TileMap tileMap, double x, double y, int rgb) {
            this.tileMap = tileMap;
            this.x = x;
            this.y = y;
            this.rgb = rgb;
        }
        
        public void update() {
            opacity -= 0.25f + new java.util.Random().nextFloat() * 0.85f;
            if(opacity < 0.1f) {
                opacity = 0.1f;
            }
        }
        
        public void paint(Graphics2D g) {
            if(new Color(rgb).getRGB() == Color.white.getRGB() || new Color(rgb).getRGB() == Color.black.getRGB()) return;
            Composite c = g.getComposite();
            g.setColor(new Color(rgb));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g.fillRect((int) x - tileMap.getX(), (int) y - tileMap.getY(), 10, 10);
            g.setComposite(c);
        }
        
        public boolean shouldBeRemoved() {
            return opacity == 0.1f || new Color(rgb).getRGB() == Color.white.getRGB() || new Color(rgb).getRGB() == Color.black.getRGB();
        }
    }/*Mario Galaxy - Star Festival https://www.youtube.com/watch?v=nNMSAXNzB20
Mario Galaxy - Good Egg Galaxy */
}