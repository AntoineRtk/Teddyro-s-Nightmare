package osoroshi.teddyro.game.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class TV extends MapElement {
    
    private ArrayList<BufferedImage[]> animations = new ArrayList<>();
    private boolean hasStarted = false, canPaint = true;
    private int count = 0;
    
    public TV(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        animations = GameResource.getTVSprites();
        fallingSpeed = 0.4;
        maxFallingSpeed = 32;
        width = 48;
        height = 37;
        animation.setFrames(animations.get(0));
        count = 0;
    }
    
    public void update() {
       super.checkCollisions();
       if(onScreen()) hasStarted = true;
       canPaint = true;
       if(hasStarted) count++;
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(!canPaint) return;
        canPaint = false;
        BufferedImage image = new BufferedImage(gl.getWidth(), gl.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D tvImage = image.createGraphics();
        boolean isHUDShown = gl.isHUDShown();
        gl.setShowHUD(false);
        gl.paint(tvImage);
        gl.setShowHUD(isHUDShown);
        tvImage.dispose();
        g.drawImage(image, (int) getX() - tileMap.getX() + 7, (int) getY() - tileMap.getY() + 3, 32, 27, null);
        if(count < 200) {
            g.setColor(Color.gray);
            for(int i = 0; i < (37*25)*(200-count)/200; i++) {
                g.fillRect((int) getX() - tileMap.getX() + 7 + new java.util.Random().nextInt(32), (int) getY() - tileMap.getY() + 3 + new java.util.Random().nextInt(27), 1, 1);
            }
        }
    }
}