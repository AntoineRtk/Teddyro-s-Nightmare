package osoroshi.teddyro.game.objects.mobs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class Slug extends Monster {
    
    private ArrayList<BufferedImage[]> animations;
    private ArrayList<SlugTail> tail = new ArrayList<>();
    private int size = 3, xd, yd;
    private boolean onBot, goX;
    
    public Slug(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, 5, 2, 1, false, 75);
        animations = GameResource.getSlugSprites();
        width = 23;
        height = 18;
        animation.setFrames(animations.get(0));
        animation.setDelay(6);
        moveSpeed = 0.7;
        maxMoveSpeed = 2.45;
        stopSpeed = 2.45;
    }
    
    public void update() {
        super.update();
        int blocks = (int) ((getX() + width - 1) / 32.0) - (int) (getX() / 32.0) + 1;
        if(!onBot) {
            for(int w = 0; w < blocks * 32; w += 32) {
                if(hasIntersectedInY() && dy == 0) {
                    onBot = true;
                    yd = 1;
                    setDirX(new java.util.Random().nextBoolean() ? 1 : -1);
                    setDirX(1);
                }
                else {
                    setJumping(true);
                    jumpStart = 2.45;
                }
            }
        }
        else {
            int xblocks = (int) (((getX() + width - 1) / 32.0) - (getX() / 32.0)) + 1;
            int yblocks = (int) (((getY() + height - 1) / 32.0) - (getY() / 32.0)) + 1;
            
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
    }
}