package osoroshi.teddyro.game.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.animations.Animation;
import osoroshi.teddyro.game.states.GameState;
import osoroshi.teddyro.game.states.Maze;
import osoroshi.teddyro.game.tilemap.MazeTileMap;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Dog {
    
    private double x, y, dx, dy;
    private double rotation = 0;
    private GameState gs;
    private Maze maze;
    private MazeTileMap mzm;
    private boolean canMove = false, up, down, left, right;
    private Animation animation = new Animation();
    private ArrayList<BufferedImage[]> animations;
    
    public Dog(GameState gs, Maze maze, MazeTileMap mzm) {
        this.gs = gs;
        this.maze = maze;
        this.mzm = mzm;
        animations = GameResource.getDogSprites();
        animation.setFrames(animations.get(1));
        animation.setDelay(6);
    }
    
    public void update() {
        animation.update();
        if(canMove) checkForKeys();
        checkCollisions();
        if(dx == 0 && dy == 0 && canMove) {
            animation.setFrames(animations.get(0));
            animation.setFrame(0);
            animation.setDelay(-1);
        }
        else if(canMove) {
            animation.setFrames(animations.get(0));
            animation.setDelay(6);
        }
        else {
            animation.setFrames(animations.get(1));
            animation.setDelay(6);
        }
        mzm.setPosition(x - gs.getWidth() / 2, y - gs.getHeight() / 2);
        mzm.setRotation(rotation);
    }
    
    public void checkCollisions() {
        if(left) {
            rotation -= 5;
        }
        if(right) {
            rotation += 5;
        }
        if(up) {
            dx = 6.5 * Math.cos(Math.toRadians(rotation-90));
            dy = 6.5 * Math.sin(Math.toRadians(rotation-90));
        }
        else if(down) {
            dx = -6.5 * Math.cos(Math.toRadians(rotation-90));
            dy = -6.5 * Math.sin(Math.toRadians(rotation-90));
        }
        else {
            dx = 0;
            dy = 0;
        }
        int xblocks = (int) ((x + 17) / 32.0) - (int) (x / 32.0) + 1;
        int yblocks = (int) ((y + 31) / 32.0) - (int) (y / 32.0) + 1;
        for(int i = 0; i < yblocks; i++) {
            if(dx > 0 && Math.abs(rotation - 90) != 90) {
                if(mzm.isSolid(x + 17 + dx, y + i * 32)) {
                    x = (int) ((x + 17 + dx) / 32.0) * 32.0 - 18;
                    dx = 0;
                }
                if(mzm.isFlower(x + 17 + dx, y + i * 32)) {
                    maze.finish();
                }
            }
            else if(dx < 0 && Math.abs(rotation - 90) != 90) {
                if(mzm.isSolid(x + dx, y + i * 32)) {
                    x = (int) ((x + dx) / 32.0 + 1) * 32.0;
                    dx = 0;
                }
                if(mzm.isFlower(x + dx, y + i * 32)) {
                    maze.finish();
                }
            }
        }
        for(int i = 0; i < xblocks; i++) {
            if(dy < 0 && Math.abs(rotation - 90) != 180) {
                if(mzm.isSolid(x + i * 32, y + dy)) {
                    y = (int) ((y + dy) / 32.0 + 1) * 32.0;
                    dy = 0;
                }
                if(mzm.isFlower(x + i * 32, y + dy)) {
                    maze.finish();
                }
            }
            else if(dy > 0 && Math.abs(rotation - 90) != 180) {
                if(mzm.isSolid(x + i * 32, y + 31 + dy)) {
                    y = (int) ((y + 31 + dy) / 32.0) * 32.0 - 32;
                    dy = 0;
                }
                if(mzm.isFlower(x + i * 32, y + 31 + dy)) {
                    maze.finish();
                }
            }
        }
        x += dx;
        y += dy;
    }
    
    public void checkForKeys() {
        up = Controler.isUp();
        down = Controler.isDown();
        left = Controler.isLeft();
        right = Controler.isRight();
    }
    
    public void paint(Graphics2D g) {
        AffineTransform af = g.getTransform();
        g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rotation), (int) x + 17 / 2.0 - mzm.getX(), (int) y + 31 / 2.0 - mzm.getY()));
        g.drawImage(animation.getImage(), (int) x - mzm.getX(), (int) y - mzm.getY(), null);
        g.setTransform(af);
    }
    
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
        if(canMove) rotation = 180;
        if(!canMove) left = right = up = down = false;
    }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void woof() {
        JukeBox.play("woof");
    }
    
    public void addRotation(int r) {
        rotation += r;
        mzm.setRotation(rotation);
    }
}