package osoroshi.teddyro.game.states;

import java.awt.Graphics2D;
import java.util.ArrayList;
import osoroshi.teddyro.game.states.scenes.Scene1;

public class SceneManager extends GameState {
    
    private ArrayList<Scene> scenes = new ArrayList<>();
    private int index = -1;
    
    public SceneManager(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        scenes.clear();
        scenes.add(new Scene1(this));
    }
    
    public void setScene(int scene) {
        this.scenes.get(scene).init();
        index = scene;
    }
    
    public void update() {
        if(index == -1) return;
        this.scenes.get(index).update();
    }
    
    public void paint(Graphics2D g) {
        if(index == -1) return;
        this.scenes.get(index).paint(g);
    }
}