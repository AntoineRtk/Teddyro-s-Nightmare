package osoroshi.teddyro.game.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Intro extends GameState {
    
    private int transparency, count, x, rotation;
    private double color;
    
    public Intro(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        JukeBox.play("shootingstars");
        transparency = 0;
        count = 0;
        rotation = 0;
        color = 0;
    }
    
    public void update() {
        x++;
        color += 4.25;
        if(color > 255) {
            color = 255;
        }
        count++;
        transparency = (int) (255.0 / 90.0 * count);
        if(transparency > 255) {
            transparency = 255;
        }
        if(count >= 360) {
            color = 255.0 - 255.0 / 90.0 * (count - 360);
            transparency = 255 - (int) (255.0 / 90.0 * (count - 360));
            if(count > 450) {
                color = 0;
                transparency = 0;
            }
        }
    }
    
    public void paint(Graphics2D g) {
        TextLayout txt;
        g.setColor(new Color((int) color, (int) color, (int) color));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(0, 0, 255, transparency));
        g.setFont(new Font("Chaparral Pro Light", Font.ITALIC, 50));
        txt = new TextLayout("A game by Osoroshi", g.getFont(), g.getFontRenderContext());
        g.drawString("A game by Osoroshi", (int) -txt.getBounds().getX(), (int) -txt.getBounds().getY());
        g.setFont(new Font("MV Boli", Font.PLAIN, 60));
        txt = new TextLayout("Teddyro's Nightmare", g.getFont(), g.getFontRenderContext());
        g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rotation), getWidth() / 2, getHeight() / 2));
        drawTitle(g, txt);
        //((SceneManager) gsm.getState()).setScene(0);
        if(count == 480) {
            gsm.setState(1);
        }
    }
    
    public void drawTitle(Graphics2D g, TextLayout txt) {
        double i1 = 2;
        float t = 1f;
        if(count > 240) {
            t = 1.0f - (float) (1.0 / 30.0 * (count - 240));
            if(t <= 0) {
                t = 0;
                if(count > 270) i1 = 2.0 - (1.0 / 30.0 * (count - 270));
                if(i1 < 0) i1 = 0;
            }
        }
        BufferedImage title = new BufferedImage((int) (txt.getBounds().getWidth() + 2), (int) (txt.getBounds().getHeight() + 2), BufferedImage.TYPE_INT_ARGB);
        Graphics2D titleGraphics = title.createGraphics();
        titleGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        titleGraphics.setFont(g.getFont());
        titleGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, t));
        titleGraphics.setPaint(new GradientPaint(x, 0, Color.blue, x + 10, 0, Color.cyan, true));
        titleGraphics.drawString("Teddyro's Nightmare", - (int) txt.getBounds().getX(), - (int) txt.getBounds().getY());
        titleGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1f));
        titleGraphics.setPaint(new TexturePaint(GameResource.getLogo(), new Rectangle(count * 10 % 200, 0, GameResource.getLogo().getWidth(), GameResource.getLogo().getHeight())));
        titleGraphics.drawString("Teddyro's Nightmare", (int) (-txt.getBounds().getX() + i1), (int) (-txt.getBounds().getY() + i1));
        titleGraphics.dispose();
        g.drawImage(title, getWidth() / 2 - title.getWidth() / 2, getHeight() / 2 - title.getHeight() / 2, null);
    }
}