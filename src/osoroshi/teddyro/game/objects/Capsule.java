package osoroshi.teddyro.game.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.platforms.InvisiblePlatform;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.CapsuleFireTile;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;

public class Capsule extends MapElement {
    
    private InvisiblePlatform ip;
    private boolean inCapsule = false, autoStart;
    private int dir = 1, changeDirCount = 50;
    
    public Capsule(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, null);
        animation.setFrames(GameResource.getCapsuleSprites().get(0));
        width = 48;
        height = 64;
        moveSpeed = 0.25;
        maxMoveSpeed = 35.75;
        fallingSpeed = 0.4;
        maxFallingSpeed = 9.8;
        jumpStart = -10;
        if(params != null) {
            if(params.length() == 2) {
                if(params.charAt(0) != '0') dir = -dir;
                autoStart = true;
            }
            else {
                dir = -dir;
            }
        }
    }
    
    public void update() {
        if(autoStart) intersect(tileMap.hero);
        if(ip != null) {
            ip.setX(-ip.width);
            ip.setY(-ip.height);
        }
        super.checkCollisions();
        if(inCapsule && tileMap.getArea() == getArea()) {
            gl.setShowHUD(false);
            gl.setCameraTween(0.9);
            setDirX(dir);
            setFlipped(dir < 0);
            setJumping(Controler.isUp());
            tileMap.hero.setFlipped(dx < 0);
            tileMap.hero.setX(getX() + width / 2.0 - tileMap.hero.width / 2.0);
            tileMap.hero.setY(getY() + height - 1 - tileMap.hero.height);
            ip.setX(getX());
            ip.setY(getY() + height - 1);
            if(getY() + height == tileMap.getMapHeight()) {
                tileMap.removePlatform(ip);
                tileMap.removeObject(this);
                tileMap.hero.kill();
                gl.setShowHUD(false);
            }
            changeDirCount++;
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(Math.abs(dx) > 10) {
            double maxAlpha = 255.0 / 25.75 * (Math.abs(dx) - 10);
            if(maxAlpha > 255) maxAlpha = 255;
            BufferedImage wind = new BufferedImage(gl.getWidth(), 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = wind.createGraphics();
            for(int i = 0; i < gl.getWidth(); i++) {
                g2.setColor(new Color(255, 255, 255, (int) (maxAlpha / (gl.getWidth() - 1) * i)));
                g2.fillRect(i, 0, 1, 1);
            }
            g2.dispose();
            if(dx < 0) wind = GameResource.getXFlippedInstance(wind);
            g.setColor(Color.white);
            for(int i = 0; i < 60; i++) {
                int ypoint = 10 + new java.util.Random().nextInt(gl.getHeight() / 2);
                if(i > 29) {
                    ypoint += gl.getHeight() / 2 - 20;
                }
                g.drawImage(wind, 0, ypoint, null);
            }
        }
    }
    
    public void intersect(MapObject mo) {
        if(mo instanceof Hero && !inCapsule) {
            tileMap.hero.dx = 0;
            tileMap.hero.setX(getX() + width / 2.0 - tileMap.hero.width / 2.0);
            tileMap.hero.setCanAction(false);
            inCapsule = true;
            ip = new InvisiblePlatform(gl, tileMap, 0, 0, width, 1);
            ip.setX(getX());
            ip.setY(getY() + height - 1);
            tileMap.addPlatform(ip);
            tileMap.hero.setPlatform(ip);
        }
    }
    
    public void xAxisVerification() {
        if(tileMap.getArea() != getArea()) return;
        tileMap.hero.setX(getX() + width / 2.0 - tileMap.hero.width / 2.0);
        tileMap.hero.xAxisVerification();
        for(int w = 0; w < blocksWidth * 32; w += 32) {
            if(tileMap.getTile(getX() + w, getY() + height) instanceof CapsuleFireTile) {
                tileMap.removePlatform(ip);
                tileMap.removeObject(this);
                tileMap.hero.kill();
            }
        }
    }

    public void changeDirection(int dir) {
        if(dir == 0 && changeDirCount > 60) {
            this.dir = -this.dir;
            changeDirCount = 0;
        }
        else if(dir == 1) {
            fallingSpeed = 4;
            maxFallingSpeed = 98;
        }
        else if(dir == -1) {
            fallingSpeed = 0.4;
            maxFallingSpeed = 9.8;
        }
        else if(dir == 2) {
            moveSpeed = .25*15;
            maxMoveSpeed = 35.75*15;
        }
        else if(dir == -2) {
            moveSpeed = 0.25;
            maxMoveSpeed = 35.75;
        }
    }
}