package osoroshi.teddyro.game.objects;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.animations.BouncingBallExplosion;
import osoroshi.teddyro.game.objects.mobs.Monster;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class BouncingBall extends MapElement {
    
    private boolean colliding = false;
    
    public BouncingBall(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, null);
        BufferedImage image = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setClip(new Ellipse2D.Double(0, 0, 80, 80));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(new GradientPaint(0, 0, Color.yellow, -5, 10, new Color(255, 130, 0), true));
        g.fillOval(0, 0, 80, 80);
        BufferedImage starImage = GameResource.getStarImage(20, 20);
        int yp = 1;
        for(int i = 0; i < 6; i++) {
            Color newColor = new Color(new java.util.Random().nextInt(256), new java.util.Random().nextInt(256), new java.util.Random().nextInt(256));
            for(int w = 0; w < starImage.getWidth(); w++) {
                for(int h = 0; h < starImage.getHeight(); h++) {
                    if(starImage.getRGB(w, h) != 0) {
                        starImage.setRGB(w, h, newColor.getRGB());
                    }
                }
            }
            int xp = new java.util.Random().nextInt(80 - 20);
            int ypp = yp * 10 + new java.util.Random().nextInt(5);
            if(i % 2 == 0) {
                yp += 2;
            }
            g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(new java.util.Random().nextInt(360)), xp + 10, ypp + 10));
            g.drawImage(starImage, xp, ypp, null);
        }
        g.dispose();
        animation.setFrames(new BufferedImage[]{image});
    }
    
    public void update() {
        if(width == 80) {
            super.checkCollisions();
        }
        setJumping(true);
        if(dx == 0 && width == 80) {
            setDirX(-getDirX());
            if(onScreen()) {
                JukeBox.playNewInstance("bbjump");
            }
        }
        rotation += getDirX() * 3 + (3 / 5.45 * dx);
        if(dy == 0 && !isFalling()) {
            if(jumpStart + 1 >= 0) {
                setDirX(0);
                maxMoveSpeed = 0;
            }
            if(width == 80 && getDirX() != 0 && onScreen()) {
                JukeBox.playNewInstance("bbjump");
            }
            jumpStart += 1;
        }
        for(int i = 0; i < tileMap.mobs[area].size(); i++) {
            if(dx == 0) break;
            Monster m = tileMap.mobs[area].get(i);
            if(getShape().intersects(m.getX(), m.getY(), m.width, m.height)) {
                m.hit(3);
                m.stunt();
            }
        }
        if(shouldBeRemoved()) {
            BufferedImage rotatedImage = createRotatedBufferedImage(getImage(), rotation);
            for(int h = 0; h < 80 / 3; h++) {
                for(int w = 0; w < 80 / 5; w++) {
                   tileMap.addObject(new BouncingBallExplosion(gl, tileMap, getX() + w * 5, getY() + h * 3, rotatedImage.getSubimage(w * 5, h * 3, 5, 3)));
                }
            }
            tileMap.removeObject(this);
        }
        boolean hasCollided = false;
        for(int i = 0; i < tileMap.objects[area].size(); i++) {
            if(!(tileMap.objects[area].get(i) instanceof BouncingBall) || width != 80) continue;
            BouncingBall b = (BouncingBall) tileMap.objects[area].get(i);
            if(b == this) continue;
            if(isCollidingWith(b)) {
                hasCollided = true;
                if(!isColliding()) {
                    b.setColliding(true);
                    setColliding(true);
                    b.setDirX(-b.getDirX());
                    setDirX(-getDirX());
                }
            }
        }
        setColliding(hasCollided);
    }
    
    private boolean isCollidingWith(BouncingBall bcb) {
        //Pythagorean Theorem
        double dist = Math.sqrt(Math.pow((bcb.getX() + 40) - (getX() + 40), 2) + Math.pow((bcb.getY() + 40) - (getY() + 40), 2));
        if(dist <= 80) {
            return true;
        }
        return false;
    }
    
    public Shape getShape() {
        return new Ellipse2D.Double(getX(), getY(), width, height);
    }
    
    public void initState() {
        moveSpeed = 2.4;
        maxMoveSpeed = 6.25;
        fallingSpeed = 0.7;
        maxFallingSpeed = 9 + new java.util.Random().nextDouble() * (11.75 - 9);
        jumpStart = -20 + -new java.util.Random().nextInt(10) + -1;
        //-28
        stopSpeed = 0.1;
        y -= 80;
    }
    
    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }
    
    public boolean isColliding() {
        return colliding;
    }
    
    public boolean onScreen() {
        return new Rectangle((int) getX(), (int) getY(), width, height).intersects(tileMap.getX(), tileMap.getY(), gl.getWidth(), gl.getHeight());
    }
    
    private boolean shouldBeRemoved() {
        return getDirX() == 0 && dx == 0 && dy == 0 && width == 80;
    }
    
    private BufferedImage createRotatedBufferedImage(BufferedImage image, double rotation) {
        BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = rotatedImage.createGraphics();
        g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rotation), image.getWidth() / 2, image.getHeight() / 2));
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return rotatedImage;
    }
}