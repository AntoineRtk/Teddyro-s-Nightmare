package osoroshi.teddyro.game.objects.mobs;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Squid extends Monster {
    
    public ArrayList<BufferedImage[]> animations;
    private int state = 0, count = 0, maxCount = 50;
    
    public Squid(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, 3, 1, 0, true, 15);
        moveSpeed = 0.4;
        maxMoveSpeed = 1.4;
        stopSpeed = 1.2;
        fallingSpeed = 2;
        maxFallingSpeed = 6;
        animations = GameResource.getSquidSprites();
        width = 20;
        height = 20;
        JukeBox.stop("breath");
    }
    
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
    
    public void update() {
        super.update();
        count++;
        if(state == 0) {
            if(tileMap.hero.getX() + tileMap.hero.width / 2.0 > getX() + width / 2.0 && onScreen()) {
                setDirX(-1);
            }
            else if(onScreen()) {
                setDirX(1);
            }
        }
        if(dx != 0 && state == 0) {
            animation.setFrames(animations.get(0));
            animation.setDelay((int) (6.0 - (3.0 / 1.4 * Math.abs(dx))));
        }
        else if(state == 0) {
            animation.setFrames(animations.get(0));
            animation.setDelay(-1);
        }
        if(onScreen() && !isStunt()) {
            if(count > maxCount && state == 0) {
                state = 1;
                count = 0;
                animation.setFrames(animations.get(1));
                animation.setDelay(-1);
                JukeBox.setLooping("breath", 0, -1);
                setDirX(0);
            }
        }
        if(count == maxCount + 10 && state == 1) {
            JukeBox.stop("breath");
            animation.setFrame(1);
            addCannonball();
        }
        if(count >= maxCount + 20 && state == 1) {
            state = 0;
            count = 0;
        }
        setFlipped(tileMap.hero.getX() > getX() + width / 2);
    }
    
    public void die() {
        super.die();
        JukeBox.stop("breath");
    }
    
    public void addCannonball() {
        if(!isFlipped()) {
            tileMap.addEnemy(new CannonBall(gl, tileMap, getX() - 35, getY() + height / 2 - 35 / 2, animations.get(2), -1));
        }
        else {
            tileMap.addEnemy(new CannonBall(gl, tileMap, getX() + width + 35, getY() + height / 2 - 35 / 2, animations.get(2), 1));
        }
    }
}