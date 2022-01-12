package osoroshi.teddyro.game.states;

import java.awt.Graphics2D;
import java.io.File;

public class CustomLevel extends GameState {
    
    private GameLevel gl;
    
    public CustomLevel(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        gl = new GameLevel(this, -1);
    }
    
    public void update() {
        gl.update();
    }
    
    public void paint(Graphics2D g) {
        gl.paint(g);
    }
    
    public void setPath(File selectedFile) {
        gl.load(selectedFile.getAbsolutePath(), false);
    }
}