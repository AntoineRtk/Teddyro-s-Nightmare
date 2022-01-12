package osoroshi.teddyro.game.objects.platforms;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class SpikeWall extends Platform {
    
    private double startingY;
    private int count = 0, preCount = 0;
    private boolean isTopSpike = false, onFire = false, falling = false;
    private ArrayList<Flame> flames = new ArrayList<>();
    
    public SpikeWall(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        width = 128;
        height = 103;
        animation.setFrames(new BufferedImage[]{GameResource.getYFlippedInstance(GameResource.getTile(434).getImage().getSubimage(0, 25, 128, 103))});
        startingY = y;
        isTopSpike = params != null;
        if(isTopSpike) {
            height = 206;
            BufferedImage newImage = new BufferedImage(128, 206, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newImage.createGraphics();
            g.drawImage(GameResource.getTile(434).getImage().getSubimage(0, 25, 128, 103), 0, 0, null);
            g.drawImage(getImage(), 0, 103, null);
            g.dispose();
            animation.setFrames(new BufferedImage[]{newImage});
            double ratio = 0.7;
            if(params.contains("-")) {
                preCount = Integer.parseInt(params.split("-")[1]) + 1;
                onFire = true;
                for(int w = 0; w < width; w++) {
                    for(int h = 0; h < height; h++) {
                        if(new Color(newImage.getRGB(w, h)).getGreen() != 0 && new Color(newImage.getRGB(w, h)).getBlue() != 0) {
                            Color lc = new Color(newImage.getRGB(w, h));
                            Color nc = new Color(lc.getRed() + (int) ((255 - lc.getRed()) * ratio), lc.getGreen() + (int) ((-lc.getGreen()) * ratio), lc.getBlue() + (int) ((-lc.getBlue()) * ratio));
                            newImage.setRGB(w, h, nc.getRGB());
                        }
                    }
                }
            }
        }
    }
    
    public void update() {
        super.update();
        super.checkCollisions();
        setJumping(true);
        jumpStart = 0;
        if(count != -1) count++;
        if(preCount != 0) {
            preCount--;
            count = -1;
            if(preCount == 0) {
                count = 0;
            }
        }
        if(count == 90 || (isTopSpike && count == 30)) {
            falling = true;
        }
        if(falling) {
            jumpStart = 4.5;
            setJumping(true);
            if(hasIntersectedInY()) {
                count = -1;
                falling = false;
            }
        }
        if(count == -1 && !falling) {
            jumpStart = -4.5;
            setJumping(true);
            if(getY() + jumpStart < startingY) {
                jumpStart = getY() - startingY;
                count = 0;
            }
        }
        if(onFire) {
            if(count % 6 == 0) {
                flames.add(new Flame(getX() + Math.random() * (width - 10), getY() + Math.random() * (height - 10)));
            }
            for(int i = 0; i < flames.size(); i++) {
                flames.get(i).update();
                if(flames.get(i).shouldBeRemoved()) {
                    flames.remove(i);
                }
            }
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(onFire) {
            for(int i = 0; i < flames.size(); i++) {
                flames.get(i).paint(g);
            }
        }
    }
    
    public boolean intersectX(MapObject mo, int dir) {
        if(onFire && mo instanceof Hero) {
            tileMap.hero.kill();
        }
        return true;
    }
    
    public boolean intersectY(MapObject mo, int dir) {
        if(mo instanceof Hero && ((dir == -1 && !isTopSpike) || isTopSpike)) {
            tileMap.hero.kill();
            falling = false;
        }
        return true;
    }
    
    private class Flame {
        
        private double x, y, py;
        private int count = 0;
        
        public Flame(double x, double y) {
            this.x = x;
            this.y = y;
            this.py = (getY() + height - 1) - y;
        }
        
        public void update() {
            count++;
        }
        
        public void paint(Graphics2D g) {
            g.setColor(new Color(255, 0, 0, 255 - (int) (255.0 / 10.0 * count)));
            g.fillOval((int) x - tileMap.getX(), (int) (getY() + py) - tileMap.getY(), count, count);
        }
        
        public boolean shouldBeRemoved() {
            return count > 10;
        }
    }
}