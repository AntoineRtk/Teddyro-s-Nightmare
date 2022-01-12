package osoroshi.teddyro.game.tilemap;

import java.awt.image.BufferedImage;

public class Slope extends Tile {
    
    private int startY, endY;
    
    public Slope(BufferedImage image, int startY, int endY) {
        super(image, true);
        this.startY = startY;
        this.endY = endY;
    }
    
    public boolean isSlope() {
        return true;
    }
    
    public boolean isUpward() {
        return endY < startY;
    }
    
    public int getStartY() {
        return startY;
    }
    
    public int getEndY() {
        return endY;
    }
    
    public boolean isSolid(int dir) {
        return super.isSolid();
    }
}