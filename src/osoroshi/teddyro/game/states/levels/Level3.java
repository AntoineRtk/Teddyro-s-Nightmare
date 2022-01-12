package osoroshi.teddyro.game.states.levels;

import java.awt.Graphics2D;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.states.GameState;
import osoroshi.teddyro.game.states.GameStateManager;

public class Level3 extends GameState {
    
    private GameLevel gl;
    
    public Level3(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        gl = new GameLevel(this, 3);
        gl.load("map3", true);
    }
    
    public void update() {
        gl.update();
    }
    
    public void paint(Graphics2D g) {
        gl.paint(g);
    }
}