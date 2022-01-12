package osoroshi.teddyro.game.objects.platforms;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class MovingPlatform extends Platform {
    
    private int maxCount, count = 0;
    
    public MovingPlatform(GameLevel gl, TileMap tileMap, double x, double y, String params, int index) {
        super(gl, tileMap, x, y, params);
        animation.setFrames(new BufferedImage[]{GameResource.getPlatformSprites(index)});
        width = 96;
        height = 64;
        if(index == 3) {
            width = 128;
            height = 128;
        }
        double toX, toY;
        if(params == null) return;
        toX = Double.parseDouble(params.split("-")[0]);
        toY = Double.parseDouble(params.split("-")[1]);
        maxCount = (int) (Math.max(Math.abs(toX - x), Math.abs(toY - y)) / 4.0);
        if(maxCount == 0) return;
        dx = (toX - x) / maxCount;
        dy = (toY - y) / maxCount;
    }
    
    public void update() {
        if(count == maxCount) {
            dx = -dx;
            dy = -dy;
            count = 0;
        }
        super.update();
        count++;
    }
}