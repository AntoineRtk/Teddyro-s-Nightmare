package osoroshi.teddyro.game.objects.pngs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class PNG extends MapElement {
    
    private ArrayList<BufferedImage[]> animations;
    private BufferedImage textImage;
    private boolean noText = true;
    
    public PNG(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        fallingSpeed = 0.7;
        maxFallingSpeed = 12.5;
        textImage = new BufferedImage(150, 1, BufferedImage.TYPE_INT_ARGB);
        animations = GameResource.getPNG(Integer.parseInt(params.substring(0, params.indexOf("-"))));
        String t = "";
        try {
            t = URLDecoder.decode(params.substring(params.indexOf("-") + 1), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            JOptionPane.showMessageDialog(null, "Error decoding UTF-8. The text will be encrypted in UTF-8.", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
            t = params.substring(params.indexOf("-") + 1);
        }
        width = animations.get(0)[0].getWidth();
        height = animations.get(0)[0].getHeight();
        if(t != null) {
            Graphics2D g = textImage.createGraphics();
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.black);
            int xb = 0, yb = 15, h;
            noText = t.length() == 0;
            for(int i = 0; i < t.split("-").length; i++) {
                TextLayout txt, current;
                try {
                    txt = new TextLayout(t.split("-")[i-1], g.getFont(), g.getFontRenderContext());
                    current = new TextLayout(t.split("-")[i], g.getFont(), g.getFontRenderContext());
                    int w = (int) txt.getBounds().getWidth();
                    xb += w + 15;
                    if(t.split("-")[i - 1].equals("<nl>")) xb -= (w + 15);
                    if(xb + (int) current.getBounds().getWidth() >= 150) {
                        xb = 0;
                        yb += 5 + (int) txt.getBounds().getHeight();
                    }
                    if(t.split("-")[i].equals("<nl>")) {
                        xb = 0;
                        yb += 5 + (int) txt.getBounds().getHeight();
                    }
                } catch (ArrayIndexOutOfBoundsException ex) { xb = 0; }
                if(t.split("-")[i].equals("<nl>")) continue;
                g.drawString(t.split("-")[i], xb, yb);
            }
            h = yb;
            yb = 15;
            textImage = new BufferedImage(150, h, BufferedImage.TYPE_INT_ARGB);
            g = textImage.createGraphics();
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.black);
            for(int i = 0; i < t.split("-").length; i++) {
                TextLayout txt, current;
                try {
                    txt = new TextLayout(t.split("-")[i-1], g.getFont(), g.getFontRenderContext());
                    current = new TextLayout(t.split("-")[i], g.getFont(), g.getFontRenderContext());
                    int w = (int) txt.getBounds().getWidth();
                    xb += w + 15;
                    if(t.split("-")[i - 1].equals("<nl>")) xb -= (w + 15);
                    if(xb + (int) current.getBounds().getWidth() >= 150) {
                        xb = 0;
                        yb += 5 + (int) txt.getBounds().getHeight();
                    }
                    if(t.split("-")[i].equals("<nl>")) {
                        xb = 0;
                        yb += 5 + (int) txt.getBounds().getHeight();
                    }
                } catch (ArrayIndexOutOfBoundsException ex) { xb = 0; }
                if(t.split("-")[i].equals("<nl>")) continue;
                g.drawString(t.split("-")[i], xb, yb);
            }
        }
    }
    
    public void update() {
        super.update();
        super.checkCollisions();
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(tileMap.hero.intersects(new Rectangle((int) getX(), (int) getY(), animations.get(0)[0].getWidth(), animations.get(0)[0].getHeight()))) {
            if(!noText) {
                g.setColor(Color.white);
                g.fillRoundRect((int) getX() + width / 2 - 150 / 2 - tileMap.getX(), (int) getY() - textImage.getHeight() - 30 - tileMap.getY(), 150, textImage.getHeight(), 25, 25);
                g.drawImage(textImage, (int) getX() + width / 2 - 150 / 2 - tileMap.getX(), (int) getY() - textImage.getHeight() - 30 - tileMap.getY(), null);
            }
            animation.setFrames(animations.get(0));
            animation.setDelay(9);
            int lastHeight = height;
            width = animations.get(0)[0].getWidth();
            height = animations.get(0)[0].getHeight();
            y -= height - lastHeight;
        }
        else {
            setFlipped(tileMap.hero.getX() + tileMap.hero.width / 2 > getX() + width / 2);
            animation.setFrames(animations.get(1));
            animation.setDelay(9);
            int lastHeight = height;
            width = animations.get(1)[0].getWidth();
            height = animations.get(1)[0].getHeight();
            y -= height - lastHeight;
        }
    }
}