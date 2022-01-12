package osoroshi.teddyro.game.tilemap;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tile implements Cloneable {
    
    private BufferedImage image;
    private boolean solid;
    
    public Tile(BufferedImage image, boolean solid) {
        this.image = image;
        this.solid = solid;
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public boolean isSolid() {
        return solid;
    }
    
    public Tile clone() {
        try {
            return (Tile) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void setSolid(boolean b) {
        this.solid = b;
    }
    
    public boolean isSlope() {
        return false;
    }
    
    public boolean isUpward() {
        return false;
    }
    
    public int getStartY() {
        return 0;
    }
    
    public int getEndY() {
        return 0;
    }
}