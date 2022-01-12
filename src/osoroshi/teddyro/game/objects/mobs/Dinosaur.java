package osoroshi.teddyro.game.objects.mobs;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class Dinosaur extends Monster {
    
    private ArrayList<BufferedImage[]> animations = new ArrayList<>();
    
    public Dinosaur(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, 4, 1, 0, true, 3);
        animations = GameResource.getDinosaurSprites();
        width = 30;
        height = 30;
        animation.setFrames(animations.get(0));
        animation.setDelay(6);
        moveSpeed = 0.4;
        maxMoveSpeed = 2.4;
        fallingSpeed = 2.3;
        maxFallingSpeed = 9.8;
        setDirX(-1);
    }
    
    public void update() {
        super.update();
        super.getNextPosition();
        setFlipped(getDirX() > 0);
    }
}