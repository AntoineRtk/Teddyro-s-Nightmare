package osoroshi.teddyro.game.animations;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class BouncingBallExplosion extends MapElement {
    
    private BufferedImage image;
    private boolean shouldBeRemoved = false;
    
    public BouncingBallExplosion(GameLevel gl, TileMap tileMap, double x, double y, BufferedImage image) {
        super(gl, tileMap, x, y, null);
        this.image = image;
        animation.setFrames(new BufferedImage[]{image});
        width = 5;
        height = 3;
        fallingSpeed = 0.85;
        maxFallingSpeed = 7.26 + new java.util.Random().nextDouble() * (12.26 + 7.26);
        dy = -(7 + new java.util.Random().nextInt(11 - 7 + 1));
        moveSpeed = 1.95 + new java.util.Random().nextDouble() * (2.25 - 1.95);
        maxMoveSpeed = new java.util.Random().nextDouble() * (5.4525 - 0.8575);
        if(new java.util.Random().nextBoolean()) {
            setDirX(1);
        }
        else setDirX(-1);
    }
    
    public void update() {
        super.checkCollisions();
        if(dy == 0 && !isFalling()) {
            tileMap.removeObject(this);
        }
    }
}