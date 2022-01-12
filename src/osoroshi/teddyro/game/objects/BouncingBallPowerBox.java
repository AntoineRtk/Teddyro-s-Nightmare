package osoroshi.teddyro.game.objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.powers.Power;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class BouncingBallPowerBox extends MapElement {
    
    private boolean visible = true;
    private long time = System.currentTimeMillis();
    private float opacity = 1;
    
    public BouncingBallPowerBox(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, null);
        width = 30;
        height = 30;
        fallingSpeed = 0.4;
        maxFallingSpeed = 9.9;
        BouncingBall bc = new BouncingBall(this.gl, tileMap, -1, -1);
        bc.width = 80;
        bc.height = 80;
        animation.setFrames(new BufferedImage[]{bc.getImage()});
    }
    
    public void update() {
        super.update();
        super.checkCollisions();
        rotation += 5;
        if(!visible) {
            opacity -= 0.1f;
            if(opacity < 0) {
                opacity = 0.1f;
            }
            setOpacity(opacity);
        }
        if(System.currentTimeMillis() - time > 6000 && !visible) {
            visible = true;
            opacity = 1;
            setOpacity(opacity);
            BouncingBall bc = new BouncingBall(this.gl, tileMap, -1, -1);
            bc.width = 80;
            bc.height = 80;
            animation.setFrames(new BufferedImage[]{bc.getImage()});
        }
    }
    
    public void intersect(MapObject mo) {
        if(!visible) return;
        visible = false;
        time = System.currentTimeMillis();
        if(mo instanceof Hero) {
            ((Hero) mo).setPower(Power.BOUNCING_BALL_POWER);
        }
    }
    
    public void paint(Graphics2D g) {
        if(opacity == 0.1f) return;
        super.paint(g);
    }
}