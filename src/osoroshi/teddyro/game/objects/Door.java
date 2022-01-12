package osoroshi.teddyro.game.objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;

public class Door extends MapElement {
    
    private int a = -1, index = -1;
    private ArrayList<BufferedImage[]> sprites;
    private boolean playing = false, invisible = false;
    
    public Door(GameLevel gl, TileMap tileMap, double x, double y, String params, int id) {
        super(gl, tileMap, x, y, params);
        setDoorParams(params);
        invisible = false;
        fallingSpeed = 0.4;
        maxFallingSpeed = 12;
        switch(id) {
            case 0:
                sprites = GameResource.getCastleDoorSprites();
                if(params.split("-").length == 3) sprites = GameResource.getCastleDoorSprites2();
                width = 170;
                height = 244;
                break;
            case 1:
                sprites = GameResource.getBossDoorSprites();
                width = 256;
                height = 256;
                break;
        }
        animation.setFrames(sprites.get(0));
    }
    
    public Door(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        width = 34;
        height = 60;
        setDoorParams(params);
        sprites = GameResource.getDoorSprites();
        if(!invisible) animation.setFrames(sprites.get(0));
    }
    
    public void setDoorParams(String params) {
        fallingSpeed = 0.4;
        maxFallingSpeed = 12;
        if(params != null) {
            if(params.split("-").length == 3) {
                invisible = true;
                fallingSpeed = 0;
                this.height = Integer.parseInt(params.split("-")[0]);
                this.a = Integer.parseInt(params.split("-")[1]);
                this.index = Integer.parseInt(params.split("-")[2]);
            }
            else {
                this.a = Integer.parseInt(params.split("-")[0]);
                this.index = Integer.parseInt(params.split("-")[1]);
            }
        }
    }
    
    public void update() {
        if(playing) {
            if(animation.isReversing() && animation.getFrameIndex() == 0) {
                playing = false;
                animation.setFrames(sprites.get(0));
                animation.setDelay(-1);
                gl.setArea(a, index);
                tileMap.hero.setCanAction(true);
            }
        }
        super.update();
        super.checkCollisions();
    }
    
    public void paint(Graphics2D g) {
        if(onScreen()) {
            /*int xi = (int) (getX() - tileMap.getX()), yi = (int) (getY() - tileMap.getY());
            if(xi < 0) xi = 0;
            if(yi < 0) yi = 0;
            BufferedImage s = gl.getScreen();
            int wi = xi + width > s.getWidth() ? s.getWidth() - xi : width, hi = yi + height > s.getHeight() ? s.getHeight() - yi : height;
            BufferedImage i = s.getSubimage((int) getX() - tileMap.getX(), (int) getY() - tileMap.getY(), wi, hi);
            g.drawImage(i, (int) getX() - tileMap.getX(), (int) getY() - tileMap.getY(), null);
            */super.paint(g);
            if(!animation.isReversing() && tileMap.hero.intersects(this)) {
                tileMap.hero.paint(g);
            }
        }
    }
    
    public void intersect(MapObject mo) {
        if(a == -1) return;
        if(mo instanceof Hero) {
            if(!invisible) {
                if(Controler.isUp()) {
                    if(!playing) {
                        Controler.setUp(false);
                        playing = true;
                        animation.setFrames(sprites.get(1));
                        animation.setReverse(true);
                        animation.setDelay(9);
                        ((Hero) mo).setCanAction(false);
                    }
                }
            }
            else {
                gl.setArea(a, index);
            }
        }
    }
}