package osoroshi.teddyro.game.world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;

public class LevelSelectorCharacter {
    
    private BufferedImage image;
    private double x, y;
    private int index = 0, toX, toY, lastIndex, nextIndex, count = 0, maxCount;
    private long currentTime = System.currentTimeMillis();
    private LevelSelector ls;
    
    public LevelSelectorCharacter(LevelSelector ls) {
        image = GameResource.getHeroPortrait();
        this.ls = ls;
    }
    
    public void update() {
        if(index != -1) {
            count = 0;
            int[] dirs = ls.gls.get(index).getDirs();
            if(Controler.isLeft() && index != -1) {
                for(int i = 0; i < dirs.length; i++) {
                    if(i % 2 == 0 && dirs[i] == -1 && ls.gls.get(dirs[i + 1]).isShown()) {
                        toX = ls.gls.get(dirs[i + 1]).getX();
                        toY = ls.gls.get(dirs[i + 1]).getY();
                        lastIndex = index;
                        nextIndex = dirs[i + 1];
                        index = -1;
                    }
                }
            }
            if(Controler.isRight() && index != -1) {
                for(int i = 0; i < dirs.length; i++) {
                    if(i % 2 == 0 && dirs[i] == 1 && ls.gls.get(dirs[i + 1]).isShown()) {
                        toX = ls.gls.get(dirs[i + 1]).getX();
                        toY = ls.gls.get(dirs[i + 1]).getY();
                        lastIndex = index;
                        nextIndex = dirs[i + 1];
                        index = -1;
                    }
                }
            }
            if(Controler.isUp() && index != -1) {
                for(int i = 0; i < dirs.length; i++) {
                    if(i % 2 == 0 && dirs[i] == -2 && ls.gls.get(dirs[i + 1]).isShown()) {
                        toX = ls.gls.get(dirs[i + 1]).getX();
                        toY = ls.gls.get(dirs[i + 1]).getY();
                        lastIndex = index;
                        nextIndex = dirs[i + 1];
                        index = -1;
                    }
                }
            }
            if(Controler.isDown() && index != -1) {
                for(int i = 0; i < dirs.length; i++) {
                    if(i % 2 == 0 && dirs[i] == 2 && ls.gls.get(dirs[i + 1]).isShown()) {
                        toX = ls.gls.get(dirs[i + 1]).getX();
                        toY = ls.gls.get(dirs[i + 1]).getY();
                        lastIndex = index;
                        nextIndex = dirs[i + 1];
                        index = -1;
                    }
                }
            }
        }
        else {
            removeMessage();
            if(lastIndex == -1) return;
            int distx = toX - ls.gls.get(lastIndex).getX(), disty = toY - ls.gls.get(lastIndex).getY();
            maxCount = Math.max(Math.abs(distx), Math.abs(disty)) / 4;
            x += 4.0 / Math.max(Math.abs(distx), Math.abs(disty)) * distx;
            y += 4.0 / Math.max(Math.abs(distx), Math.abs(disty)) * disty;
            count++;
            if(count == maxCount) {
                x = toX + 30 - image.getWidth() / 2.0;
                y = toY + 30 - image.getHeight() / 2.0;
                index = nextIndex;
            }
        }
        removeMessage();
    }
    
    public void removeMessage() {
        currentTime = System.currentTimeMillis();
    }
    
    public void paint(Graphics2D g) {
        g.drawImage(image, (int) x, (int) y, null);
        if(System.currentTimeMillis() - currentTime >= 3000) {
            g.setFont(new Font("Gungsuh", Font.PLAIN, 19));
            g.setColor(Color.cyan);
            TextLayout txt1 = new TextLayout("Appuyez sur les flèches directionnelles pour vous déplacer.", g.getFont(), g.getFontRenderContext());
            TextLayout txt2 = new TextLayout("Appuyez sur \"entrée\" pour accéder à un niveau.", g.getFont(), g.getFontRenderContext());
            g.drawString("Appuyez sur les flèches directionnelles pour vous déplacer.", (ls.getWidth() - (int) txt1.getBounds().getWidth()) / 2, ls.getHeight() / 2 - (int) txt2.getBounds().getHeight() - (int) txt1.getBounds().getHeight());
            g.drawString("Appuyez sur \"entrée\" pour accéder à un niveau.", (ls.getWidth() - (int) txt2.getBounds().getWidth()) / 2, ls.getHeight() / 2 - (int) txt2.getBounds().getHeight());
            g.drawImage(GameResource.getArrows(), (ls.getWidth() - GameResource.getArrows().getWidth()) / 2, ls.getHeight() / 2, null);
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, image.getWidth(), image.getHeight());
    }
    
    public void setPosition(GameLevelSelector gls) {
        this.x = gls.getX() + 30 - image.getWidth() / 2;
        this.y = gls.getY() + 30 - image.getHeight() / 2;
    }
    
    public int getIndex() {
        return index;
    }
}