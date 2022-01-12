package osoroshi.teddyro.game.objects.mobs;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Snail extends Monster {
    
    private ArrayList<BufferedImage[]> sprites;
    private boolean stopped = false;
    
    public Snail(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, 2, 1, 0, false, 15);
        sprites = GameResource.getSnailSprites();
        animation.setFrames(sprites.get(0));
        animation.setDelay(1);
        width = 22;
        height = 20;
        fallingSpeed = 0.4;
        maxFallingSpeed = 10;
        moveSpeed = 0.3;
        maxMoveSpeed = 1.7;
        jumpStart = -7;
        setDirX(1);
        JukeBox.stop("snailsfx");
   }
    
    public void update() {
        super.update();
        getNextPosition();
        if(getDirX() > 0) {
            setFlipped(false);
        }
        else {
            setFlipped(true);
        }
        if(onScreen() && !isFalling() && dx != 0) {
            JukeBox.setLooping("snailsfx", 0, -1);
        }
        else {
            if(!stopped) {
                stopped = true;
                JukeBox.stop("snailsfx");
            }
        }
    }
}