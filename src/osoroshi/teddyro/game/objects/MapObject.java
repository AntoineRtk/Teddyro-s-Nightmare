package osoroshi.teddyro.game.objects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.animations.Animation;
import osoroshi.teddyro.game.objects.mobs.Monster;
import osoroshi.teddyro.game.objects.platforms.Platform;
import osoroshi.teddyro.game.tilemap.Slope;
import osoroshi.teddyro.game.tilemap.SpecialTile;
import osoroshi.teddyro.game.tilemap.Tile;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class MapObject {
    
    public TileMap tileMap;
    private int dirx, diry;
    public double x, y, dx, dy, rotation, xIncreasing, yIncreasing, jsa;
    public double moveSpeed, maxMoveSpeed, stopSpeed;
    public double jumpStart, fallingSpeed, maxFallingSpeed;
    public int width, height, blocksWidth, blocksHeight, area, sx, sy;
    private float opacity = 1.0f;
    private boolean jumping, falling, flipped, intersectedX = false, intersectedY = false, forcedJump = false;
    public Animation animation = new Animation();
    private Slope slope;
    private Platform p;
    
    public MapObject(TileMap tileMap, double x, double y) {
        this.tileMap = tileMap;
        this.x = x;
        this.y = y;
    }
    
    public void update() {
        animation.update();
    }
    
    public void checkCollisions() {
        blocksWidth = width == 0 ? 0 : 1 + (int) ((getX() + width - 1) / 32.0) - (int) (getX() / 32.0);
        blocksHeight = height == 0 ? 0 : 1 + (int) ((getY() + height - 1) / 32.0) - (int) (getY() / 32.0);
        falling = fallingSpeed != 0 && p == null;
        for(int i = 0; i < blocksWidth * 32; i += 32) {
            if(isSolid(getX() + i, getY() + height) && dy == 0) {
                falling = false;
                break;
            }
        }
        if(falling) {
            dy += fallingSpeed;
            if(dy >= maxFallingSpeed) {
                dy = maxFallingSpeed;
            }
        }
        switch(getDirX()) {
            case -1:
                dx -= moveSpeed;
                if(dx < -maxMoveSpeed) dx = -maxMoveSpeed;
                break;
            case 1:
                dx += moveSpeed;
                if(dx > maxMoveSpeed) dx = maxMoveSpeed;
                break;
            case 0:
                if(dx > 0) {
                    dx -= moveSpeed;
                    if(dx < 0) dx = 0;
                }
                if(dx < 0) {
                    dx += moveSpeed;
                    if(dx > 0) dx = 0;
                }
                break;
        }
        if((jumping && !falling) || forcedJump) {
            dy = jumpStart + jsa;
            if(jsa != 0) jsa = 0;
            jumping = false;
            falling = true;
            slope = null;
            p = null;
            if(forcedJump) forcedJump = false;
        }
        if(p != null) {
            addXImpulse(p.dx);
            if(p != null) setY(p.getY() - height);
        }
        intersectedX = intersectedY = false;
        int max = (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy)));
        if(max == 0) max = 1;
        xIncreasing = dx / max;
        yIncreasing = dy / max;
        double lastX = getX(), ldx = dx, lastY = getY(), ldy = dy;
        for(int m = 0; m < max; m++) {
            if(dx != 0 && dx == ldx) {
                setX(lastX + xIncreasing * (m + 1));
                if(slope != null && getY() >= sy - height) {
                    if(slope.isUpward()) {
                        double yp = (slope.getStartY() - slope.getEndY()) / 32.0 * (getX() + width - 1 - sx);
                        if(yp < 0) yp = 0;
                        if(yp > 32) yp = 32;
                        setY(sy - height + Math.max(slope.getStartY(), slope.getEndY()) - yp);
                        if(getY() < sy - height) setY(sy - height);
                    }
                    else {
                        double dif = slope.getEndY() - slope.getStartY();
                        double yp = dif - dif / 32.0 * (getX() - sx);
                        if(yp < 0) yp = 0;
                        if(yp > 32) yp = 32;
                        setY(sy - height + Math.max(slope.getStartY(), slope.getEndY())  - yp);
                        if(getY() < sy - height) setY(sy - height);
                    }
                    blocksHeight = height == 0 ? 0 : 1 + (int) ((getY() + height - 1) / 32.0) - (int) (getY() / 32.0);
                }
                for(int h = 0; h < blocksHeight * 32; h += 32) {
                    if(dx > 0) {
                        Tile tile = tileMap.getTile(((moveSpeed < 1) ? 1 - moveSpeed : 0) + getX() + width - 1, getY() + h);
                        if(tile instanceof SpecialTile) {
                            ((SpecialTile) tile).doAction(this, true, 1);
                        }
                        if(tile.isSolid()) {
                            if(tile.isSlope()) {
                                if(getY() + height - 1 < getSlopeY(tile, ((moveSpeed < 1) ? 1 - moveSpeed : 0) + getX() + width - 1, getY() + h)) {
                                    slope = (Slope) tile;
                                    sx = (int) ((((moveSpeed < 1) ? 1 - moveSpeed : 0) + getX() + width - 1) / 32.0) * 32;
                                    sy = (int) ((getY() + h) / 32.0) * 32;
                                    blocksHeight = height == 0 ? 0 : 1 + (int) ((getY() + height - 1) / 32.0) - (int) (getY() / 32.0);
                                    continue;
                                }
                            }
                            setX((int) ((((moveSpeed < 1) ? 1 - moveSpeed : 0) + getX() + width) / 32.0) * 32 - width);
                            dx = 0;
                            intersectedX = true;
                        }
                    }
                    else if(dx < 0) {
                        Tile tile = tileMap.getTile(getX(), getY() + h);
                        if(Math.signum(getX()) == -1) {
                            setX(0);
                            dx = 0;
                            intersectedX = true;
                            continue;
                        }
                        if(tile instanceof SpecialTile) {
                            ((SpecialTile) tile).doAction(this, true, -1);
                        }
                        if(tile.isSolid()) {
                            if(tile.isSlope()) {
                                //double yslope = (int) ((getY() + h) / 32.0) * 32 + 32 - Math.max(tile.getStartY(), tile.getEnd) * (getX() + width - 1);
                                if(getY() + height - 1 < getSlopeY(tile, getX(), getY() + h)) {
                                    slope = (Slope) tile;
                                    sx = (int) (getX() / 32.0) * 32;
                                    sy = (int) ((getY() + h) / 32.0) * 32;
                                    blocksHeight = height == 0 ? 0 : 1 + (int) ((getY() + height - 1) / 32.0) - (int) (getY() / 32.0);
                                    continue;
                                }
                            }
                            setX((int) ((getX() - 1) / 32.0 + 1) * 32);
                            dx = 0;
                            intersectedX = true;
                        }
                    }
                }
                if(slope != null) {
                    if(getX() >= sx + 32) {
                        if(!slope.isUpward() && getY() >= sy - height + Math.max(slope.getStartY(), slope.getEndY())) {
                            setY(sy - height + Math.max(slope.getStartY(), slope.getEndY()));
                        }
                        slope = null;
                    }
                    if(getX() + width - 1 >= sx + 32 && slope != null && slope.isUpward()) slope = null;
                    if(getX() + width - 1 < sx && slope != null) {
                        if(slope.isUpward() && getY() >= sy - height + Math.max(slope.getStartY(), slope.getEndY())) setY(sy - height + Math.max(slope.getStartY(), slope.getEndY()));
                        slope = null;
                    }
                }
                for(int i = 0; i < tileMap.platforms[area].size(); i++) {
                    Platform platform = tileMap.platforms[area].get(i);
                    if(platform == this) continue;
                    if(dx < 0) {
                        if(((getY() >= platform.getY() && getY() < platform.getY() + platform.height) || (platform.getY() >= getY() && platform.getY() < getY() + height)) &&
                                (getX() >= platform.getX() && getX() < platform.getX() + platform.width)) {
                            setX(platform.getX() + platform.width);
                            if(platform.intersectX(this, -1)) dx = 0;
                            intersectedX = true;
                        }
                    }
                    else if(dx > 0) {
                        if(((getY() >= platform.getY() && getY() < platform.getY() + platform.height) || (platform.getY() >= getY() && platform.getY() < getY() + height)) &&
                                (getX() + width > platform.getX() && getX() + width < platform.getX() + platform.width)) {
                            setX(platform.getX() - width);
                            if(platform.intersectX(this, 1)) dx = 0;
                            intersectedX = true;
                        }
                    }
                }
                if(this instanceof Hero && ((Hero) this).isRolling()) {
                    ((Hero) this).checkCollisionsWhileRolling();
                }
                xAxisVerification();
                blocksWidth = width == 0 ? 0 : 1 + (int) ((getX() + width - 1) / 32.0) - (int) (getX() / 32.0);
                checkForHigherSlope(blocksWidth);
            }
            if(p != null) {
                if(getX() > p.getX() + p.width - 1 || getX() + width - 1 < p.getX()) {
                    losePlatform(p);
                }
            }
            if(dy != 0 && dy == ldy) {
                setY(lastY + yIncreasing * (m + 1));
                for(int w = 0; w < blocksWidth * 32; w += 32) {
                    if(dy > 0) {
                        Tile tile = tileMap.getTile(getX() + w, getY() + height - 1);
                        if(tile instanceof SpecialTile) {
                            ((SpecialTile) tile).doAction(this, false, 1);
                        }
                        if(tile.isSolid()) {
                            if(tile.isSlope()) {
                                int tilex = (int) ((getX() + w) / 32.0) * 32;
                                int tiley = (int) ((getY() + height - 1) / 32.0) * 32;
                                double yintersection = 0;
                                if(tile.isUpward()) {
                                    double yp = (tile.getStartY() - tile.getEndY()) / 32.0 * (getX() + width - 1 - tilex);
                                    if(yp < 0) yp = 0;
                                    if(yp > 32) yp = 32;
                                    yintersection = tiley + Math.max(tile.getStartY(), tile.getEndY()) - yp;
                                    if(yintersection < tiley) yintersection = tiley;
                                }
                                else {
                                    double dif = tile.getEndY() - tile.getStartY();
                                    double yp = dif - dif / 32.0 * (getX() - tilex);
                                    if(yp < 0) yp = 0;
                                    if(yp > 32) yp = 32;
                                    yintersection = tiley + Math.max(tile.getStartY(), tile.getEndY()) - yp;
                                    if(yintersection < tiley) yintersection = tiley;
                                }
                                if(getY() + height - 1 >= yintersection) {
                                    setY(yintersection - height);
                                    slope = (Slope) tile;
                                    sx = tilex;
                                    sy = tiley;
                                    dy = 0;
                                }
                                continue;
                            }
                            setY((int) ((getY() + height - 1) / 32.0) * 32 - height);
                            dy = 0;
                            falling = false;
                            intersectedY = true;
                        }
                    }
                    else if(dy < 0) {
                        if(Math.signum(getY()) == -1) {
                            setY(0);
                            dy = 0;
                            intersectedY = true;
                            continue;
                        }
                        if(tileMap.getTile(getX() + w, getY()) instanceof SpecialTile) {
                            ((SpecialTile) tileMap.getTile(getX() + w, getY())).doAction(this, false, -1);
                        }
                        if(isSolid(getX() + w, getY()) && (tileMap.getTile(getX() + w, getY()) != tileMap.getTile(getX() + w, getY() + height - 1))) {
                            setY(((int) (getY() / 32.0) + 1) * 32);
                            dy = 0;
                            intersectedY = true;
                        }
                    }
                }
                if(this instanceof Hero && dy > 0) {
                    ((Hero) this).checkCollisionsWhileFalling();
                }
                for(int i = 0; i < tileMap.platforms[area].size(); i++) {
                    Platform platform = tileMap.platforms[area].get(i);
                    if(platform == this) continue;
                    if(dy < 0) {
                        if(((getX() >= platform.getX() && getX() < platform.getX() + platform.width) || (platform.getX() >= getX() && platform.getX() < getX() + width)) &&
                                (getY() >= platform.getY() && getY() < platform.getY() + platform.height)) {
                            setY(platform.getY() + platform.height);
                            if(platform.intersectY(this, -1)) dy = 0;
                            intersectedY = true;
                        }
                    }
                    else if(dy > 0) {
                        if(((getX() >= platform.getX() && getX() < platform.getX() + platform.width) || (platform.getX() >= getX() && platform.getX() <= getX() + width - 1)) &&
                                (getY() + height > platform.getY() && getY() + height < platform.getY() + platform.height)) {
                            setPlatform(platform);
                            intersectedY = true;
                        }
                    }
                    if(dy >= 0) {
                        if(((getX() >= platform.getX() && getX() <= platform.getX() + platform.width - 1) || (platform.getX() >= getX() && platform.getX() <= getX() + width - 1)) &&
                                (getY() + height >= platform.getY() && getY() + height < platform.getY() + platform.height)) {
                            setPlatform(platform);
                            intersectedY = true;
                        }
                    }
                }
                if(this instanceof Hero && getY() + height == tileMap.getMapHeight() + 32) {
                    ((Hero) this).checkCollisionsWhileFalling();
                }
            }
            if(this instanceof Monster) ((Monster) this).checkIntersections();
            for(int i = 0; i < tileMap.objects[area].size(); i++) {
                if(intersects(tileMap.objects[area].get(i).getShape()) && tileMap.objects[area].get(i) != this) {
                    tileMap.objects[area].get(i).intersect(this);
                }
            }
            for(int i = 0; i < blocksWidth * 32; i += 32) {
                for(int i1 = 0; i1 < blocksHeight * 32; i1 += 32) {
                    if(tileMap.getTile(getX() + i, getY() + i1) instanceof SpecialTile) {
                        ((SpecialTile) tileMap.getTile(getX() + i, getY() + i1)).doAction(this, false, 0);
                    }
                }
                if(tileMap.getTile(getX() + i, getY() + height) instanceof SpecialTile) {
                    ((SpecialTile) tileMap.getTile(getX() + i, getY() + height)).doAction(this, false, 0);
                }
            }
            yAxisVerification();
            blocksHeight = height == 0 ? 0 : 1 + (int) ((getY() + height - 1) / 32.0) - (int) (getY() / 32.0);
        }
    }
    
    public void addXImpulse(double xa) {
        int max = (int) Math.ceil(Math.abs(xa));
        double xImpulse = xa / max;
        double lastX = getX();
        boolean in = intersectedX;
        intersectedX = false;
        for(int m = 0; m < max; m++) {
            setX(lastX + xImpulse * (m + 1));
            if(slope != null && getY() >= sy - height) {
                if(slope.isUpward()) {
                    double yp = (slope.getStartY() - slope.getEndY()) / 32.0 * (getX() + width - 1 - sx);
                    if(yp < 0) yp = 0;
                    if(yp > 32) yp = 32;
                    setY(sy - height + Math.max(slope.getStartY(), slope.getEndY()) - yp);
                    if(getY() < sy - height) setY(sy - height);
                }
                else {
                    double dif = slope.getEndY() - slope.getStartY();
                    double yp = dif - dif / 32.0 * (getX() - sx);
                    if(yp < 0) yp = 0;
                    if(yp > 32) yp = 32;
                    setY(sy - height + Math.max(slope.getStartY(), slope.getEndY())  - yp);
                    if(getY() < sy - height) setY(sy - height);
                }
                blocksHeight = height == 0 ? 0 : 1 + (int) ((getY() + height - 1) / 32.0) - (int) (getY() / 32.0);
            }
            for(int h = 0; h < blocksHeight * 32; h += 32) {
                if(xa > 0) {
                    Tile tile = tileMap.getTile(((moveSpeed < 1) ? 1 - moveSpeed : 0) + getX() + width - 1, getY() + h);
                    if(tile instanceof SpecialTile) {
                        ((SpecialTile) tile).doAction(this, true, 1);
                    }
                    if(tile.isSolid()) {
                        if(tile.isSlope()) {
                            if(getY() + height - 1 < getSlopeY(tile, ((moveSpeed < 1) ? 1 - moveSpeed : 0) + getX() + width - 1, getY() + h)) {
                                slope = (Slope) tile;
                                sx = (int) ((((moveSpeed < 1) ? 1 - moveSpeed : 0) + getX() + width - 1) / 32.0) * 32;
                                sy = (int) ((getY() + h) / 32.0) * 32;
                                blocksHeight = height == 0 ? 0 : 1 + (int) ((getY() + height - 1) / 32.0) - (int) (getY() / 32.0);
                                continue;
                            }
                        }
                        setX((int) ((((moveSpeed < 1) ? 1 - moveSpeed : 0) + getX() + width) / 32.0) * 32 - width);
                        xImpulse = 0;
                        intersectedX = true;
                    }
                }
                else if(xa < 0) {
                    Tile tile = tileMap.getTile(getX(), getY() + h);
                    if(Math.signum(getX()) == -1) {
                        setX(0);
                        xImpulse = 0;
                        intersectedX = true;
                        continue;
                    }
                    if(tile instanceof SpecialTile) {
                        ((SpecialTile) tile).doAction(this, true, -1);
                    }
                    if(tile.isSolid()) {
                        if(tile.isSlope()) {
                            if(getY() + height - 1 < getSlopeY(tile, getX(), getY() + h)) {
                                slope = (Slope) tile;
                                sx = (int) (getX() / 32.0) * 32;
                                sy = (int) ((getY() + h) / 32.0) * 32;
                                blocksHeight = height == 0 ? 0 : 1 + (int) ((getY() + height - 1) / 32.0) - (int) (getY() / 32.0);
                                continue;
                            }
                        }
                        setX((int) ((getX() - 1) / 32.0 + 1) * 32);
                        xImpulse = 0;
                        intersectedX = true;
                    }
                }
            }
            if(getX() >= sx + 32 && slope != null && !slope.isUpward()) {
                setY(sy - height + Math.max(slope.getStartY(), slope.getEndY()));
                slope = null;
            }
            if(getX() + width - 1 >= sx + 32 && slope != null && slope.isUpward()) slope = null;
            if(getX() + width - 1 < sx && slope != null) {
                if(slope.isUpward()) setY(sy - height + Math.max(slope.getStartY(), slope.getEndY()));
                slope = null;
            }
            for(int i = 0; i < tileMap.platforms[area].size(); i++) {
                Platform platform = tileMap.platforms[area].get(i);
                if(platform == this) continue;
                if(xa < 0) {
                    if(((getY() >= platform.getY() && getY() < platform.getY() + platform.height) || (platform.getY() >= getY() && platform.getY() < getY() + height)) &&
                            (getX() >= platform.getX() && getX() < platform.getX() + platform.width)) {
                        setX(platform.getX() + platform.width);
                        if(platform.intersectX(this, -1)) xImpulse = 0;
                        intersectedX = true;
                    }
                }
                else if(xa > 0) {
                    if(((getY() >= platform.getY() && getY() < platform.getY() + platform.height) || (platform.getY() >= getY() && platform.getY() < getY() + height)) &&
                            (getX() + width > platform.getX() && getX() + width < platform.getX() + platform.width)) {
                        setX(platform.getX() - width);
                        if(platform.intersectX(this, 1)) xImpulse = 0;
                        intersectedX = true;
                    }
                }
            }
            if(this instanceof Hero && ((Hero) this).isRolling()) ((Hero) this).checkCollisionsWhileRolling();
            xAxisVerification();
            blocksWidth = width == 0 ? 0 : 1 + (int) ((getX() + width - 1) / 32.0) - (int) (getX() / 32.0);
            checkForHigherSlope(blocksWidth);
        }
        if(p != null) {
            if(getX() > p.getX() + p.width - 1 || getX() + width - 1 < p.getX()) {
                losePlatform(p);
            }
        }
        if(in) intersectedX = true;
    }
    
    public void addYImpulse(double ya) {
        int max = (int) Math.ceil(Math.abs(ya));
        double yImpulse = ya / max;
        double lastY = getY();
        boolean in = intersectedY;
        intersectedY = false;
        for(int m = 0; m < max; m++) {
            setY(lastY + yImpulse * (m + 1));
            for(int w = 0; w < blocksWidth * 32; w += 32) {
                if(ya > 0) {
                    if(tileMap.getTile(getX() + w, getY() + height - 1) instanceof SpecialTile) {
                        ((SpecialTile) tileMap.getTile(getX() + w, getY() + height - 1)).doAction(this, false, 1);
                    }
                    if(isSolid(getX() + w, getY() + height - 1)) {
                        setY((int) ((getY() + height - 1) / 32.0) * 32 - height);
                        yImpulse = 0;
                        intersectedY = true;
                    }
                }
                else if(ya < 0) {
                    if(Math.signum(getY()) == -1) {
                        setY(0);
                        yImpulse = 0;
                        intersectedY = true;
                        continue;
                    }
                    if(tileMap.getTile(getX() + w, getY()) instanceof SpecialTile) {
                        ((SpecialTile) tileMap.getTile(getX() + w, getY())).doAction(this, false, -1);
                    }
                    if(isSolid(getX() + w, getY()) && (tileMap.getTile(getX() + w, getY()) != tileMap.getTile(getX() + w, getY() + height - 1))) {
                        setY(((int) (getY() / 32.0) + 1) * 32);
                        yImpulse = 0;
                        intersectedY = true;
                        return;
                    }
                }
            }
            if(this instanceof Hero && dy > 0) {
                ((Hero) this).checkCollisionsWhileFalling();
            }
            for(int i = 0; i < tileMap.platforms[area].size(); i++) {
                Platform platform = tileMap.platforms[area].get(i);
                if(platform == this) continue;
                if(ya < 0) {
                    if(((getX() >= platform.getX() && getX() < platform.getX() + platform.width) || (platform.getX() >= getX() && platform.getX() < getX() + width)) &&
                            (getY() >= platform.getY() && getY() < platform.getY() + platform.height)) {
                        setY(platform.getY() + platform.height);
                        if(platform.intersectY(this, -1)) yImpulse = 0;
                        intersectedY = true;
                    }
                }
                else if(ya > 0) {
                    if(((getX() >= platform.getX() && getX() < platform.getX() + platform.width) || (platform.getX() >= getX() && platform.getX() <= getX() + width - 1)) &&
                            (getY() + height > platform.getY() && getY() + height < platform.getY() + platform.height)) {
                        p = platform;
                        setY(p.getY() - height);
                        if(platform.intersectY(this, 1)) yImpulse = 0;
                        falling = false;
                        intersectedY = true;
                    }
                }
                if(ya >= 0) {
                    if(((getX() >= platform.getX() && getX() <= platform.getX() + platform.width - 1) || (platform.getX() >= getX() && platform.getX() <= getX() + width - 1)) &&
                            (getY() + height >= platform.getY() && getY() + height < platform.getY() + platform.height)) {
                            p = platform;
                            setY(p.getY() - height);
                            if(platform.intersectY(this, 1)) dy = 0;
                            falling = false;
                            intersectedY = true;
                    }
                }
            }
            yAxisVerification();
        }
        if(in) intersectedY = true;
    }
    
    public double getSlopeY(Tile tile, double ssx, double ssy) {
        double slopey;
        ssx = (int) (ssx / 32.0) * 32;
        ssy = (int) (ssy / 32.0) * 32;
        if(tile.isUpward()) {
            double yp = (tile.getStartY() - tile.getEndY()) / 32.0 * (getX() + width - 1 - ssx);
            if(yp < 0) yp = 0;
            if(yp > 32) yp = 32;
            slopey = ssy + Math.max(tile.getStartY(), tile.getEndY()) - yp;
            if(slopey < ssy) slopey = ssy;
        }
        else {
            double dif = tile.getEndY() - tile.getStartY();
            if(getX() - ssx < 0) return ssy + Math.max(tile.getStartY(), tile.getEndY());
            double yp = dif - dif / 32.0 * (getX() - ssx);
            if(yp < 0) yp = 0;
            if(yp > 32) yp = 32;
            slopey = ssy + Math.max(tile.getStartY(), tile.getEndY()) - yp;
            if(slopey < ssy) slopey = ssy;
        }
        return slopey;
    }
    
    public void checkForHigherSlope(int blocksWidth) {
        double maxy = -1;
        int index = -1;
        for(int i = 0; i < blocksWidth * 32; i += 32) {
            if(isSlope(getX() + i, getY() + height) && slope == null) {
                Slope s = (Slope) tileMap.getTile(getX() + i, getY() + height);;
                double ssx = (int) ((getX() + i) / 32.0) * 32;
                double ssy = (int) ((getY() + height) / 32.0) * 32;
                if(s.isUpward()) {
                    double yp = (s.getStartY() - s.getEndY()) / 32.0 * (getX() + width - 1 - ssx);
                    if(yp < 0) yp = 0;
                    if(yp > 32) yp = 32;
                    double yslope = ssy - height + Math.max(s.getStartY(), s.getEndY()) - yp;
                    if(yslope < ssy - height) yslope = sy - height;
                    if(maxy == -1 || yslope < maxy) {
                        maxy = yslope;
                        index = i;
                    }
                }
                else {
                    double dif = s.getEndY() - s.getStartY();
                    double yp = dif - dif / 32.0 * (getX() - ssx);
                    if(yp < 0) yp = 0;
                    if(yp > 32) yp = 32;
                    double yslope = ssy - height + Math.max(s.getStartY(), s.getEndY()) - yp;
                    if(getY() < ssy - height) yslope = sy - height;
                    if(maxy == -1 || yslope < maxy) {
                        maxy = yslope;
                        index = i;
                    }
                }
            }
        }
        if(index != -1) {
            slope = (Slope) tileMap.getTile(getX() + index, getY() + height);
            sx = (int) ((getX() + index) / 32.0) * 32;
            sy = (int) ((getY() + height) / 32.0) * 32;
        }
    }
    
    public void paint(Graphics2D g) {
        AffineTransform af = g.getTransform();
        Composite c = g.getComposite();
        g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rotation), getX() - tileMap.getX() + width / 2.0, getY() - tileMap.getY() + height / 2.0));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        if(!flipped) {
            g.drawImage(animation.getImage(), (int) getX() - tileMap.getX(), (int) getY() - tileMap.getY(), width, height, null);
        }
        else {
            g.drawImage(animation.getImage(), (int) getX() - tileMap.getX() + width, (int) getY() - tileMap.getY(), -width, height, null);
        }
        if(GameResource.showOutlines()) {
            g.setColor(Color.red);
            g.translate(getX() - tileMap.getX(), getY() - tileMap.getY());
            //g.drawRect((int) getX() - tileMap.getX(), (int) getY() - tileMap.getY(), width, height);
            double lx = x, ly = y;
            x = y = 0;
            g.draw(getShape());
            x = lx;
            y = ly;
            g.translate(-getX() + tileMap.getX(), -getY() + tileMap.getY());
        }
        g.setComposite(c);
        g.setTransform(af);
    }
    
    public void setPlatform(Platform platform) {
        p = platform;
        setY(p.getY() - height);
        if(platform.intersectY(this, 1)) dy = 0;
        falling = false;
    }
    
    public void xAxisVerification() {}
    
    public void yAxisVerification() {}
    
    public BufferedImage getImage() {
        return animation.getImage();
    }
    
    private boolean isSolid(double x, double y) {
        return tileMap.isSolid(x, y);
    }
    
    private boolean isSlope(double x, double y) {
        return tileMap.getTile(x, y).isSlope();
    }
    
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
    
    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }
    
    public void jump() {
        forcedJump = true;
    }
    
    public void jump(double jumpStart) {
        jsa = jumpStart - this.jumpStart;
        jump();
    }
    
    public void setFalling(boolean falling) {
        this.falling = falling;
    }
    
    public boolean isJumping() {
        return this.jumping;
    }
    
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
    
    public void losePlatform(Platform platform) {
        if(p != null && p == platform) {
            p = null;
        }
    }
    
    public Platform getPlatform() {
        return this.p;
    }
    
    public void stopYIncreasing() {
        yIncreasing = 0;
    }
    
    public float getOpacity() {
        return opacity;
    }
    
    public boolean isFlipped() {
        return flipped;
    }
    
    public boolean isFalling() {
        return falling;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public void setDirX(int dirx) {
        this.dirx = dirx;
    }
    
    public void setDirY(int diry) {
        this.diry = diry;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public int getDirX() {
        return dirx;
    }
    
    public int getDirY() {
        return diry;
    }
    
    public boolean intersects(MapObject mo) {
        return mo.getShape().intersects(x, y, width, height);
    }
    
    public boolean intersects(Shape r) {
        return r.intersects(x, y, width, height);
    }
    
    public Shape getShape() {
        return new Rectangle2D.Double(getX(), getY(), width, height);
    }
    
    public void setArea(int area) {
        this.area = area;
        p = null;
        slope = null;
    }
    
    public int getArea() {
        return area;
    }
    
    public boolean hasIntersectedInX() {
        return intersectedX;
    }
    
    public boolean hasIntersectedInY() {
        return intersectedY;
    }
    
    public int getSX() {
        return sx;
    }
    
    public int getSY() {
        return sy;
    }
}