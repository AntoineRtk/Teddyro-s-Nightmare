package osoroshi.teddyro.game.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.MapObject;

public class TrampolineTile extends SpecialTile {
    
    private BufferedImage normalImage, reducedImage;
    private int count = 16;
    
    public TrampolineTile(BufferedImage image) {
        super(image, true);
        normalImage = image;
        reducedImage = reduce(image);
    }
    
    private BufferedImage reduce(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 16, 32, 16, null);
        g.dispose();
        return newImage;
    }
    
    public void update() {
        if(count < 16) {
            setImage(reducedImage);
        }
        else {
            setImage(normalImage);
        }
        count++;
    }
    
    public void doAction(MapObject mo, boolean x, int dir) {
        if(x || (!x && dir == -1)) return;
        double j = -20;
        if(mo.getDirY() == -1) j -= 10;
        mo.jump(j);
        count = 0;
    }
}