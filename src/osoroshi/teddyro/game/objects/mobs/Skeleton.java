package osoroshi.teddyro.game.objects.mobs;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class Skeleton extends Monster {
    
    private ArrayList<BufferedImage[]> animations = new ArrayList<>();
    
    public Skeleton(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, 5, 3, 1, true, 60);
        moveSpeed = 0.8;
        maxMoveSpeed = 3.65;
        fallingSpeed = 0.8;
        maxFallingSpeed = 9.55;
        stopSpeed = 0.3;
        width = 24;
        height = 32;
        jumpStart = -14;
        animations = GameResource.getSkeletonSprites();
        animation.setFrames(animations.get(0));
    }
    
    public void update() {
        super.update();
        getNextJumpPosition();
        if(onScreen()) {
            animation.setFrames(animations.get(1));
            animation.setDelay((int) (4.5 - (3.0 / maxMoveSpeed * Math.abs(dx))));
            if(tileMap.hero.getX() < getX() + width / 2) {
                setDirX(-1);
            }
            else {
                setDirX(1);
            }
            if(new Rectangle((int) getX() - 100, (int) getY() - 100, 200, 200).intersects(tileMap.hero.getShape().getBounds())) {
                setJumping(true);
            }
        }
        else {
            setDirX(0);
            animation.setFrames(animations.get(0));
        }
        if(intersects(tileMap.hero) && !isFlinching()) {
            gl.confus(1000 + new java.util.Random().nextInt(4001));
        }
        setFlipped(tileMap.hero.getX() > getX() + width / 2);
    }
}