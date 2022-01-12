package osoroshi.teddyro.game.objects.mobs;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.animations.DustMonster;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public abstract class Monster extends MapObject {
    
    public GameLevel gl;
    private int life = 0, damage = 0, def = 0, flinchCount, flinchTime, lastDirX, lastDirY, stuntCount;
    private boolean canHitAtHead = true, stunt = false;
    
    public Monster(GameLevel gl, TileMap tileMap, double x, double y, int life, int damage, int def, boolean chah, int flinchTime) {
        super(tileMap, x, y);
        this.gl = gl;
        this.life = life;
        this.damage = damage;
        this.def = def;
        this.canHitAtHead = chah;
        this.flinchTime = flinchTime;
        flinchCount = flinchTime + 1;
    }
    
    public void update() {
        super.update();
        if(stuntCount > 60 && stunt) {
            stunt = false;
            setDirX(lastDirX);
            setDirY(lastDirY);
        }
        if(stunt) {
            setDirX(0);
            setDirY(0);
        }
        stuntCount++;
        super.checkCollisions();
        flinchCount++;
        if((int) ((y + height) / 32) * 32 >= tileMap.getMapHeight()) {
            die();
        }
    }
    
    public void checkIntersections() {
        if(isDead()) return;
        if(((tileMap.hero.getX() >= getX() && tileMap.hero.getX() <= getX() + width - 1) ||
                (getX() >= tileMap.hero.getX() && getX() <= tileMap.hero.getX() + tileMap.hero.width - 1)) &&
                ((tileMap.hero.getY() >= getY() && tileMap.hero.getY() <= getY() + height - 1) ||
                (getY() >= tileMap.hero.getY() && getY() <= tileMap.hero.getY() + tileMap.hero.height - 1))) {
            tileMap.hero.hit(damage);
            hitHero();
        }
    }
    
    public void hitHero() {}
    
    public boolean isFlinching() {
        return flinchCount <= flinchTime;
    }
    
    public void paint(Graphics2D g) {
        if(flinchCount > flinchTime || (flinchCount <= flinchTime && flinchCount % 3 == 0)) {
            super.paint(g);
            if(flinchCount <= flinchTime) {
                Shape s = g.getClip();
                Composite c = g.getComposite();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g.setColor(Color.red);
                Shape ms = getMonsterShape();
                if(ms == null) {
                    g.setClip(s);
                    g.setComposite(c);
                    return;
                }
                g.setClip(ms);
                g.fillRect((int) getX() - tileMap.getX(), (int) getY() - tileMap.getY(), width, height);
                g.setClip(s);
                g.setComposite(c);
            }
        }
    }
    
    public Shape getMonsterShape() {
        if(!GameResource.drawConvexShapes()) return null;
        Area ms = new Area();
        BufferedImage i = getImage();
        if(isFlipped()) {
            i = GameResource.getXFlippedInstance(getImage());
        }
        for(int h = 0; h < animation.getImage().getHeight(); h++) {
            for(int w = 0; w < animation.getImage().getWidth(); w++) {
                Color c = new Color(i.getRGB(w, h));
                if(c.getRed() != 255 && c.getBlue() != 255 && c.getGreen() != 255) {
                    java.awt.geom.Rectangle2D r = new java.awt.geom.Rectangle2D.Double(getX() - tileMap.getX() + w, getY() - tileMap.getY() + h, 1, 1);
                    ms.add(new Area(r));
                }
            }
        }
        return ms;
    }
    
    public void getNextJumpPosition() {
        
    }
    
    public void getNextPosition() {
        if(dx == 0) {
            setDirX(-getDirX());
        }
    }
    
    public void setLife(int life) {
        this.life = life;
    }
    
    public int getLife() {
        return life;
    }
    
    public boolean hit(int damage) {
        if(damage - def < 1) return false;
        if(flinchCount > flinchTime) {
            flinchCount = 0;
            this.life -= damage;
            if(life <= 0) {
                die();
            }
            return true;
        }
        return false;
    }
    
    public int getDef() {
        return def;
    }
    
    public boolean canHitAtHead() {
        return canHitAtHead;
    }
    
    public boolean isDead() {
        return life <= 0;
    }
    
    public void die() {
        BufferedImage deadImage = getImage();
        if(isFlipped()) {
            deadImage = GameResource.getXFlippedInstance(deadImage);
        }
        for(int w = 0; w < getImage().getWidth(); w++) {
            for(int h = 0; h < getImage().getHeight(); h++) {
                tileMap.addObject(new DustMonster(gl, tileMap, deadImage.getSubimage(w, h, 1, 1), getX() + w, getY() + h));
            }
        }
        tileMap.removeEnemy(this);
    }
    
    public void stunt() {
        lastDirX = getDirX();
        lastDirY = getDirY();
        stunt = true;
        stuntCount = 0;
    }
    
    public boolean isStunt() {
        return stunt;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public boolean onScreen() {
        return new Rectangle((int) getX(), (int) getY(), width, height).intersects(tileMap.getX(), tileMap.getY(), gl.getWidth(), gl.getHeight());
    }
    
    public void setDamage(int damage) {
        this.damage = damage;
    }
}