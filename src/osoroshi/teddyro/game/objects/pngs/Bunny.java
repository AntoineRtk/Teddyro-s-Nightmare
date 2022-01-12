package osoroshi.teddyro.game.objects.pngs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class Bunny extends MapElement {
    
    private ArrayList<BufferedImage[]> animations;
    private BufferedImage[] textsImage;
    private int count = 0;
    private long time;
    private int[] pos;
    private boolean started = false, talking = false;
    
    public Bunny(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        animations = GameResource.getBunnySprites();
        width = 23;
        height = 15;
        fallingSpeed = 0.6;
        maxFallingSpeed = 12;
        String[] texts = params.split("/");
        pos = new int[texts.length];
        textsImage = new BufferedImage[texts.length];
        for(int i = 0; i < params.split("/").length; i++) {
            pos[i] = Integer.parseInt(texts[i].split("-")[0]);
            String text = texts[i].substring((pos[i]+"-").length());
            if(text != null) {
                textsImage[i] = new BufferedImage(150, 1, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = textsImage[i].createGraphics();
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.black);
                int xb = 0, yb = 15, h;
                for(int i1 = 0; i1 < text.split("-").length; i1++) {
                    TextLayout txt, current;
                    try {
                        txt = new TextLayout(text.split("-")[i1-1], g.getFont(), g.getFontRenderContext());
                        current = new TextLayout(text.split("-")[i1], g.getFont(), g.getFontRenderContext());
                        int w = (int) txt.getBounds().getWidth();
                        xb += w + 15;
                        if(text.split("-")[i1 - 1].equals("<nl>")) xb -= (w + 15);
                        if(xb + (int) current.getBounds().getWidth() >= 150) {
                            xb = 0;
                            yb += 5 + (int) txt.getBounds().getHeight();
                        }
                        if(text.split("-")[i1].equals("<nl>")) {
                            xb = 0;
                            yb += 5 + (int) txt.getBounds().getHeight();
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) { xb = 0; }
                    if(text.split("-")[i1].equals("<nl>")) continue;
                    g.drawString(text.split("-")[i1], xb, yb);
                }
                h = yb;
                yb = 15;
                textsImage[i] = new BufferedImage(150, h, BufferedImage.TYPE_INT_ARGB);
                g = textsImage[i].createGraphics();
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.black);
                for(int i1 = 0; i1 < text.split("-").length; i1++) {
                    TextLayout txt, current;
                    try {
                        txt = new TextLayout(text.split("-")[i1-1], g.getFont(), g.getFontRenderContext());
                        current = new TextLayout(text.split("-")[i1], g.getFont(), g.getFontRenderContext());
                        int w = (int) txt.getBounds().getWidth();
                        xb += w + 15;
                        if(text.split("-")[i1 - 1].equals("<nl>")) xb -= (w + 15);
                        if(xb + (int) current.getBounds().getWidth() >= 150) {
                            xb = 0;
                            yb += 5 + (int) txt.getBounds().getHeight();
                        }
                        if(text.split("-")[i1].equals("<nl>")) {
                            xb = 0;
                            yb += 5 + (int) txt.getBounds().getHeight();
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) { xb = 0; }
                    if(text.split("-")[i1].equals("<nl>")) continue;
                    g.drawString(text.split("-")[i1], xb, yb);
                }
            }
        }
        moveSpeed = 9.75;
        maxMoveSpeed = 9.75;
        stopSpeed = 1.75;
        time = System.currentTimeMillis();
    }
    
    public void update() {
        setDirX(0);
        if(talking) {
            animation.setFrames(animations.get(0));
            animation.setFrame(1);
            animation.setDelay(-1);
            setFlipped(tileMap.hero.getX() + tileMap.hero.width / 2 > getX() + width / 2);
            if(System.currentTimeMillis() - time >= 3000) {
                if(count + 1 < textsImage.length) {
                    count++;
                    talking = false;
                }
            }
        }
        else if(!talking) {
            setFlipped(pos[count] > getX() + width);
            animation.setFrames(animations.get(0));
            animation.setDelay(3);
            if(!started) {
                animation.setFrame(1);
                animation.setDelay(-1);
                setFlipped(tileMap.hero.getX() + tileMap.hero.width / 2 > getX() + width / 2);
            }
            if(started) {
                double dist = Math.abs(pos[count] - getX());
                if(dist <= 9.75) {
                    setX(pos[count]);
                    dx = 0;
                    talking = true;
                    time = System.currentTimeMillis();
                    animation.setFrames(animations.get(0));
                    animation.setFrame(1);
                    animation.setDelay(-1);
                    setFlipped(tileMap.hero.getX() + tileMap.hero.width / 2 > getX() + width / 2);
                }
                else {
                    if(animation.getFrameIndex() == 0) {
                        setDirX((pos[count] > getX()) ? 1 : -1);
                    }
                }
            }
        }
        if(tileMap.hero.intersects(this)) {
            started = true;
        }
        super.update();
        super.checkCollisions();
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(!talking) return;
        g.setColor(Color.white);
        g.fillRoundRect((int) getX() + width / 2 - 150 / 2 - tileMap.getX(), (int) getY() - textsImage[count].getHeight() - 30 - tileMap.getY(), 150, textsImage[count].getHeight(), 25, 25);
        g.drawImage(textsImage[count], (int) getX() + width / 2 - 150 / 2 - tileMap.getX(), (int) getY() - textsImage[count].getHeight() - 30 - tileMap.getY(), null);
    }
}