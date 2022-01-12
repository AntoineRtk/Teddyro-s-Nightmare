package osoroshi.teddyro.game.objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;

public class Bird extends MapObject {
    
    private ArrayList<BufferedImage[]> animations;
    private int color = new java.util.Random().nextInt(2);
    private double angle;
    private boolean canBeRemoved = false, f;
    
    public Bird(TileMap tileMap, double x, double y, boolean flipped) {
        super(tileMap, x, y);
        animations = GameResource.getBirdsSprites();
        width = 8;
        height = 7;
        animation.setFrames(animations.get(new java.util.Random().nextInt(2)));
        if(flipped) {
            f = true;
            angle = -180;
            BufferedImage[] frames = animation.getFrames();
            BufferedImage toReplace = new BufferedImage(8, 7, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = toReplace.createGraphics();
            g.drawImage(frames[0], 0, 7, 8, -7, null);
            g.dispose();
            animation.setFrames(new BufferedImage[]{toReplace});
        }
    }
    
    public void update() {
        if(Controler.isUp() || Controler.isLeft()) {
            angle -= !f ? 3.75 : -3.75;
        }
        else if(Controler.isDown() || Controler.isRight()) {
            angle += !f ? 3.75 : -3.75;
        }
        dx = Math.cos(angle * Math.PI / 180.0) * 7.75;
        jumpStart = Math.sin(angle * Math.PI / 180.0) * 7.75;
        if(Math.abs(angle) % 360 > 0 && Math.abs(angle) % 360 <= 90 || (Math.abs(angle) % 360 > 180 && Math.abs(angle) % 360 <= 270)) {
            jumpStart += 3.25;
        }
        setJumping(true);
        rotation = angle;
        super.update();
        super.checkCollisions();
        if(hasIntersectedInX() || hasIntersectedInY()) {
            canBeRemoved = true;
        }
    }
    
    public boolean canBeRemoved() {
        return canBeRemoved;
    }
}