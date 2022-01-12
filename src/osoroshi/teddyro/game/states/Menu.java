package osoroshi.teddyro.game.states;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;
import osoroshi.teddyro.game.utils.SaveManager;

public class Menu extends GameState {
    
    private GameMenu currentMenu;
    private int index = 0, omberX = 3, omberY = 3, count = 0, omberCount, clickCount;
    private double rotation = 0;
    private boolean reverse = false, reverseOmber, initSave = false;
    private GameLevel gl;
    private float opacity = 1.0f;
    
    public Menu(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        SaveManager.removeInitialization();
        count = 0;
        initSave = false;
        opacity = 1.0f;
        index = 0;
        gl = new GameLevel(this, -1);
        gl.setShowHUD(false);
        gl.disableAcessToMenu();
        if(new java.util.Random().nextBoolean() && false) {
            gl.load("menu0", true);
            JukeBox.play(4);
        }
        else {
            gl.load("menu1", true);
            JukeBox.setLooping("menu", -1, -1);
        }
        gl.setHeroCanAction(false);
        gl.setHeroDirX(1);
        gl.setCameraTween(1);
        gl.setHeroMaxMoveSpeed(2);
        GameMenu menu = new GameMenu(gsm, this, "");
        GameMenu play = new GameMenu(gsm, this, "Jouer");
        play.addMenu(new GameMenuAction(gsm, this, "Aventure", 0, null));
        play.addMenu(new GameMenuAction(gsm, this, "Mini-jeux", 19, null));
        play.addMenu(new GameMenuAction(gsm, this, "Retour", -1, menu));
        menu.addMenu(play);
        menu.addMenu(new GameMenuAction(gsm, this, "Carte personnalisée", 2, null));
        GameMenu options = new GameMenu(gsm, this, "Options");
        GameMenu size = new GameMenu(gsm, this, "Taille de la fenêtre");
        size.addMenu(new GameMenuAction(gsm, this, "Fenêtré", 3, null));
        size.addMenu(new GameMenuAction(gsm, this, "Plein écran", 4, null));
        size.addMenu(new GameMenuAction(gsm, this, "Retour", -1, options));
        GameMenu sound = new GameMenu(gsm, this, "Son");
        sound.addMenu(new GameMenuAction(gsm, this, "Son actif", 5, null));
        sound.addMenu(new GameMenuAction(gsm, this, "Son non-actif", 6, null));
        sound.addMenu(new GameMenuAction(gsm, this, "Retour", -1, options));
        GameMenu fps = new GameMenu(gsm, this, "Vitesse du jeu");
        fps.addMenu(new GameMenuAction(gsm, this, "25 FPS", 7, null));
        fps.addMenu(new GameMenuAction(gsm, this, "50 FPS", 8, null));
        fps.addMenu(new GameMenuAction(gsm, this, "60 FPS", 9, null));
        fps.addMenu(new GameMenuAction(gsm, this, "100 FPS", 10, null));
        fps.addMenu(new GameMenuAction(gsm, this, "Maximum", 18, null));
        fps.addMenu(new GameMenuAction(gsm, this, "Retour", -1, options));
        GameMenu cursor = new GameMenu(gsm, this, "Curseur");
        cursor.addMenu(new GameMenuAction(gsm, this, "Curseur 1", 14, null));
        cursor.addMenu(new GameMenuAction(gsm, this, "Curseur 2", 15, null));
        cursor.addMenu(new GameMenuAction(gsm, this, "Curseur 3", 16, null));
        cursor.addMenu(new GameMenuAction(gsm, this, "Retour", -1, options));
        GameMenu preference = new GameMenu(gsm, this, "Préférences");
        preference.addMenu(new GameMenuAction(gsm, this, "Tracer formes convexes lors des attaques", 11, null));
        preference.addMenu(new GameMenuAction(gsm, this, "Pas tracer formes convexes lors des attaques", 12, null));
        preference.addMenu(new GameMenuAction(gsm, this, "Retour", -1, options));
        options.addMenu(size);
        options.addMenu(sound);
        options.addMenu(fps);
        options.addMenu(cursor);
        options.addMenu(preference);
        options.addMenu(new GameMenuAction(gsm, this, "Retour", -1, menu));
        menu.addMenu(options);
        menu.addMenu(new GameMenuAction(gsm, this, "Dossier des captures d'écran", 17, null));
        menu.addMenu(new GameMenuAction(gsm, this, "Crédits", 13, null));
        menu.addMenu(new GameMenuAction(gsm, this, "Quitter", 1, null));
        currentMenu = menu;
    }
    
    public void start() {
        if(opacity == 1.0f) opacity = 0.9f;
    }
    
