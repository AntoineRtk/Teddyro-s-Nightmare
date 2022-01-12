package osoroshi.teddyro.game.states;

import osoroshi.teddyro.game.objects.camera.Camera;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.hud.HUD;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.JukeBox;
import osoroshi.teddyro.game.utils.SaveManager;

public class GameLevel {
    
    private GameState gs;
    private Hero hero;
    private HUD hud;
    private TileMap tileMap;
    private boolean showHUD = true, flash, confused, finish = false, flipped = false, canAcessToMenu = true;
    private Camera camera;
    private long currentFlashTime, flashTime, confusedTime, currentConfusedTime;
    private int pauseCount, finishCount, level;
    private double circleLeftX, circleRightX, circleUpY, circleDownY, xCircleLeftRatio, xCircleRightRatio, yCircleUpRatio, yCircleDownRatio;
    private BufferedImage screen;
    private Color c;
    private ArrayList<MapObject> p = new ArrayList<>();
    
    public GameLevel(GameState gs, int l) {
        //try {
        this.gs = gs;
        this.level = l;
        tileMap = new TileMap(this);
        hero = new Hero(this, tileMap, 32, 32);
        tileMap.setHero(hero);
        hud = new HUD(this, hero);
        camera = new Camera(this, tileMap);
        camera.setMapObject(hero);
            /*byte[] buf = new byte[ 1 ];
            AudioFormat af = new AudioFormat( (float )44100, 8, 1, true, false );
            SourceDataLine sdl = AudioSystem.getSourceDataLine( af );
            sdl = AudioSystem.getSourceDataLine( af );
            sdl.open( af );
            sdl.start();
            for( int i = 0; i < 44800 * 0; i++ ) {
                double angle = i / ( (float )44100 / 440 ) * -3.0;
                buf[0] = (byte )( Math.sin( angle ) * 100 );
                System.out.println(Math.sin(angle) * 100);
                sdl.write(buf, 0, 1 );
            }
            sdl.drain();
            sdl.stop();
            sdl.close();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(GameLevel.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    public void load(String map, boolean in) {
        finish = false;
        JukeBox.stopAll();
        tileMap.loadMap(map, in);
        tileMap.setArea(6-6, 0);
        /*hero.setX(1664 - 45);
        hero.setY(hero.getY() - 32 * 5);*/
    }
    
    public void update() {
        setCameraAllignement(Camera.MID);
        if(finish) {
            circleLeftX += xCircleLeftRatio;
            circleRightX -= xCircleRightRatio;
            circleUpY += yCircleUpRatio;
            circleDownY -= yCircleDownRatio;
            finishCount++;
            if(finishCount == 100) {
                if(getLevel() < 6) {
                    gs.setState(2);
                }
            }
        }
        if(System.currentTimeMillis() - currentFlashTime > flashTime) {
            flash = false;
        }
        pauseCount++;
        if(Controler.isEscape() && canAcessToMenu) {
            ((PauseState) gs.gsm.getState(5)).setState(gs.getStateIndex());
            gs.setState(5);
            pauseCount = 0;
        }
        if(tileMap.getTile(0, 0) != null) {
            tileMap.updatePlatforms();
            hero.update();
            tileMap.updateMonsters();
            tileMap.updateObjects();
            hud.update();
            tileMap.update();
            camera.update();
        }
        if(screen == null && finish) {
            screen = getScreen();
            c = new Color(new java.util.Random().nextInt(256), new java.util.Random().nextInt(256), new java.util.Random().nextInt(256));
        }
    }
    
