package osoroshi.teddyro.game.objects.platforms;

import java.awt.Color;
import java.awt.Graphics2D;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class Wall extends Platform {
    
    private int ellipseX, ellipseY, ellipseWidth, ellipseHeight, alpha = 255;
    private double iniX;
    private boolean closing = false;
    
    public Wall(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, null);
        animation.setFrames(GameResource.getWallSprites().get(0));
        width = 32;
        height = 64;
        fallingSpeed = 0.3;
        maxFallingSpeed = 100.8-90;
    }
    
    public void update() {
        super.checkCollisions();
        if(closing) {
            ellipseX -= 32;
            ellipseY -= 32;
            ellipseWidth += 64;
            ellipseHeight += 64;
            alpha = (int) (255.0 / 3200.0 * (3200 - Math.abs(ellipseX)));
            x = iniX + 16.0 / 255.0 * (255.0 - alpha);
            width = (int) (iniX + 16 - x) * 2;
            if(alpha < 0) {
                tileMap.removePlatform(this);
            }
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(closing) {
            for(int ratio = 10; ratio > -1; ratio -= 1) {
                int green = (int) (255.0 / 10.0 * ratio);
                g.setColor(new Color(0, green, 255, alpha));
                int r = green / (255 / 10);
                g.drawOval((int) getX() + ellipseX - tileMap.getX() + r, (int) getY() + ellipseY - tileMap.getY() + r, ellipseWidth - r * 2, ellipseHeight - r * 2);
            }
        }
    }
    
    public void open() {
        closing = true;
        iniX = getX();
        ellipseWidth = width;
        ellipseHeight = height;
    }
}