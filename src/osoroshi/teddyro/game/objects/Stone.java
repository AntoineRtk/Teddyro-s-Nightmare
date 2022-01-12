package osoroshi.teddyro.game.objects;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.animations.DustExplosion;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class Stone extends MapElement {
    
    private int wait, waitTime;
    
    public Stone(GameLevel gl, TileMap tileMap, double x, double y, int waitTime) {
        super(gl, tileMap, x, y, null);
        this.waitTime = waitTime;
        width = 30;
        height = 30;
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setClip(new Ellipse2D.Double(0, 0, 30, 30));
        g.drawImage(GameResource.getTile(325).getImage(), (int) (Math.random() * -96), (int) (Math.random() * -96), null);
        g.dispose();
        animation.setFrames(new BufferedImage[]{image});
        fallingSpeed = 0.4;
        maxFallingSpeed = 12.8;
    }
    
    public void update() {
        if(wait >= waitTime) super.checkCollisions();
        wait++;
        rotation += 5;
        if(hasIntersectedInY()) {
            destroy();
        }
        if(tileMap.hero.intersects(this)) {
            tileMap.hero.hit(1 + (new java.util.Random().nextInt(3) == 0 ? 1 : 0));
            destroy();
        }
    }
    
    public void destroy() {
        for(int i = 0; i < 5; i++) {
            for(int i1 = 0; i1 < 5; i1++) {
                tileMap.addObject(new DustExplosion(gl, tileMap, getImage().getSubimage(i * 6, i1 * 6, 6, 6), getX() + i * 6, getY() + i * 6, false, 6, 6));
            }
        }
        tileMap.removeObject(this);
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
    }
}