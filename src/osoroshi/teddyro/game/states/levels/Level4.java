package osoroshi.teddyro.game.states.levels;

import java.awt.Graphics2D;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.states.GameState;
import osoroshi.teddyro.game.states.GameStateManager;
import osoroshi.teddyro.game.states.SceneManager;

public class Level4 extends GameState {
    
    private GameLevel gl;
    private boolean launchCutscene = true;
    
    public Level4(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        if(launchCutscene) {
            setState(14);
            ((SceneManager) gsm.getState()).setScene(0);
            launchCutscene = false;
        }
        else {
            gl = new GameLevel(this, 5);
            gl.load("map4", true);
            launchCutscene = true;
        }
    }
    
    public void update() {
        gl.update();
    }
    
    public void paint(Graphics2D g) {
        gl.paint(g);
    }
}