    public void paint(Graphics2D g) {
        if(tileMap.getTile(0, 0) != null) {
            tileMap.paintBackground(g);
            hero.paint(g);
            if(confused) {
                if(System.currentTimeMillis() - currentConfusedTime <= confusedTime) {
                    tileMap.addRotation(new java.util.Random().nextInt(10));
                }
                else {
                    confused = false;
                }
            }
            else {
                tileMap.setRotation(0);
            }
            tileMap.paint(g);
            for (MapObject p1 : p) {
                p1.paint(g);
            }
        }
        if(flash && System.currentTimeMillis() - currentFlashTime <= flashTime) {
            int alpha = 255;
            double b4 = 4.0 / flashTime * (System.currentTimeMillis() - currentFlashTime);
            if((int) b4 == 0) {
                alpha = (int) (255.0 / (flashTime / 4.0) * (System.currentTimeMillis() - currentFlashTime));
            }
            if((int) b4 == 3) {
                double ratio = flashTime / 4.0 * 3.0;
                double newValue = 255.0 / (flashTime / 4.0) * (System.currentTimeMillis() - currentFlashTime - ratio);
                alpha = 255 - (int) newValue;
            }
            g.setColor(new Color(255, 255, 255, alpha));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        if(showHUD && tileMap.getTile(0, 0) != null && !intersectingWithBot()) {
            hud.paint(g);
        }
        if(flipped) {
            g.drawImage(getFlippedInstance(), 0, getHeight() / 2, null);
        }
        if(finish) {
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setClip(new Ellipse2D.Double(circleLeftX, circleUpY, circleRightX - circleLeftX, circleDownY - circleUpY));
            g.drawImage(screen, 0, 0, null);
            g.setFont(new Font("System", Font.BOLD, 30));
            g.setClip(null);
            g.setColor(c);
            TextLayout txt = new TextLayout("Terminé !", g.getFont(), g.getFontRenderContext());
            AffineTransform af = g.getTransform();
            g.setTransform(AffineTransform.getRotateInstance(45.0 / 4.0 * Math.PI / 180.0, getWidth() / 2.0, getHeight() / 4.0));
            //g.drawString("Terminé", getWidth() / 2 - g.getFontMetrics().stringWidth("Terminé") / 2, getHeight() / 4);
            g.fill(txt.getOutline(AffineTransform.getTranslateInstance(getWidth() / 2.0 - txt.getBounds().getWidth() / 2.0, getHeight() / 4.0)));
            g.setColor(Color.white);
            g.draw(txt.getOutline(AffineTransform.getTranslateInstance(getWidth() / 2.0 - txt.getBounds().getWidth() / 2.0, getHeight() / 4.0)));
            g.setTransform(af);
        }
    }
    
    public boolean intersectingWithBot() {
        return hero.getY() + hero.height > tileMap.getY() + getHeight() - 40;
    }
    
    private BufferedImage getFlippedInstance() {
        BufferedImage flippedInstance = new BufferedImage(getWidth(), getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
        BufferedImage normal = getScreen();
        Graphics2D g = flippedInstance.createGraphics();
        /*for(int h = 0; h < normal.getHeight() / 2; h++) {
            for(int w = 0; w < flippedInstance.getWidth(); w++) {
                int rgb = normal.getRGB(w, h);
                g.setColor(new Color(rgb));
                g.fillRect(w, flippedInstance.getHeight() - h, 1, 1);
            }
        }*/
        g.drawImage(normal.getSubimage(0, 0, getWidth(), getHeight() / 2), 0, getHeight() / 2, getWidth(), -getHeight() / 2, null);
        g.dispose();
        return flippedInstance;
    }
    
    public void setState(int state) {
        this.gs.setState(state);
    }
    
    public void restart() {
        tileMap.reinit();
        hero.relive();
        setCamera(hero);
    }
    
    public int getWidth() {
        return gs.getWidth();
    }
    
    public int getHeight() {
        return gs.getHeight();
    }
    
    public void setHeroCanAction(boolean heroCanAction) {
        this.hero.setCanAction(heroCanAction);
    }
    
    public void setShowHUD(boolean b) {
        this.showHUD = b;
    }
    
    public void setArea(int area, int index) {
        this.tileMap.setArea(area, index);
        camera.setTween(1);
        setCamera(hero);
        camera.center();
        p = new ArrayList<>();
    }
    
    public void addToPaintAtEnd(MapObject mo) {
        p.add(mo);
    }
    
    public void setCamera(MapObject mo) {
        this.camera.setMapObject(mo);
    }
    
    public void setCameraAllignement(int allignement) {
        this.camera.setAllignement(allignement);
    }
    
    public void setBackgroundScreenSize(int size) {
        this.tileMap.setBackgroundSize(size);
    }
    
    public void setHeroDirX(int d) {
        this.hero.setDirX(d);
    }
    
    public void setHeroMaxMoveSpeed(double m) {
        this.hero.maxMoveSpeed = m;
    }
    
    public boolean isHUDShown() {
        return showHUD;
    }
    
    public void flash(long time) {
        this.flash = true;
        this.currentFlashTime = System.currentTimeMillis();
        this.flashTime = time;
    }
    
    public void confus(long time) {
        this.confused = true;
        this.currentConfusedTime = System.currentTimeMillis();
        this.confusedTime = time;
    }
    
    public void setHeroClothings(int i) {
        this.hero.setClothings(i);
    }
    
    public TileMap getTileMap() {
        return tileMap;
    }
    
    public void finish() {
        if(finish) return;
        if(getLevel() != -1) SaveManager.finish(getLevel());
        JukeBox.play("win");
        screen = null;
        finish = true;
        finishCount = 0;
        xCircleLeftRatio = (hero.getX() + hero.width / 2.0 - tileMap.getX()) / 30.0;
        xCircleRightRatio = (tileMap.getX() + getWidth() - (hero.getX() + hero.width / 2.0)) / 30.0;
        yCircleUpRatio = (hero.getY() + hero.height / 4.0 - tileMap.getY()) / 30.0;
        yCircleDownRatio = (tileMap.getY() + getHeight() - hero.getY()) / 30.0;
        circleLeftX = 0;
        circleRightX = getWidth();
        circleUpY = 0;
        circleDownY = getHeight();
    }
    
    public BufferedImage getScreen() {
        return gs.getScreen();
    }
    
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
    
    public void setCameraTween(double tween) {
        this.camera.setTween(tween);
    }
    
    public int getCameraXAdj() {
        return this.camera.getXAdj();
    }
    
    public int getCameraYOpp() {
        return camera.getYOpp();
    }
    
    public int getLevel() {
        return level;
    }
    
    public void disableAcessToMenu() {
        canAcessToMenu = false;
    }
}