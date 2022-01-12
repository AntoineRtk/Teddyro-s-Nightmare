package osoroshi.teddyro.game.states.levels;

import java.awt.Graphics2D;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.states.GameState;
import osoroshi.teddyro.game.states.GameStateManager;
import osoroshi.teddyro.game.states.SceneManager;

public class Level5 extends GameState {
    
    private GameLevel gl;
    private boolean launchCutscene = false;
    
    public Level5(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        //gl.setArea(3, 0);
        //Faction de la destruction
        //Forêt du rejet
        if(launchCutscene) {
            setState(14);
            ((SceneManager) gsm.getState()).setScene(1);
            launchCutscene = false;
        }
        else {
            gl = new GameLevel(this, 6);
            gl.load("map5", true);
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