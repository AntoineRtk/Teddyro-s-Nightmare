package osoroshi.teddyro.game.objects.mobs;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Bat extends Monster {
    
    private ArrayList<BufferedImage[]> sprites;
    private int dirX = 0, dirY = 0, countX, countY, mx, my;
    
    public Bat(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, 3, 1, 0, true, 9);
        moveSpeed = 1.2;
        maxMoveSpeed = 5;
        jumpStart = -1;
        stopSpeed = 0.8;
        width = 30;
        height = 15;
        sprites = GameResource.getBatSprites();
        animation.setFrames(sprites.get(0));
        animation.setDelay(3);
        animation.setReverse(true);
        dirX = 1;
        dirY = 1;
        mx = 10 + new java.util.Random().nextInt(5) + 1;
        my = 15 + new java.util.Random().nextInt(10) + 1;
    }
    
    public void update() {
        setJumping(true);
        if(dirX == 1) {
            setDirX(1);
        }
        else if(dirX == -1) {
            setDirX(-1);
        }
        if(dirY == -1 && onScreen()) {
            jumpStart = -1;
            JukeBox.play("flap");
        }
        else if(dirY == 1) {
            jumpStart = 1;
        }
        countX++;
        countY++;
        if(countX >= mx) {
            dirX = -dirX;
            countX = 0;
        }
        if(countY >= my) {
            dirY = -dirY;
            countY = 0;
        }
        super.update();
    }
}