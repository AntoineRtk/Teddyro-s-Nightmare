package osoroshi.teddyro.game.world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.utils.GameResource;

public class LevelEnterAnimation {
    
    private LevelSelector ls;
    private BufferedImage screen;
    private int lvIndex = 0, index = 0, count;
    private boolean running = false, screenshot = false, timerStarted = false;
    private long currentTime = System.currentTimeMillis();
    private Color color;
    
    public LevelEnterAnimation(LevelSelector ls) {
        this.ls = ls;
    }
    
    public void start(int index, int lvIndex, int world) {
        this.index = index;
        this.lvIndex = lvIndex;
        running = true;
        screenshot = true;
        timerStarted = false;
    }
    
    public void stop() {
        running = false;
        ls.setState(index);
    }
    
    public void update() {
        if(count == 30) stop();
        count++;
        if(count > 30) count = 30;
        if(timerStarted && System.currentTimeMillis() - currentTime > 3000) {
            stop();
        }
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public void paint(Graphics2D g) {
        if(screenshot) {
            screenshot = false;
            screen = ls.getScreen();
            int cr = 0, cg = 0, cb = 0;
            for(int h = 0; h < screen.getHeight(); h++) {
                for(int w = 0; w < screen.getWidth(); w++) {
                    Color color = new Color(screen.getRGB(w, h));
                    cr += color.getRed();
                    cg += color.getGreen();
                    cb += color.getBlue();
                }
            }
            this.color = new Color(cr / (screen.getWidth() * screen.getHeight()), cg / (screen.getWidth() * screen.getHeight()), cb / (screen.getWidth() * screen.getHeight()));
        }
        g.setColor(color);
        g.fillRect(0, 0, ls.getWidth(), ls.getHeight());
        AffineTransform af = g.getTransform();
        g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(360.0 / 30.0 * count), ls.getWidth() / 2.0, ls.getHeight() / 2.0));
        try {
            g.drawImage(screen.getSubimage((int) ((ls.getLevelX() - 30) / 30.0 * count), (int) ((ls.getLevelY() - 30) / 30.0 * count), ls.getWidth() - (int) ((ls.getWidth() - 120) / 30.0 * count), ls.getHeight() - (int) ((ls.getHeight() - 120) / 30.0 * count)), 0, 0, ls.getWidth(), ls.getHeight(), null);
        } catch(java.awt.image.RasterFormatException e) {}
        g.setTransform(af);
        g.setFont(new Font("Verdana", Font.PLAIN, 50));
        TextLayout txt = new TextLayout("Niveau "+lvIndex, g.getFont(), g.getFontRenderContext());
        g.setColor(Color.black);
        g.drawString("Niveau "+(lvIndex  + 1), (ls.getWidth() - (int) txt.getBounds().getWidth()) / 2, (int) - txt.getBounds().getY() + 15);
    }
}