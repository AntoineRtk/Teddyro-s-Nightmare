
package osoroshi.teddyro.game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.utils.GameResource;

public class FinalCutscene extends GameState {
    
    private BufferedImage bg;
    private double x, y, ratiox, ratioy;
    private double dx, dy, ratiodx, ratiody;
    private int count = 0;
    
    public FinalCutscene(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        bg = GameResource.getWorld4Background();
        dx = (-9000 + getWidth() / 2.0) / 125.0;
        dy = (-4800 + getHeight() / 2.0) / 125.0;
        ratiodx = (bg.getWidth() * 10.0 / getWidth() - 1.0) / 125.0;
        ratiody = (bg.getHeight() * 10.0 / getHeight() - 1.0) / 125.0;
        x = 0;
        y = 0;
        ratiox = 1;
        ratioy = 1;
    }
    
    public void update() {
        count++;
        if(count >= 250) {
            x += dx;
            y += dy;
            ratiox += ratiodx;
            ratioy += ratiody;
            if(count - 250 >= 125) {
                x = -9000 + getWidth() / 2.0;
                y = -4800 + getHeight() / 2.0;
                ratiox = (bg.getWidth() * 10.0) / getWidth();
                ratioy = (bg.getHeight() * 10.0) / getHeight();
            }
        }
        if(count >= 500) {
            count = 500;
            gsm.setState(26);
        }
    }
    
    public void paint(Graphics2D g) {
        g.drawImage(bg, (int) x, (int) y, (int) (getWidth() * ratiox), (int) (getHeight() * ratioy), null);
        if(count >= 375) {
            g.setColor(new Color(255, 255, 255, (int) (255.0 / 125.0 * (count - 375.0))));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        if(count >= 250) return;
        int alpha = 255;
        if(count < 250.0 / 4.0) {
            alpha = (int) (255.0 / (250.0 / 4.0) * count);
        }
        else if(count > 250 / 4.0 * 3.0) {
            alpha = 255 - (int) (255.0 / (250.0 / 4.0) * (count - 250 / 4.0 * 3.0));
        }
        g.setColor(new Color(0, 0, 255, alpha));
        g.setFont(new Font("Gabriola", Font.BOLD, 27));
        g.drawString("Dans la plus profonde des galaxies... Au plus profond de son cauchemar...", 0, (int) (getHeight() / 1.25));
    }
}