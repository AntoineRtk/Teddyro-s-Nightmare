package osoroshi.teddyro.game.states;

import java.awt.Color;
import java.awt.Graphics2D;
import osoroshi.teddyro.game.tilemap.Background;
import osoroshi.teddyro.game.utils.JukeBox;

public class Dream extends GameState {
    
    private GameLevel gl;
    private int count = 0, opacity = 255;
    
    public Dream(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        gl = new GameLevel(this, 0);
        gl.load("dream", true);
        gl.setBackgroundScreenSize(Background.LOOPING);
        count = 0;
    }
    
    public void update() {
        gl.update();
        opacity = 255 - (int) (255.0 / 299.0 * count);
        count++;
        if(opacity < 0) {
            opacity = 0;
            if(!JukeBox.isRunning("dream")) {
                JukeBox.play("dream");
            }
        }
    }
    
    public void paint(Graphics2D g) {
        gl.paint(g);
        g.setColor(new Color(0, 0, 0, opacity));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}