package osoroshi.teddyro.game.states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.objects.mobs.BlackHoleLauncher;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Prologue extends GameState {
    
    private BufferedImage bookImage, dreamOrNightmareImage;
    private double bookX, xminus, xplus, yminus, yplus;
    private int opacity, y, count = 0, dreamOrNightmareCount = 0;
    private float levelOpacity = 0;
    private long time;
    private boolean inLevel = false, turning = false, reversing = false;
    private GameLevel gl;
    
    public Prologue(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        gl = new GameLevel(this, -1);
        gl.load("prologue", true);
        gl.setHeroClothings(1);
        gl.setShowHUD(false);
        time = System.currentTimeMillis();
        dreamOrNightmareCount = 0;
        try {
            dreamOrNightmareImage = ImageIO.read(getClass().getClassLoader().getResource("osoroshi/teddyro/resources/divers/dreamornightmare.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error reading an element.", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
        }
        reversing = false;
        xminus = xplus = yminus = yplus = count = opacity = 0;
        bookImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bookImage.createGraphics();
        BufferedImage book = GameResource.getBook();
        g.drawImage(GameResource.getBook(), 0, getHeight() / 2 - book.getHeight() / 2, getWidth(), book.getHeight(), null);
        g.setFont(new Font("Courier New", Font.PLAIN, 14));
        g.setColor(Color.black);
        String text = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("osoroshi/teddyro/resources/texts/story.txt")));
            String line = "";
            while((line = br.readLine()) != null) {
                text += line+"\n";
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error reading story file.", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        int xb = 0, yb = 80, part = 2;
        for(int i = 0; i < text.split(" ").length; i++) {
            TextLayout txt, current;
            try {
                txt = new TextLayout(text.split(" ")[i-1], g.getFont(), g.getFontRenderContext());
                current = new TextLayout(text.split(" ")[i], g.getFont(), g.getFontRenderContext());
                int w = (int) txt.getBounds().getWidth();
                xb += w + 15;
                if(text.split(" ")[i - 1].equals("<nl>")) xb -= (w + 15);
                if(xb + (int) current.getBounds().getWidth() >= getWidth() / part - 20) {
                    if(part == 2) {
                        xb = 20;
                    }
                    else {
                        xb = getWidth() / 2 + 20;
                    }
                    yb += 25;
                    if(yb >= getHeight() / 2 + book.getHeight() / 2) {
                        yb = 80;
                        part = 1;
                        xb = getWidth() / 2 + 20;
                    }
                }
                if(text.split(" ")[i].equals("<nl>")) {
                    if(part == 2) {
                        xb = 20;
                    }
                    else {
                        xb = getWidth() / 2 + 20;
                    }
                    yb += 25;
                    if(yb >= getHeight() / 2 + book.getHeight() / 2) {
                        yb = 80;
                        part = 1;
                        xb = getWidth() / 2 + 20;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ex) { xb = 80; }
            if(text.split(" ")[i].equals("<nl>")) continue;
            g.drawString(text.split(" ")[i], xb, yb);
        }
        g.dispose();
        this.y = -getHeight();
    }
    
    public void update() {
        if(inLevel) {
            levelOpacity += 0.05f;
            if(levelOpacity > 1f) {
                levelOpacity = 1.0f;
            }
            if(System.currentTimeMillis() - time > 10000 && count <= 30) {
                xminus += (getWidth() / 2 + 20) / 30.0;
                yminus += (getHeight() / 2 - getHeight() / 4) / 30.0;
                xplus += (getWidth() - (getWidth() / 2 - 40)) / 30.0;
                yplus += (getHeight() / 2) / 30.0;
                count++;
            }
        }
        if(inLevel && levelOpacity == 1.0f) gl.update();
        else {
            opacity += (int) (255.0 / (5000.0 / 25.0));
            if(opacity > 255) {
                opacity = 255;
                if(dreamOrNightmareCount >= 0) {
                    if(dreamOrNightmareCount <= 100 && !reversing) {
                        dreamOrNightmareCount++;
                        if(dreamOrNightmareCount == 101) reversing = true;
                    }
                    else {
                        dreamOrNightmareCount--;
                    }
                    time = System.currentTimeMillis();
                }
                else {
                    y += 15.0 / getHeight() * Math.abs(y);
                    if(y > 0) {
                        y = 0;
                    }
                    if(!inLevel) {
                        JukeBox.setLooping("sadstory", 0, -1);
                    }
                }
            }
        }
        if(System.currentTimeMillis() - time > 15000 && Controler.isEnter() && !turning) {
            turning = true;
            bookX = getWidth() / 2;
        }
        if(turning) {
            bookX -= 2.35;
            if(bookX < 0 && !inLevel) {
                bookX = 0;
                inLevel = true;
                time = System.currentTimeMillis();
                JukeBox.stop("sadstory");
                JukeBox.setLooping("festival", 287700, -1);
            }
            redrawBook();
        }
    }
    
    public void redrawBook() {
        Graphics2D g = bookImage.createGraphics();
        BufferedImage book = GameResource.getBook();
        g.setClip(getWidth() / 2 + (int) bookX, 0, getWidth() / 2 - (int) bookX, getHeight());
        g.drawImage(book.getSubimage(book.getWidth() / 2, 0, getWidth() / 2, book.getHeight()), getWidth() / 2, getHeight() / 2 - book.getHeight() / 2, getWidth() / 2, book.getHeight(), null);
        g.dispose();
    }
    
    public void paint(Graphics2D g) {
        g.setColor(new Color(255, 255, 255, opacity));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(bookImage, 0, y, null);
        if(System.currentTimeMillis() - time > 15000) {
            g.drawImage(GameResource.getEnter(), getWidth() - GameResource.getEnter().getWidth(), getHeight() - GameResource.getEnter().getHeight(), null);
        }
        if(dreamOrNightmareCount > 0) {
            float alpha = 1.0f / 50.0f * dreamOrNightmareCount;
            if(alpha > 1) alpha = 1;
            Composite c = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g.drawImage(dreamOrNightmareImage, 0, getHeight() / 2 - dreamOrNightmareImage.getHeight() / 2, getWidth(), dreamOrNightmareImage.getHeight(), null);
            g.setComposite(c);
        }
        if(inLevel) {
            gl.setShowHUD(false);
            if(count < 30) {
                BufferedImage levelImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = levelImage.createGraphics();
                gl.paint(g2);
                g2.dispose();
                Composite c = g.getComposite();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, levelOpacity));
                g.drawImage(levelImage, getWidth() / 2 + 20 - (int) xminus, getHeight() / 2 - getHeight() / 4 - (int) yminus, getWidth() / 2 - 40 + (int) xplus, getHeight() / 2 + (int) yplus, null);
                g.setComposite(c);
            }
            else {
                g.setColor(Color.black);
                g.fillRect(0, 0, getWidth(), getHeight());
                gl.paint(g);
            }
        }
    }
    
    public void keyPressed(int keyCode) {
        if(!inLevel) return;
        TileMap tileMap = gl.getTileMap();
        try {
            for(int i = 0; i < tileMap.objects[tileMap.getArea()].size(); i++) {
                MapObject mo = tileMap.objects[tileMap.getArea()].get(i);
                if(mo instanceof BlackHoleLauncher) {
                    ((BlackHoleLauncher) mo).keyPressed(keyCode);
                }
            }
        } catch (java.lang.NullPointerException ex) {}
    }
    
    public void keyReleased(int keyCode) {
        if(!inLevel) return;
        TileMap tileMap = gl.getTileMap();
        try {
            for(int i = 0; i < tileMap.objects[tileMap.getArea()].size(); i++) {
                MapObject mo = tileMap.objects[tileMap.getArea()].get(i);
                if(mo instanceof BlackHoleLauncher) {
                    ((BlackHoleLauncher) mo).keyReleased();
                }
            }
        } catch (java.lang.NullPointerException ex) {}
    }
}