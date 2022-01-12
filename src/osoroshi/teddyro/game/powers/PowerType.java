package osoroshi.teddyro.game.powers;

import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public abstract class PowerType implements Cloneable {
    
    public GameLevel gl;
    public Hero hero;
    public TileMap tileMap;
    
    public PowerType(GameLevel gl, Hero hero, TileMap tileMap) {
        this.gl = gl;
        this.hero = hero;
        this.tileMap = tileMap;
    }
    
    public abstract void doAction();
    
    public abstract void update();
    
    public void paint(Graphics2D g) {}
    
    public PowerType clone() {
        try {
            return (PowerType) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(PowerType.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}