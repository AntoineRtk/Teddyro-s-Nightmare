package osoroshi.teddyro.game.powers;

import java.awt.Graphics2D;
import osoroshi.teddyro.game.objects.Bird;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class BirdPower extends PowerType {
    
    private Bird bird;
    
    public BirdPower(GameLevel gl, Hero hero, TileMap tileMap) {
        super(gl, hero, tileMap);
    }
    
    public void doAction() {
        /*if(hero.canAction()) {
            hero.setCanAction(false);
            bird = new Bird(tileMap, hero.isFlipped() ? hero.getX() - 8 : hero.getX() + hero.width, hero.getY(), hero.isFlipped());
        }*/
    }
    
    public void update() {
        if(bird == null) return;
        bird.update();
        gl.setCamera(bird);
        if(bird.canBeRemoved()) {
            hero.setCanAction(true);
            gl.setCamera(hero);
            bird = null;
        }
    }
    
    public void paint(Graphics2D g) {
        if(bird == null) return;
        bird.paint(g);
    }
}