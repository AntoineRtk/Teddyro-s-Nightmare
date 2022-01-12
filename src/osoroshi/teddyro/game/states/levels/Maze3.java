package osoroshi.teddyro.game.states.levels;

import java.awt.Graphics2D;
import osoroshi.teddyro.game.states.GameState;
import osoroshi.teddyro.game.states.GameStateManager;
import osoroshi.teddyro.game.states.Maze;

public class Maze3 extends GameState {
    
    private Maze maze;
    
    public Maze3(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        maze = new Maze(this, 15);
        maze.load("maze3");
        maze.start();
    }
    
    public void update() {
        maze.update();
    }
    
    public void paint(Graphics2D g) {
        maze.paint(g);
    }
}