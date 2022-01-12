package osoroshi.teddyro.game.tilemap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.MapObject;

public class SpeedTile extends SpecialTile {
    
    private int count = -1;
    private int colorCount = 0;
    private boolean reverse = false;
    
    public SpeedTile() {
        super(null, false);
    }
    
    public void update() {
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(255, (int) (128.0 / 24.0 * colorCount), 0));
        g.fillRect(0, 29, 32, 3);
        g.dispose();
        setImage(image);
        if(reverse) colorCount--;
        else colorCount++;
        if(colorCount < 0) {
            reverse = false;
            colorCount = 0;
        }
        if(colorCount > 24) {
            reverse = true;
            colorCount = 24;
        }
    }
    
    public void doAction(MapObject mo, boolean x, int dir) {
        int yp = mo.height - 32;
        if(yp < 0) yp = 0;
        mo.dx = mo.maxMoveSpeed * ((mo.getDirX() == 0) ? 1 : mo.getDirX());
    }
}