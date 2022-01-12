package osoroshi.teddyro.game.animations;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class DustExplosion extends MapElement {
    
    private BufferedImage image;
    private boolean hasJumped = false;
    private double rotation = new java.util.Random().nextInt(360);
    
    public DustExplosion(GameLevel gl, TileMap tileMap, BufferedImage image, double x, double y, boolean tile, int w, int h) {
        super(gl, tileMap, x, y, null);
        this.image = image;
        jumpStart = -15;
        moveSpeed = 0.1;
        maxMoveSpeed = 0.3 + new java.util.Random().nextDouble() * 0.35;
        setDirX(new java.util.Random().nextInt(3) - 1);
        width = w;
        height = h;
        fallingSpeed = 0.75 + Math.random() * 0.6;
        maxFallingSpeed = 8;
        if(!tile) {
            hasJumped = true;
            dy = jumpStart;
        }
        else {
        int tileY = 0;
            for(;;) {
                if(tileMap.getTile(x + 0.5, y + tileY).isSolid()) {
                    this.y = (int) ((y + tileY) / 32.0) * 32 - 1;
                    break;
                }
                else {
                    tileY += 32;
                }
            }
        }
    }
    
    public void update() {
        if(tileMap.isSolid(getX(), getY())) {
            setX((int) (getX() / 32.0) * 32);
        }
        if(tileMap.isSolid(getX() + width - 1, getY())) {
            setX((int) ((getX() + width - 1) / 32.0) * 32 - width);
        }
        rotation += new java.util.Random().nextInt(30) * getDirX();
        if(dy == 0 && hasJumped) {
            tileMap.removeObject(this);
        }
        if(!hasJumped && !isFalling()) {
            hasJumped = true;
            setJumping(true);
        }
        super.checkCollisions();
    }
    
    public void paint(Graphics2D g) {
        AffineTransform af = g.getTransform();
        g.setTransform(AffineTransform.getRotateInstance(rotation * Math.PI / 180.0, getX() - tileMap.getX() + image.getWidth() / 2.0, getY() - tileMap.getY() + image.getHeight() / 2.0));
        g.drawImage(image, (int) getX() - tileMap.getX(), (int) getY() - tileMap.getY(), null);
        g.setTransform(af);
    }
}