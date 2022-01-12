package osoroshi.teddyro.game.objects.mobs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.JukeBox;

public class AutoCannonBall extends CannonBall {
    
    private BufferedImage arrow;
    private double angle;
    private int count = 29;
    
    public AutoCannonBall(GameLevel gl, TileMap tileMap, double x, double y, BufferedImage[] explosion, BufferedImage arrow, int dir) {
        super(gl, tileMap, x, y, explosion, dir);
        BufferedImage image = new BufferedImage(35, 35, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(127, 0, 0));
        g.fillOval(0, 0, 35, 35);
        g.dispose();
        animation.setFrames(new BufferedImage[]{image});
        this.arrow = arrow;
        moveSpeed = 1;
        stopSpeed = 0.2;
        maxMoveSpeed = 1;
    }
    
    public void update() {
        setJumping(true);
        super.update();
        count++;
        if(count > 30) {
            double adj = Math.abs((tileMap.hero.getX() + tileMap.hero.width / 2) - (getX() + width / 2)),
                    opp = Math.abs((tileMap.hero.getY() + tileMap.hero.height / 2) - (getY() + width / 2));
            maxMoveSpeed = (adj > 0) ? 2 + new java.util.Random().nextInt(3) : adj / 30;
            jumpStart = (tileMap.hero.getY() + tileMap.hero.height / 2 < getY() + height / 2) ? -(opp / 30) : opp / 30;
            count = 0;
            if(tileMap.hero.getX() > getX() + width / 2) {
                if(getDirX() == -1) dx = 1;
                setDirX(1);
            }
            else {
                if(getDirX() == 1) dx = -1;
                setDirX(-1);
            }
        }
        if(!JukeBox.isRunning("warning")) {
            JukeBox.setLooping("warning", 0, -1);
        }
        if(animation.getFrames() == getExplosionFrames()) {
            JukeBox.stop("warning");
        }
        double hypot = Math.hypot((tileMap.hero.getX() + tileMap.hero.width / 2) - (getX() + width / 2), (tileMap.hero.getY() + tileMap.hero.height / 2) - (getY() + height / 2));
        angle = Math.acos(((tileMap.hero.getX() + tileMap.hero.width / 2) - (getX() + width / 2))/hypot);
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(animation.getFrames() == getExplosionFrames()) return;
        AffineTransform af = g.getTransform();
        if(tileMap.hero.getY() + height / 2 < getY() + height / 2) {
            g.setTransform(AffineTransform.getRotateInstance(-angle, getX() + width / 2 - tileMap.getX(), getY() + height / 2 - tileMap.getY()));
        }
        else {
            g.setTransform(AffineTransform.getRotateInstance(angle, getX() + width / 2 - tileMap.getX(), getY() + height / 2 - tileMap.getY()));
        }
        g.drawImage(arrow, (int) getX() + width - tileMap.getX(), (int) getY() + height / 2 - arrow.getHeight() / 2 - tileMap.getY(), null);
        g.setTransform(af);
        /*if(new java.util.Random().nextInt(300) != 0) return;
        g.setColor(Color.red);
        g.drawLine((int) getX() - tileMap.getX(), (int) getY() - tileMap.getY(), (int) tileMap.hero.getX() - tileMap.getX(), (int) getY() - tileMap.getY());
        g.drawLine((int) getX() - tileMap.getX(), (int) getY() - tileMap.getY(), (int) tileMap.hero.getX() - tileMap.getX(), (int) tileMap.hero.getY() - tileMap.getY());
        g.drawLine((int) tileMap.hero.getX() - tileMap.getX(), (int) tileMap.hero.getY() - tileMap.getY(), (int) tileMap.hero.getX() - tileMap.getX(), (int) getY() - tileMap.getY());
    */}
}