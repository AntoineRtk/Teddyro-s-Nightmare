package osoroshi.teddyro.game.objects;

import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Flower extends MapElement {
    
    private boolean heroInFlower = false;
    private int count = 0;
    
    public Flower(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, null);
        width = 45;
        height = 38;
        animation.setFrames(GameResource.getFlowerSprites().get(0));
    }
    
    public void update() {
        if(heroInFlower) {
            tileMap.hero.setCanAction(false);
            if(!tileMap.hero.isFalling()) {
                tileMap.hero.setAnimation(11, 0);
                count++;
                if(count > 20) {
                    tileMap.hero.setAnimationIndex(1);
                }
                if(count > 40) {
                    gl.finish();
                }
            }
        }
    }
    
    public void intersect(MapObject mo) {
        if(mo instanceof Hero) {
            JukeBox.stopAll();
            heroInFlower = true;
            tileMap.hero.setX((tileMap.hero.getX() + width / 2 < getX() + width / 2) ? getX() - tileMap.hero.width : getX() + width);
            if(tileMap.hero.getX() > getX()) tileMap.hero.setFlipped(true);
            else tileMap.hero.setFlipped(false);
        }
    }
}