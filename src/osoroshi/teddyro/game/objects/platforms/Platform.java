package osoroshi.teddyro.game.objects.platforms;

import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.objects.mobs.Monster;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public abstract class Platform extends MapElement {
    
    private int cr = -1;
    
    public Platform(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
    }
    
    public void update() {
        super.update();
        double max = Math.max(Math.abs(dx), Math.abs(dy));
        double lx = getX(), ly = getY();
        double xIncrease = dx / max;
        double yIncrease = dy / max;
        for(int n = 0; n < max; n++) {
            if(dx != 0) {
                setX(lx + xIncrease * (n + 1));
                checkXAxis();
            }
            if(dy != 0) {
                setY(ly + yIncrease * (n + 1));
                checkYAxis();
            }
        }
    }
    
    public void xAxisVerification() {
        checkXAxis();
    }
    
    public void yAxisVerification() {
        if(cr != -1) cr++;
        checkYAxis();
        if(cr == 30) gl.restart();
    }
    
    public void checkXAxis() {
        Hero hero = tileMap.hero;
        if(dx != 0) {
            if(dx < 0) {
                for(int i = 0; i < tileMap.mobs[area].size(); i++) {
                    Monster m = tileMap.mobs[area].get(i);
                    if(((m.getY() >= getY() && m.getY() < getY() + height) || (getY() >= m.getY() && getY() < m.getY() + m.height))
                        && (getX() >= m.getX() && getX() < m.getX() + m.width)) {
                        m.setX(getX() - m.width);
                        m.dx = 0;
                        intersectX(m, 1);
                    }
                }
                if(((hero.getY() >= getY() && hero.getY() < getY() + height) || (getY() >= hero.getY() && getY() < hero.getY() + hero.height))
                    && (getX() >= hero.getX() && getX() < hero.getX() + hero.width)) {
                    hero.setX(getX() - hero.width);
                    hero.dx = 0;
                    intersectX(hero, 1);
                }
            }
            if(dx > 0) {
                for(int i = 0; i < tileMap.mobs[area].size(); i++) {
                    Monster m = tileMap.mobs[area].get(i);
                    if(((m.getY() >= getY() && m.getY() < getY() + height) || (getY() >= m.getY() && getY() < m.getY() + m.height))
                        && (getX() + width > m.getX() && getX() + width < m.getX() + m.width)) {
                        m.setX(getX() + width);
                        m.dx = 0;
                        intersectX(m, -1);
                    }
                }
                if(((hero.getY() >= getY() && hero.getY() < getY() + height) || (getY() >= hero.getY() && getY() < hero.getY() + hero.height))
                    && (getX() + width > hero.getX() && getX() + width < hero.getX() + hero.width)) {
                    hero.setX(getX() + width);
                    hero.dx = 0;
                    intersectX(hero, -1);
                }
            }
        }
    }
    
    public void checkYAxis() {
        Hero hero = tileMap.hero;
        if(dy != 0) {
            if(dy < 0) {
                for(int i = 0 ; i < tileMap.mobs[area].size(); i++) {
                    Monster m = tileMap.mobs[area].get(i);
                    if(((m.getX() >= getX() && m.getX() < getX() + width) || (getX() >= m.getX() && getX() < m.getX() + m.width))
                        && (getY() >= m.getY() && getY() < m.getY() + m.height)) {
                      m.setPlatform(this);
                        for(int i1 = 0; i1 < m.blocksWidth; i1++) {
                            if(tileMap.isSolid(m.getX() + i1 * 32, m.getY())) {
                                int yb = (int) (m.getY() / 32.0) * 32;
                                m.height = (int) (getY() - yb);
                                m.setY(getY() - m.height);
                                if(m.height == 0) {
                                    m.hit(m.getDef() + m.getLife());
                                }
                            }
                        }
                    }
                }
                if(((hero.getX() >= getX() && hero.getX() < getX() + width) || (getX() >= hero.getX() && getX() < hero.getX() + hero.width))
                    && (getY() >= hero.getY() && getY() <=hero.getY() + hero.height)) {
                    hero.setPlatform(this);
                    for(int i = 0; i < hero.blocksWidth; i++) {
                        if(tileMap.isSolid(hero.getX() + i * 32, hero.getY())) {
                            hero.setCanAction(false);
                            int yb = (int) (hero.getY() / 32.0) * 32;
                            hero.height = (int) (getY() - yb);
                            hero.setY(getY() - hero.height);
                            if(hero.height == 0) {
                                cr = 0;
                            }
                        }
                    }
                }
            }
            if(dy > 0) {
                for(int i = 0 ; i < tileMap.mobs[area].size(); i++) {
                    Monster m = tileMap.mobs[area].get(i);
                    if(((m.getX() >= getX() && m.getX() < getX() + width) || (getX() >= m.getX() && getX() < m.getX() + m.width))
                        && (m.getY() >= getY() && m.getY() < getY() + height)) {
                        m.setY(getY() + height);
                        intersectY(m, -1);
                        m.dy = 0;
                        for(int i1 = 0; i1 < m.blocksWidth; i1++) {
                            if(tileMap.isSolid(m.getX() + i1 * 32, m.getY() + m.height - 1)) {
                                m.dx = 0;
                                int yb = (int) ((m.getY() + m.height) / 32.0) * 32;
                                m.height = (int) (yb - getY() - height);
                                m.setY(yb - m.height);
                                if(m.height == 0) {
                                    m.hit(m.getDef() + m.getLife());
                                }
                            }
                        }
                    }
                }
                if(((hero.getX() >= getX() && hero.getX() < getX() + width) || (getX() >= hero.getX() && getX() < hero.getX() + hero.width))
                    && (hero.getY() >= getY() && hero.getY() < getY() + height)) {
                    hero.setY(getY() + height);
                    intersectY(hero, -1);
                    hero.dy = 0;
                    for(int i = 0; i < hero.blocksWidth; i++) {
                        if(tileMap.isSolid(hero.getX() + i * 32, hero.getY() + hero.height - 1)) {
                            hero.setCanAction(false);
                            int yb = (int) ((hero.getY() + hero.height) / 32.0) * 32;
                            hero.height = (int) (yb - getY() - height);
                            hero.setY(yb - hero.height);
                            if(hero.height == 0) {
                                cr = 0;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public boolean intersectX(MapObject mo, int dir) { return true; }
    
    public boolean intersectY(MapObject mo, int dir) { return true; }
}