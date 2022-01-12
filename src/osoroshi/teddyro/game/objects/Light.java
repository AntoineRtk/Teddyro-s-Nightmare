package osoroshi.teddyro.game.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.tilemap.MazeTileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class Light {
    
    public static final int CIRCLE = 0, STAR = 1;
    private MazeTileMap mzm;
    private BufferedImage image;
    private int fx, fy, x, y, toX, toY, count = 0;
    private boolean r;
    private double rotation;
    
    public Light(MazeTileMap mzm, int x, int y, int toX, int toY, int type) {
        this.mzm = mzm;
        this.fx = x + 128;
        this.fy = y + 128;
        this.toX = toX + 128;
        this.toY = toY + 128;
        this.x = this.fx;
        this.y = this.fy;
        image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(new java.util.Random().nextInt(256), new java.util.Random().nextInt(256), new java.util.Random().nextInt(256)));
        switch(type) {
            case CIRCLE:
                g.fillOval(0, 0, 30, 30);
                break;
            case STAR:
                g.fill(GameResource.getStarPath(30, 30));
                break;
        }
        g.dispose();
    }
    
    public void update() {
        double distX = toX - fx;
        double distY = toY - fy;
        if(r) {
            distX = -distX;
            distY = -distY;
        }
        int c = (int) (Math.max(Math.abs(distX), Math.abs(distY)) / 4);
        x += distX / c;
        y += distY / c;
        count++;
        if(count >= c - 1) {
            count = 0;
            r = !r;
        }
        rotation += 5.0 * Math.PI / 180.0;
    }
    
    public void paint(Graphics2D g) {
        AffineTransform af = g.getTransform();
        g.setTransform(AffineTransform.getRotateInstance(rotation, x - mzm.getX() + 15, y - mzm.getY() + 15));
        g.drawImage(image, x - mzm.getX(), y - mzm.getY(), null);
        g.setTransform(af);
    }
}