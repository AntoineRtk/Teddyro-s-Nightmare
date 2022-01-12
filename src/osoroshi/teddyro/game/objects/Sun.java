package osoroshi.teddyro.game.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.JukeBox;

public class Sun extends MapElement {
    
    public Sun(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, null);
    }
    
    public void update() {
        if(!tileMap.isSunny()) {
            JukeBox.stopAll();
            JukeBox.play(2);
            tileMap.setSunny(true);
        }
    }
    
    public void paint(Graphics2D g) {
        for(int i = 15; i < 0; i--) {
            g.setColor(Color.white);
            g.fillRect(15 - i, 0, 1, 1);
        }
    }
}