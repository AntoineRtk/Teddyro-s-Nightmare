package osoroshi.teddyro.game.objects.platforms;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class GreenBlock extends ColoredBlock {
    
    public GreenBlock(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        animation.setFrames(new BufferedImage[]{GameResource.getGreenBlock()});
        width = 64;
        height = 64;
    }
}