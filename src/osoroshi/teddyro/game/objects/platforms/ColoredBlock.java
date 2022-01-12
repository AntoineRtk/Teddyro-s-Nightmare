package osoroshi.teddyro.game.objects.platforms;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class ColoredBlock extends MovingPlatform {
    
    private boolean solid = false;
    
    public ColoredBlock(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params, 0);
        setSolid(params != null);
    }
    
    public void setSolid(boolean solid) {
        this.solid = solid;
        if(this.solid) {
            setOpacity(1f);
        }
        else {
            setOpacity(0.25f);
        }
    }
    
    public Shape getShape() {
        if(!solid) return new Rectangle2D.Double(0, 0, 0, 0);
        return super.getShape();
    }
    
    public void paint(Graphics2D g) {
        boolean s = solid;
        solid = true;
        super.paint(g);
        solid = s;
    }
    
    public double getX() {
        return (solid) ? super.getX() : -width;
    }
    
    public double getY() {
        return (solid) ? super.getY() : -height;
    }
}