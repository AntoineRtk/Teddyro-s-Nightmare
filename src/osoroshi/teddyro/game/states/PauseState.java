package osoroshi.teddyro.game.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class PauseState extends GameState {
    
    private BufferedImage image;
    private int state = -1, index, dir, xg, count;
    private double x;
    private ArrayList<String> texts = new ArrayList<>();
    private long time;
    private boolean exit = false, e, m1 = false, m2 = true;
    private Color color;
    
    public PauseState(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        JukeBox.stopSFX();
        image = getScreen();
        x = getWidth() + 551.4375;
        xg = 0;
        exit = false;
        texts.clear();
        texts.add("Reprendre");
        texts.add("Afficher/Cacher FPS");
        texts.add("Activer/Désactiver le son");
        texts.add("Retour au menu");
        if(count >= 3) {
            texts.add("Afficher/Ne pas afficher les contours");
            texts.add("Afficher/Ne pas afficher emplacements tuiles");
        }
        do {
        color = new Color(new java.util.Random().nextInt(256), new java.util.Random().nextInt(256), new java.util.Random().nextInt(256));
        } while(color.getRed() > 230 && color.getGreen() > 230 && color.getBlue() < 25);
        m1 = false;
        m2 = false;
    }
    
    public void update() {
        if(!exit) {
            x -= (getWidth() + 551.4375) / 30.0;
            if(x < getWidth() / 2) {
                x = getWidth() / 2;
            }
        }
        xg++;
        if(Controler.isUp()) {
            dir = -1;
        }
        else if(Controler.isDown()) {
            dir = 1;
        }
        else {
            dir = 0;
        }
        e = Controler.isEnter();
        if(exit) {
            x -= 30.0 * (x + getWidth() / 2.0) / (getWidth() / 2.0);
        }
        if(System.currentTimeMillis() - time > 50) {
            if(Controler.isEscape() && x == getWidth() / 2) {
                e = true;
                index = 0;
            }
            time = System.currentTimeMillis();
            if(dir == -1) {
                index--;
            }
            else if(dir == 1) {
                index++;
            }
            if(index > texts.size() - 1) {
                index = texts.size() - 1;
            }
            else if(index < 0) {
                index = 0;
            }
            if(e) {
                if(index == 0) {
                    gsm.resume(state);
                }
                if(index == 1) {
                    gsm.displayFPS();
                }
                if(index == 2) {
                    JukeBox.mute();
                }
                if(index == 3) {
                    exit = true;
                }
                if(index == 4 && !m1) {
                    GameResource.displayOutlines();
                    m1 = true;
                }
                if(index == 5 && !m2) {
                    GameResource.displayTilesLocations();
                    m2 = true;
                }
            }
        }
    }
    
    public void paint(Graphics2D g) {
        Composite gc = g.getComposite();
        if(exit) {
            float opacity = (float) ((1.0 / (getWidth() / 2 + 542)) * x);
            if(opacity < 0) {
                gsm.setState(1);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
            }
            else {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            }
        }
        Composite c = g.getComposite();
        if(!exit) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        g.drawImage(image, 0, 0, null);
        g.setComposite(c);
        g.setFont(new Font("Arial", Font.PLAIN, texts.size() == 4 ? 50 : 35));
        int h = 0;
        for(int i = 0; i < texts.size(); i++) {
            TextLayout txt = new TextLayout(texts.get(i), g.getFont(), g.getFontRenderContext());
            h += (int) txt.getBounds().getHeight();
        }
        for(int i = 0; i < texts.size(); i++) {
            TextLayout txt = new TextLayout(texts.get(i), g.getFont(), g.getFontRenderContext());
            g.setColor(Color.yellow);
            if(index == i) g.setPaint(new GradientPaint(xg, 0, Color.yellow, xg + 10, 0, color, true));
            g.drawString(texts.get(i), (int) (x - txt.getBounds().getWidth() / 2), getHeight() / 2 - h / 2 + (h / texts.size()) * i + (int) txt.getBounds().getHeight() / 2);
            if(index == i) {
                g.setColor(Color.red);
                g.drawLine((int) (x - txt.getBounds().getWidth() / 2), getHeight() / 2 - h / 2 + (h / texts.size()) * i + (int) txt.getBounds().getHeight() / 2, (int) (x + txt.getBounds().getWidth() / 2), getHeight() / 2 - h / 2 + (h / texts.size()) * i + (int) txt.getBounds().getHeight() / 2);
            }
        }
        g.setComposite(gc);
    }
    
    public void keyReleased(int keyCode) {
        if(keyCode == KeyEvent.VK_A) {
            count++;
            if(count == 3) {
                texts.add("Afficher/Ne pas afficher les contours");
                texts.add("Afficher/Ne pas afficher emplacements tuiles");
            }
        }
    }
    
    public void setState(int state) {
        this.state = state;
    }
}