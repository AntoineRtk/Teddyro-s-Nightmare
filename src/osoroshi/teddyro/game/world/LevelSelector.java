package osoroshi.teddyro.game.world;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.states.GameState;
import osoroshi.teddyro.game.states.GameStateManager;
import osoroshi.teddyro.game.states.PauseState;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.JukeBox;

public abstract class LevelSelector extends GameState {
    
    public LevelSelectorCharacter lsc;
    public ArrayList<GameLevelSelector> gls = new ArrayList<>();
    public BufferedImage bg;
    private int fontValue = 30, pauseCount, enteringWorldCount;
    private boolean reverse = false;
    private LevelEnterAnimation lea;
    
    public LevelSelector(GameStateManager gsm) {
        super(gsm);
    }
    
    public void addLevel(int[][] informations) {
        this.gls.add(new GameLevelSelector(informations));
        this.lsc.setPosition(this.gls.get(0));
    }
    
    public void init() {
        JukeBox.stopAll();
        gls.clear();
        enteringWorldCount = 0;
        lsc = new LevelSelectorCharacter(this);
        lea = new LevelEnterAnimation(this);
    }
    
    private int rc = new java.util.Random().nextInt(256), gc = new java.util.Random().nextInt(256), bc = new java.util.Random().nextInt(256);
    private boolean rr = new java.util.Random().nextBoolean(), gr = new java.util.Random().nextBoolean(), br = new java.util.Random().nextBoolean();
    
    public void update() {
        if(rr) {
            rc -= new java.util.Random().nextInt(10) + 1;
            if(rc < 0) {
                rc = 0;
                rr = false;
            }
        }
        else {
            rc += new java.util.Random().nextInt(10) + 1;
            if(rc > 255) {
                rc = 255;
                rr = true;
            }
        }
        if(gr) {
            gc -= new java.util.Random().nextInt(10) + 1;
            if(gc < 0) {
                gc = 0;
                gr = false;
            }
        }
        else {
            gc += new java.util.Random().nextInt(10) + 1;
            if(gc > 255) {
                gc = 255;
                gr = true;
            }
        }
        if(br) {
            bc -= new java.util.Random().nextInt(10) + 1;
            if(bc < 0) {
                bc = 0;
                br = false;
            }
        }
        else {
            bc += new java.util.Random().nextInt(10) + 1;
            if(bc > 255) {
                bc = 255;
                br = true;
            }
        }
        for(int i = 0; i < gls.size(); i++) {
            gls.get(i).update();
        }
        if(!lea.isRunning()) {
            lsc.update();
        }
        if(!reverse) {
            fontValue += 1;
            if(fontValue >= 45) {
                fontValue = 45;
                reverse = true;
            }
        }
        else {
            fontValue -= 0.2f;
            if(fontValue <= 30) {
                fontValue = 30;
                reverse = false;
            }
        }
        if(enteringWorldCount != 0) {
            enteringWorldCount++;
            lsc.removeMessage();
            if(enteringWorldCount > 100) {
                gsm.setState(2 + gls.get(lsc.getIndex()).getWorldAccess());
            }
        }
        int index = lsc.getIndex();
        if(index != -1) {
            if(gls.get(index).getWorldAccess() != -1 && enteringWorldCount == 0) {
                enteringWorldCount = 1;
            }
            if(Controler.isEnter() && !lea.isRunning() && enteringWorldCount == 0) {
                lsc.removeMessage();
                boolean canEnter = true;
                for(int i = 0; i < gls.get(index).getLevelToComplete().length; i++) {
                    if(!gls.get(gls.get(index).getLevelToComplete()[i]).isFinish()) {
                        canEnter = false;
                    }
                }
                if(canEnter) {
                    lea.start(gls.get(index).getState(), index, getWorld());
                }
            }
        }
        if(lea.isRunning()) {
            lea.update();
        }
        pauseCount++;
        if(Controler.isEscape() && pauseCount > 30) {
            ((PauseState) gsm.getState(5)).setState(getStateIndex());
            gsm.setState(5);
            pauseCount = 0;
        }
    }
    
    public void paint(Graphics2D g) {
        if(bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
        for(int i = 0; i < gls.size(); i++) {
            if(!gls.get(i).isShown()) continue;
            g.setPaint(new GradientPaint(0, 0, Color.white, 1, 1, Color.blue, true));
            g.setColor(new Color(rc, gc, bc));
            for(int i1 = 0; i1 < gls.get(i).getFrom().length; i1++) {
                g.drawLine(gls.get(gls.get(i).getFrom()[i1]).getX() + 30, gls.get(gls.get(i).getFrom()[i1]).getY() + 30, gls.get(i).getX() + 30, gls.get(i).getY() + 30);
            }
        }
        for(int i = 0; i < gls.size(); i++) {
            boolean c = false;
            for(int i1 = 0; i1 < gls.get(i).getLevelToComplete().length; i1++) {
                if(!gls.get(gls.get(i).getLevelToComplete()[i1]).isFinish()) c = true;
            }
            gls.get(i).setShown(false);
            if(c) continue;
            gls.get(i).setShown(true);
            gls.get(i).paint(g);
            if(gls.get(i).getShape().intersects(lsc.getBounds())) {
                g.setColor(Color.gray);
                //g.fillRoundRect(30, 10, getWidth() - 60, 50, 90, 45);
                g.setColor(Color.cyan);
                g.setFont(new Font("Times New Roman", Font.ITALIC, 30));
                if(gls.get(i).isBoss()) {
                    g.setColor(Color.red);
                    g.setFont(new Font("Times New Roman", Font.ITALIC, fontValue));
                }
                String t = "Niveau "+(i+1);
                if(gls.get(i).isFinalAccess()) {
                    t = "L'antre finale";
                    g.setPaint(new GradientPaint(0, 0, Color.black, 0, 50, new Color(64, 0, 128), true));
                }
                if(gls.get(i).getWorldAccess() == -1) {
                    TextLayout txt = new TextLayout(t, g.getFont(), g.getFontRenderContext());
                    if(!lea.isRunning()) g.drawString(t, (getWidth() - (int) txt.getBounds().getWidth()) / 2, 10 + (int) txt.getBounds().getHeight() + 10);
                }
            }
        }
        lsc.paint(g);
        if(lea.isRunning()) {
            lea.paint(g);
        }
        if(enteringWorldCount != 0) {
            g.setColor(new Color(255, 255, 255, (int) (2.55 * enteringWorldCount)));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    public void setBonus(int level) {
        this.gls.get(level).setBonus(true);
    }
    
    public void setBoss(int level) {
        this.gls.get(level).setBoss(true);
    }
    
    public void setFinalAccess(int level) {
        this.gls.get(level).setFinalAccess(true);
    }
    
    public void setAccessToWorld(int level, int world) {
        this.gls.get(level).setAccessToWorld(world);
    }
    
    public int getLevelX() {
        return gls.get(lsc.getIndex()).getX();
    }
    
    public int getLevelY() {
        return gls.get(lsc.getIndex()).getY();
    }
    
    public abstract int getWorld();
}