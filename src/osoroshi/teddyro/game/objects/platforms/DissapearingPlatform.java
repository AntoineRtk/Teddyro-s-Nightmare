package osoroshi.teddyro.game.objects.platforms;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class DissapearingPlatform extends Platform {
    
    private int nbr = 0, count = 0;
    
    public DissapearingPlatform(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        animation.setFrames(new BufferedImage[]{GameResource.getTile(2).getImage()});
        width = 32;
        height = 32;
    }
    
    public void update() {
        super.update();
        count++;
        if(count <= 90) {
            nbr = 3 - (int) (3.0 / 90.0 * count);
        }
        else if(count > 90 && count < 106) {
            tileMap.hero.losePlatform(this);
            float alpha = 1.0f - (1.0f / 15.0f * (count - 90));
            if(alpha < 0) alpha = 0f;
            setOpacity(alpha);
        }
        else if(count > 120) {
            setOpacity(1f);
            count = 0;
            nbr = 3;
        }
    }
    
    public void paint(Graphics2D g) {
        int c = count;
        count = 0;
        super.paint(g);
        count = c;
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        TextLayout txt = new TextLayout(nbr+"", g.getFont(), g.getFontRenderContext());
        if(nbr != 0) g.drawString(nbr+"", (int) super.getX() - tileMap.getX() + 15 - (int) txt.getBounds().getWidth() / 2, (int) super.getY() - tileMap.getY() + 24);
    }
    
    public double getX() {
        return (count < 91) ? super.getX() : -width;
    }
    
    public double getY() {
        return (count < 91) ? super.getY() : -height;
    }
}