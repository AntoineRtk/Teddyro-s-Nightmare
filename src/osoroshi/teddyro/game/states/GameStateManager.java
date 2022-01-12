package osoroshi.teddyro.game.states;

import osoroshi.teddyro.game.world.LevelSelector4;
import osoroshi.teddyro.game.states.levels.Maze1;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import osoroshi.teddyro.game.states.levels.Level1;
import osoroshi.teddyro.game.states.levels.Level10;
import osoroshi.teddyro.game.states.levels.Level11;
import osoroshi.teddyro.game.states.levels.Level12;
import osoroshi.teddyro.game.states.levels.Level2;
import osoroshi.teddyro.game.states.levels.Level3;
import osoroshi.teddyro.game.states.levels.Level4;
import osoroshi.teddyro.game.states.levels.Level5;
import osoroshi.teddyro.game.states.levels.Level6;
import osoroshi.teddyro.game.states.levels.Level7;
import osoroshi.teddyro.game.states.levels.Level8;
import osoroshi.teddyro.game.states.levels.Level9;
import osoroshi.teddyro.game.states.levels.Maze2;
import osoroshi.teddyro.game.states.levels.Maze3;
import osoroshi.teddyro.game.world.LevelSelector1;
import osoroshi.teddyro.game.world.LevelSelector2;
import osoroshi.teddyro.game.world.LevelSelector3;

public class GameStateManager {
    
    public ArrayList<GameState> states = new ArrayList<>();
    private Game game;
    private int index = -1;
    
    public GameStateManager(Game game) {
        this.game = game;
        states.add(new Intro(this));
        states.add(new Menu(this));
        states.add(new LevelSelector1(this));
        states.add(new LevelSelector2(this));
        states.add(new LevelSelector3(this));
        states.add(new PauseState(this));
        states.add(new CustomLevel(this));
        states.add(new Prologue(this));
        states.add(new Dream(this));
        states.add(new Level1(this));
        states.add(new Level2(this));
        states.add(new Level3(this));
        states.add(new Maze1(this));
        states.add(new Level4(this));
        states.add(new SceneManager(this));
        states.add(new Level5(this));
        states.add(new Level6(this));
        states.add(new Level7(this));
        states.add(new Maze2(this));
        states.add(new Level8(this));
        states.add(new Level9(this));
        states.add(new Level10(this));
        states.add(new Level11(this));
        states.add(new Level12(this));
        states.add(new Maze3(this));
        states.add(new FinalCutscene(this));
        states.add(new LevelSelector4(this));
        states.add(new MiniGameSelector(this));
        states.add(new MiniGame1(this));
    }
    
    public void setState(int index) {
        this.index = index;
        try {
            this.states.get(this.index).init();
        } catch (IndexOutOfBoundsException ex) {
            Object[] options = new String[]{"Return to index 0", "Write error to error.txt", "Exit game"};
            int option = JOptionPane.showOptionDialog(null, "The index that you were looking for is empty : "+ex+"\nDo return to index 0 ?", "Teddyro's Nightmare", 0, JOptionPane.ERROR_MESSAGE, null, options, 0);
            if(option == 0) {
                setState(0);
            }
            if(option == 1) {
                try {
                    System.setErr(new PrintStream("error.txt"));
                    throw new Exception(ex);
                } catch (FileNotFoundException ex1) {
                    JOptionPane.showMessageDialog(null, "Error "+ex1, "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex1) {
                    Logger.getLogger(GameStateManager.class.getName()).log(Level.SEVERE, null, ex1);
                }
                setState(0);
            }
            if(option == 2) {
                System.exit(0);
            }
        }
    }
    
    public void resume(int index) {
        this.index = index;
    }
    
    public GameState getState() {
        return states.get(index);
    }
    
    public GameState getState(int index) {
        return states.get(index);
    }
    
    public int getStateIndex() {
        return index;
    }
    
    public void update() {
        if(index == -1) return;
        states.get(index).update();
    }
    
    public void paint(Graphics2D g) {
        if(index == -1) return;
        states.get(index).paint(g);
    }
    
    public void keyPressed(int keyCode) {
        states.get(index).keyPressed(keyCode);
    }
    
    public void keyReleased(int keyCode) {
        states.get(index).keyReleased(keyCode);
    }
    
    public int getWidth() {
        return game.getWidth();
    }
    
    public int getHeight() {
        return game.getHeight();
    }
    
    public void setWidth(int w) {
        Main.setWidth(w);
        game.initImage();
    }
    
    public void setHeight(int h) {
        Main.setHeight(h);
        game.initImage();
    }
    
    public BufferedImage getScreen() {
        return game.getScreen();
    }
    
    public void setFPS(int fps) {
        game.setFPS(fps);
    }
    
    public void displayFPS() {
        game.displayFPS();
    }
}