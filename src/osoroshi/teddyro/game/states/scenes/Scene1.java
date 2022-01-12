package osoroshi.teddyro.game.states.scenes;

import osoroshi.teddyro.game.states.Scene;
import osoroshi.teddyro.game.states.SceneManager;

public class Scene1 extends Scene {
    
    public Scene1(SceneManager sm) {
        super(sm);
    }
    
    public void init() {
        load("scene1");
    }
    
    public void finish() {
        getSceneManager().gsm.setState(13);
    }
}