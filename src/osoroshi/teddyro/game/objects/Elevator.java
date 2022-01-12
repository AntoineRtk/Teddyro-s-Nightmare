package osoroshi.teddyro.game.objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.animations.Smoke;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Elevator extends MapElement {
    
    private ArrayList<BufferedImage[]> animations;
    private boolean entering = false, elevating = false, closingDoors = false, reversing = false;
    private double defaultX, defaultY, px;
    private int elevatingIndex = 0, elevatingCount = 0, count = 0;
    private int[][] destinations;
    
    public Elevator(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        animations = GameResource.getElevatorSprites();
        animation.setFrames(animations.get(0));
        width = 64;
        height = 64;
        fallingSpeed = 0.4;
        maxFallingSpeed = 9.8;
        if(params == null) return;
        destinations = new int[params.split("-").length / 2][2];
        for(int i = 0; i < destinations.length; i++) {
            int xx = Integer.parseInt(params.split("-")[i * 2]), yy = Integer.parseInt(params.split("-")[i * 2 + 1]);
            destinations[i] = new int[]{xx, yy};
        }
    }
    
    public void update() {
        super.update();
        if(!elevating) super.checkCollisions();
        if(entering) {
            tileMap.hero.setFlipped(px < 0);
            tileMap.hero.addXImpulse(px);
            count++;
            if(count == 25) {
                tileMap.hero.setX(getX() + width / 2.0 - tileMap.hero.width / 2.0);
                entering = false;
                animation.setFrames(animations.get(1));
                animation.setDelay(15);
                animation.setReverse(true);
                closingDoors = true;
                JukeBox.play("elevator");
            }
        }
        if(closingDoors) {
            tileMap.hero.setCanAction(false);
            if(reversing && animation.getFrameIndex() == 0) {
                animation.setFrame(0);
                animation.setDelay(-1);
                elevating = true;
                elevatingIndex = 0;
                elevatingCount = 0;
                defaultX = getX();
                defaultY = getY();
                closingDoors = false;
            }
            if(animation.getFrameIndex() == 2) {
                reversing = true;
            }
        }
        if(elevating) {
            tileMap.hero.setCanAction(false);
            int dist = (int) Math.hypot(destinations[elevatingIndex][0] - ((elevatingIndex == 0) ? defaultX : destinations[elevatingIndex - 1][0]), destinations[elevatingIndex][1] - ((elevatingIndex == 0) ? defaultY : destinations[elevatingIndex - 1][1])) / 10;
            x += (destinations[elevatingIndex][0] - ((elevatingIndex == 0) ? defaultX : destinations[elevatingIndex - 1][0])) / dist;
            y += (destinations[elevatingIndex][1] - ((elevatingIndex == 0) ? defaultY : destinations[elevatingIndex - 1][1])) / dist;
            elevatingCount++;
            tileMap.hero.setX(getX() + width / 2.0 - tileMap.hero.width / 2.0);
            tileMap.hero.setY(getY() + height / 2.0 - tileMap.hero.height / 2.0);
            if(elevatingCount == dist) {
                elevatingCount = 0;
                elevatingIndex++;
                if(elevatingIndex > destinations.length - 1) {
                    elevatingIndex = destinations.length - 1;
                    tileMap.addObject(new Smoke(gl, tileMap, getX() + 32 - 128, getY() + 32 - 128, null));
                    tileMap.removeObject(this);
                    tileMap.hero.setCanAction(true);
                }
            }
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if((reversing && animation.getFrameIndex() == 1) || (reversing && animation.getFrameIndex() == 2) || (!reversing && onScreen())) {
            Shape s = g.getClip();
            if(animation.getFrameIndex() == 1 && reversing) g.setClip((int) getX() - tileMap.getX() + width / 4, (int) getY() - tileMap.getY(), width / 2, height);
            tileMap.hero.paint(g);
            g.setClip(s);
        }
    }
    
    public void intersect(MapObject mo) {
        if(mo instanceof Hero) {
            if(Controler.isUp() && !entering && !elevating) {
                entering = true;
                px = (getX() + width / 2.0 - tileMap.hero.width / 2.0 - tileMap.hero.getX()) / 25.0;
                tileMap.hero.dx = 0;
                count = 0;
                tileMap.hero.setCanAction(false);
            }
        }
    }
}