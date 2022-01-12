package osoroshi.teddyro.game.objects.mobs;

import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class RedSquid extends Squid {

    public RedSquid(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y);
        animations = GameResource.getRedSquidSprites();
        moveSpeed = 0.3255;
        maxMoveSpeed = 5.8575;
        setMaxCount(30);
        setLife(6);
    }
    
    public void addCannonball() {
        if(!isFlipped()) {
            tileMap.addEnemy(new AutoCannonBall(gl, tileMap, getX() - 35, getY() + height / 2 - 35 / 2, animations.get(2), animations.get(3)[0], -1));
        }
        else {
            tileMap.addEnemy(new AutoCannonBall(gl, tileMap, getX() + width + 35, getY() + height / 2 - 35 / 2, animations.get(2), animations.get(3)[0], 1));
        }
    }
}