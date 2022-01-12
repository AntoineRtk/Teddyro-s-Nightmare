
package osoroshi.teddyro.game.powers;

import osoroshi.teddyro.game.objects.BouncingBall;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;

public class BouncingBallPower extends PowerType {
    
    private int count = 0, whp;
    private boolean launchingBall = false;
    private BouncingBall currentBouncingBall;
    
    public BouncingBallPower(GameLevel gl, Hero hero, TileMap tileMap) {
        super(gl, hero, tileMap);
    }
    
    public void doAction() {
        /*if(!hero.isFalling() && !hero.preparingJump() && currentBouncingBall == null && !hero.shootingStars()){
            BouncingBall bcb = new BouncingBall(this.gl, tileMap, 0, 0);
            currentBouncingBall = bcb;
            tileMap.addObject(currentBouncingBall);
            whp = 2 + new java.util.Random().nextInt(4);
        }*/
    }
    
    public void update() {
        if(currentBouncingBall != null) {
            if(currentBouncingBall.width != 80) {
                hero.setCanAction(false);
                count++;
                hero.setAnimation(6, -1);
                hero.width = 57;
                if(count >= 2 + new java.util.Random().nextInt(1) + 1) {
                    currentBouncingBall.width += whp;
                    currentBouncingBall.height += whp;
                    if(currentBouncingBall.width >= 80) {
                        currentBouncingBall.width = 80;
                        currentBouncingBall.height = 80;
                        hero.setCanAction(true);
                        if(hero.isFlipped()) {
                            currentBouncingBall.setDirX(-1);
                        }
                        else {
                            currentBouncingBall.setDirX(1);
                        }
                        currentBouncingBall.initState();
                        hero.width = 45;
                    }
                    if(hero.isFlipped()) {
                        currentBouncingBall.setX(hero.getX() - currentBouncingBall.width);
                    }
                    else {
                        currentBouncingBall.setX(hero.getX() + hero.width);
                    }
                    currentBouncingBall.setY(hero.getY() + (hero.height - currentBouncingBall.height) / 2);
                    count = 0;
                }
                if(currentBouncingBall.width == 80) {
                    currentBouncingBall.setY(hero.getY() + hero.height - currentBouncingBall.height);
                    currentBouncingBall = null;
                }
            }
        }
    }
}