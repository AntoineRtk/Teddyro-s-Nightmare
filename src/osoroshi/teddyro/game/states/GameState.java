package osoroshi.teddyro.game.states;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class GameState {
    
    public GameStateManager gsm;
    
    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }
    
    public abstract void init();
    
    public abstract void update();
    
    public abstract void paint(Graphics2D g);
    
    public void keyPressed(int keyCode) {}
    
    public void keyReleased(int keyCode) {}
    
    public int getWidth() {
        return gsm.getWidth();
    }
    
    public int getHeight() {
        return gsm.getHeight();
    }
    
    public void setState(int state) {
        gsm.setState(state);
    }
    
    public BufferedImage getScreen() {
        return gsm.getScreen();
    }
    
    public int getStateIndex() {
        return gsm.getStateIndex();
    }
}