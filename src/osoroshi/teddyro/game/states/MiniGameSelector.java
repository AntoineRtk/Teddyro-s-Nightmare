
package osoroshi.teddyro.game.states;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import osoroshi.teddyro.game.utils.GameResource;

public class MiniGameSelector extends GameState {
    
    private int count;
    private Color[] colors = new Color[80];
    private float opacity, gamesOpacity;
    
    public MiniGameSelector(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        count = 0;
        for(int i = 0; i < colors.length; i++) {
            colors[i] = new Color(new java.util.Random().nextInt(256), new java.util.Random().nextInt(256), new java.util.Random().nextInt(256));
        }
        opacity = gamesOpacity = 0f;
        gsm.setState(28);
    }
    
    public void update() {
        count++;
        if(opacity != 1f) {
            opacity = (float) (1.0 / 90.0 * count);
        }
        if(gamesOpacity != 1f) {
            gamesOpacity = (float) (1.0 / 180.0 * count);
        }
        if(count == 38 * 18) {
            count = 0;
        }
    }
    
    public void paint(Graphics2D g) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g.setColor(Color.pink);
        g.fillRect(0, 0, getWidth(), getHeight());
        double y = 0;
        for(int i = 0; i < 4; i++) {
            y = getHeight() / 5.0 * (i + 1);
            g.translate(i % 2 == 0 ? count : -count, y);
            for(int i1 = 0; i1 < 18; i1++) {
                g.setColor(colors[i * 20 + i1 + 1]);
                double x = i1 * 38;
                if(x + g.getTransform().getTranslateX() > getWidth() - 38) {
                    g.translate(x, -19);
                    g.fill(GameResource.getStarPath(38, 38));
                    g.translate(-x, 19);
                    x -= getWidth();
                }
                else if(x + g.getTransform().getTranslateX() < 0) {
                    g.translate(x, -19);
                    g.fill(GameResource.getStarPath(38, 38));
                    g.translate(-x, 19);
                    x += getWidth();
                }
                g.translate(x, -19);
                g.fill(GameResource.getStarPath(38, 38));
                g.translate(-x, 19);
            }
            g.translate(i % 2 == 0 ? -count : count, -y);
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, gamesOpacity));
        g.setColor(Color.black);
        Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(8f));
        g.drawRect((int) (getWidth() / 8.0) - 25, getHeight() / 4, getWidth() / 2 - getWidth() / 8, (int) (getHeight() * 0.5));
        g.drawRect((int) (getWidth() / 2.0) + 25, getHeight() / 4, getWidth() / 2 - getWidth() / 8, (int) (getHeight() * 0.5));
        g.setStroke(stroke);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}