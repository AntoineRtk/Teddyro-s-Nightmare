
package osoroshi.teddyro.game.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.states.GameStateManager;
import osoroshi.teddyro.game.utils.GameResource;

public class LevelSelector4 extends LevelSelector {
    
    private BufferedImage bg;
    private int p1, p2, p3, p4;
    
    public LevelSelector4(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        super.init();
        bg = GameResource.getWorld5Background();
        int[][][] levels = {
            {{getWidth() / 2 - 30, getHeight() / 2 + bg.getHeight() / 2, 0}, {}, {}, {-1, 1}},
            {{0, getHeight() / 2 - 30, 4}, {}, {}, {1, 0}}
        };
        for(int i = 0; i < levels.length; i++) {
            addLevel(levels[i]);
        }
    }
    
    public void paint(Graphics2D g) {
        g.setColor(new Color(p1));
        g.fillRect(0, 0, getWidth() / 2 - 1, getHeight() / 2 - 1);
        g.setColor(new Color(p2));
        g.fillRect(getWidth() / 2, 0, getWidth() / 2 - 1, getHeight() / 2 - 1);
        g.setColor(new Color(p3));
        g.fillRect(0, getHeight() / 2, getWidth() / 2 - 1, getHeight() / 2 - 1);
        g.setColor(new Color(p4));
        g.fillRect(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 1, getHeight() / 2 - 1);
        g.drawImage(bg, getWidth() / 2 - bg.getWidth() / 2, getHeight() / 2 - bg.getHeight() / 2, null);
        super.paint(g);
    }
    
    public int getWorld() {
        return 3;
    }
}