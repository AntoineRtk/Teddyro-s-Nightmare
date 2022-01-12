package osoroshi.teddyro.game.powers;

import java.awt.Graphics2D;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class Power {
    
    public static PowerType NONE;
    public static BouncingBallPower BOUNCING_BALL_POWER;
    public static BirdPower BIRD_POWER;
    private PowerType pt;
    public GameLevel gl;
    private Hero hero;
    private TileMap tileMap;
    
    public Power(GameLevel gl, Hero hero, TileMap tileMap) {
        this.gl = gl;
        this.hero = hero;
        this.tileMap = tileMap;
        BOUNCING_BALL_POWER = new BouncingBallPower(this.gl, this.hero, this.tileMap);
        BIRD_POWER = new BirdPower(this.gl, this.hero, this.tileMap);
    }
    
    public void doAction() {
        if(pt != null) {
            pt.doAction();
        }
    }
    
    public void update() {
        if(pt != null) {
            this.pt.update();
        }
    }
    
    public void paint(Graphics2D g) {
        if(pt != null) {
            this.pt.paint(g);
        }
    }
    
    public void setPower(PowerType pt) {
        try {
            this.pt = pt.clone();
            this.hero.setCanAction(true);
        } catch (java.lang.NullPointerException ex) {
            this.pt = null;
        }
    }
    
    public PowerType getType() {
        return this.pt;
    }
}