    public void update() {
        count++;
        omberCount++;
        clickCount++;
        if(initSave && !SaveManager.isInitialized()) {
            SaveManager.init();
        }
        if(!SaveManager.isInitialized()) return;
        gl.update();
        if(!reverse) {
            rotation += 2;
            if(rotation >= 22.5) {
                reverse = true;
                rotation = 22.5;
            }
        }
        else {
            rotation -= 2;
            if(rotation <= -22.5) {
                reverse = false;
                rotation = -22.5;
            }
        }
        if(omberCount > 3) {
            if(!reverseOmber) {
                omberX += 1;
                omberY += 1;
                if(omberX >= 3) {
                    reverseOmber = true;
                }
            }
            else {
                omberX -= 1;
                omberY -= 1;
                if(omberY <= -3) {
                    reverseOmber = false;
                }
            }
            omberCount = 0;
        }
        if(opacity != 1.0f) {
            opacity -= 0.1f;
            if(opacity < 0.0f) {
                opacity = 0.0f;
            }
        }
        if(clickCount < 4) return;
        if(Controler.isUp()) {
            JukeBox.play("menuaction");
            index--;
        clickCount = 0;
            if(index < 0) {
                index = currentMenu.size()-1;
            }
        }
        if(Controler.isDown()) {
            JukeBox.play("menuaction");
            clickCount = 0;
            index++;
            if(index >= currentMenu.size()) {
                index = 0;
            }
        }
        if(Controler.isEnter() && opacity == 1.0f) {
            Controler.setEnter(false);
            currentMenu.doAction(index);
            clickCount = 0;
        }
    }
    
    public void setMenu(GameMenu menu) {
        this.currentMenu = menu;
        index = 0;
    }
    
    public void paint(Graphics2D g) {
        Composite c = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        gl.paint(g);
        AffineTransform af = g.getTransform();
        drawMenu(currentMenu, g);
        g.setTransform(af);
        g.setComposite(c);
        if(!SaveManager.isInitialized()) {
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(new Font("Gabriola", Font.BOLD, 60));
            g.setColor(Color.black);
            TextLayout txt = new TextLayout("Initialisation de la sauvegarde...", g.getFont(), g.getFontRenderContext());
            g.drawString("Initialisation de la sauvegarde...", getWidth() / 2 - (int) txt.getBounds().getWidth() / 2 - (int) txt.getBounds().getX(), getHeight() / 2 - (int) txt.getBounds().getHeight() / 2 - (int) txt.getBounds().getY());
            initSave = true;
        }
        if(opacity == 0.0f) {
            //if(SaveManager.isFinish(0)) gsm.setState(7);
            //else gsm.setState(13);
            gsm.setState(2);
        }
    }
    
    public void drawMenu(GameMenu gm, Graphics2D g) {
        g.setFont(new Font("Gabriola", Font.PLAIN, 105));
        TextLayout txt = new TextLayout("Teddyro's Nightmare", g.getFont(), g.getFontRenderContext());
        g.setPaint(new TexturePaint(GameResource.getLogo(), new Rectangle(count * 10 % 200 - 1, 0, GameResource.getLogo().getWidth(), (int) txt.getBounds().getHeight())));
        g.drawString("Teddyro's Nightmare", (int) (getWidth() / 2.0 -txt.getBounds().getX() - txt.getBounds().getWidth() / 2.0), (int) (-txt.getBounds().getY()));
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        double y = getHeight() / 4.0;
        for(int i = 0; i < gm.size(); i++) {
            g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rotation), getWidth() / 2.0, y + gm.get(i).getHeight(g) / 2.0));
            g.setColor(Color.red);
            g.drawString(gm.get(i).getText(), (int) (getWidth() / 2.0 - gm.get(i).getWidth(g) / 2.0 - gm.get(i).getX(g)), (int) Math.ceil(y - gm.get(i).getY(g)));
            g.setColor(Color.gray);
            g.drawString(gm.get(i).getText(), (int) (getWidth() / 2.0 - gm.get(i).getWidth(g) / 2.0 - gm.get(i).getX(g) + omberX), (int) (y - gm.get(i).getY(g) + omberY));
            if(i == index) {
                Rectangle2D rectangle = new Rectangle2D.Double(-5 + getWidth() / 2.0 - gm.get(i).getWidth(g) / 2.0, -5 + y, 10 + gm.get(i).getWidth(g), 10 + gm.get(i).getHeight(g));
                g.setColor(Color.orange);
                Stroke s = g.getStroke();
                g.setStroke(new BasicStroke(5));
                g.draw(rectangle);
                g.setStroke(s);
            }
            y += getHeight() * 0.75 / gm.size();
        }
    }
}