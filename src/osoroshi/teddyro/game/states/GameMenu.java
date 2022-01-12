package osoroshi.teddyro.game.states;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.util.ArrayList;

public class GameMenu {
    
    private String text;
    private Menu menu;
    public GameStateManager gsm;
    public ArrayList<GameMenu> menus = new ArrayList<>();
    
    public GameMenu(GameStateManager gsm, Menu menu, String text) {
        this.gsm = gsm;
        this.menu = menu;
        this.text = text;
    }
    
    public void addMenu(GameMenu gm) {
        menus.add(gm);
    }
    
    public String getText() {
        return text;
    }
    
    public int getX(Graphics2D g) {
        return (int) new TextLayout(text, g.getFont(), g.getFontRenderContext()).getBounds().getX();
    }
    
    public int getY(Graphics2D g) {
        return (int) new TextLayout(text, g.getFont(), g.getFontRenderContext()).getBounds().getY();
    }
    
    public int getWidth(Graphics2D g) {
        return (int) new TextLayout(text, g.getFont(), g.getFontRenderContext()).getBounds().getWidth();
    }
    
    public int getHeight(Graphics2D g) {
        return (int) new TextLayout(text, g.getFont(), g.getFontRenderContext()).getBounds().getHeight();
    }
    
    public void doAction(int index) {
        if(menus.get(index) instanceof GameMenuAction) {
            menus.get(index).doAction(0);
        }
        else {
            menu.setMenu(menus.get(index));
        }
    }
    
    public void back(GameMenu gm) {
        menu.setMenu(gm);
    }
    
    public int size() {
        return menus.size();
    }
    
    public GameMenu get(int i) {
        return menus.get(i);
    }
}