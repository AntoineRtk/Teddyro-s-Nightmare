package osoroshi.teddyro.game.objects.mobs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.JukeBox;

public class CannonBall extends Monster {
    
    private BufferedImage[] explosion;
    private int dir;
    private double x1, y1;
    
    public CannonBall(GameLevel gl, TileMap tileMap, double x, double y, BufferedImage[] explosion, int dir) {
        super(gl, tileMap, x, y, 1, 2, 0, true, -1);
        this.dir = dir;
        moveSpeed = 3;
        maxMoveSpeed = 12;
        width = 35;
        height = 35;
        BufferedImage image = new BufferedImage(35, 35, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.black);
        g.fillOval(0, 0, 35, 35);
        g.dispose();
        animation.setFrames(new BufferedImage[]{image});
        setDirX(dir);
        this.explosion = explosion;
        if(this.tileMap.isSolid(x + width / 2, y + height)) {
            this.y = (int) ((y + height) / 32) * 32 - height;
        }
    }
    
    public void update() {
        super.update();
        super.checkCollisions();
        if(shouldBeRemoved()) {
            tileMap.removeEnemy(this);
            return;
        }
        if(dx == 0 && dy == 0 && animation.getFrames() != explosion) {
            animation.setFrames(explosion);
            animation.setDelay(1);
            JukeBox.play("explosion");
            setX(-width);
            setY(-height);
        }
    }
    
    public boolean hit(int damage) {
        dx = 0;
        dy = 0;
        setDirX(0);
        x1 = getX();
        y1 = getY();
        return true;
    }
    
    public void hitHero() {
        dx = 0;
        dy = 0;
        setDirX(0);
        x1 = getX();
        y1 = getY();
    }
    
    public void paint(Graphics2D g) {
        setX(x1);
        setY(y1);
        super.paint(g);
        setX(-width);
        setY(-height);
    }
    
    public boolean shouldBeRemoved() {
        return animation.getFrames() == explosion && animation.hasPlayedOnce();
    }
    
    public BufferedImage[] getExplosionFrames() {
        return explosion;
    }
    
    public Shape getShape() {
        return new Ellipse2D.Double(getX(), getY(), width, height);
    }
}