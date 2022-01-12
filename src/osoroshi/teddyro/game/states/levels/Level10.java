package osoroshi.teddyro.game.states.levels;

import java.awt.Graphics2D;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.states.GameState;
import osoroshi.teddyro.game.states.GameStateManager;

public class Level10 extends GameState {
    
    private GameLevel gl;
    
    public Level10(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        gl = new GameLevel(this, 12);
        gl.load("map10", true);
        //gl.setArea(3, 0);
        //Faction de la destruction
        //Forêt du rejet
    }
    
    public void update() {
        gl.update();
    }
    
    public void paint(Graphics2D g) {
        gl.paint(g);
    }
}