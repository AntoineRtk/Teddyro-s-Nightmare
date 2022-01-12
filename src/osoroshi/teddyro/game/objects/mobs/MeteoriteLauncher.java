package osoroshi.teddyro.game.objects.mobs;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.Background;
import osoroshi.teddyro.game.tilemap.Tile;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class MeteoriteLauncher extends MapElement {
    
    private Meteorite meteorite;
    private ArrayList<BufferedImage[]> animations;
    private ArrayList<BlockParticle> blocksParticles;
    private boolean launched = false;
    
    public MeteoriteLauncher(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        animations = GameResource.getMeoritSprites();
        this.blocksParticles = new ArrayList<>();
    }
    
    public void update() {
        if(tileMap.hero.getX() > getX() && meteorite == null && !launched) {
            tileMap.hero.setCanAction(false);
            meteorite = new Meteorite(tileMap, 0, 0);
            gl.setCamera(meteorite);
            gl.setBackgroundScreenSize(Background.LOOPING);
            tileMap.hero.setX(getX() - tileMap.hero.width);
            tileMap.hero.dx = 0;
            launched = true;
        }
        if(meteorite != null) {
            meteorite.update();
            if(meteorite.getY() > tileMap.hero.getY() + tileMap.hero.height + gl.getHeight() / 2.0) {
                gl.setCamera(tileMap.hero);
                tileMap.hero.setCanAction(true);
                int blocks = (int) ((meteorite.getX() + meteorite.width - meteorite.getX()) / 32.0) + 1;
                for(int w = 0; w < blocks * 32; w += 32) {
                    tileMap.setTile(GameResource.getTile(40), (int) (meteorite.getX() + w) / 32, (int) (meteorite.getY() + meteorite.height + meteorite.dy) / 32);
                    for(int h = 1; h < 5; h++) {
                        tileMap.setTile(GameResource.getTile(44), (int) (meteorite.getX() + w) / 32, (int) (meteorite.getY() + meteorite.height + meteorite.dy) / 32 + h);
                    }
                }
                meteorite = null;
            }
        }
        for(int i = 0; i < blocksParticles.size(); i++) {
            blocksParticles.get(i).update();
            if(blocksParticles.get(i).canBeRemoved()) blocksParticles.remove(i);
        }
    }
    
    public void paint(Graphics2D g) {
        for(int i = 0; i < blocksParticles.size(); i++) {
            blocksParticles.get(i).paint(g);
        }
        if(meteorite != null) meteorite.paint(g);
    }
    
    private class Meteorite extends MapObject {
        
        public Meteorite(TileMap tileMap, double x, double y) {
            super(tileMap, x, y);
            animation.setFrames(animations.get(0));
            animation.setDelay(2);
            width = 90;
            height = 150;
            fallingSpeed = 7.75;
            maxFallingSpeed = 31.35;
        }
        
        public void update() {
            super.update();
            super.checkCollisions();
            this.setX(tileMap.hero.getX() + tileMap.hero.width);
            if(dy == 0) {
                int blocks = (int) ((meteorite.getX() + meteorite.width - meteorite.getX()) / 32.0) + 1;
                for(int w = 0; w < blocks * 32; w += 32) {
                    if(tileMap.isSolid(getX() + w, y + height + dy)) {
                        BufferedImage tileImage = tileMap.getTile(getX() + w, getY() + height + dy).getImage();
                        for(int h = 0; h < tileImage.getHeight(); h += 4) {
                            for(int ww = 0; ww < tileImage.getWidth(); ww += 4) {
                                blocksParticles.add(new BlockParticle(tileImage.getSubimage(ww, h, 4, 4), (int) getX() + w + 16, (int) getY() + height));
                            }
                        }
                        tileMap.setTile(new Tile(null, false), (int) (getX() + w) / 32, (int) (getY() + height + dy) / 32);
                    }
                }
            }
        }
    }
    
    private class BlockParticle {
        
        private double x, y, dx, dy, tdy;
        private BufferedImage image;
        private double rotation = new java.util.Random().nextInt(360);
        private int count = 0;
        
        public BlockParticle(BufferedImage image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
            dx = -2 + Math.random() * 4.0;
            dy = -10 - Math.random() * 20.0;
        }
        
        public void update() {
            rotation += new java.util.Random().nextInt(10);
            x += dx;
            y += dy;
            count++;
            if(count > 30) dy = 9.75;
        }
        
        public boolean canBeRemoved() {
            return count == 60;
        }
        
        public void paint(Graphics2D g) {
            Composite c = g.getComposite();
            AffineTransform af = g.getTransform();
            g.setTransform(AffineTransform.getRotateInstance(rotation * Math.PI / 180.0, x - tileMap.getX() + 2, y - tileMap.getY() + 2));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - 1.0f / 60.0f * count));
            g.drawImage(image, (int) x - tileMap.getX(), (int) y - tileMap.getY(), null);
            g.setTransform(af);
            g.setComposite(c);
        }
    }
}