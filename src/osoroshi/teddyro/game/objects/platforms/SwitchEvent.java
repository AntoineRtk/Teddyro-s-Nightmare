package osoroshi.teddyro.game.objects.platforms;

import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.BouncingBallPowerBox;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.objects.mobs.Bat;
import osoroshi.teddyro.game.objects.mobs.Skeleton;
import osoroshi.teddyro.game.objects.mobs.Slug;
import osoroshi.teddyro.game.objects.mobs.Snail;
import osoroshi.teddyro.game.objects.mobs.Squid;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.LavaTile;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class SwitchEvent extends Platform {
    
    private BufferedImage image;
    private int action = -1, count = -1;
    private int[][] wl;
    
    public SwitchEvent(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, null);
        image = GameResource.getSwitchEventSprites().get(0)[0];
        animation.setFrames(new BufferedImage[]{image});
        width = 32;
        height = 24;
        fallingSpeed = 4;
        maxFallingSpeed = 16;
        if(params != null) {
            this.action = Integer.parseInt(params.split("-")[0]);
            if(params.split("-").length > 1) {
                wl = new int[params.split("-").length / 2][2];
                for(int i = 1; i < params.split("-").length; i += 2) {
                    this.wl[i / 2] = new int[]{Integer.parseInt(params.split("-")[i]), Integer.parseInt(params.split("-")[i + 1])};
                }
            }
        }
    }
    
    public void update() {
        super.checkCollisions();
        if(count != -1) {
            if(action == 0) {
                if(count > 1119) {
                    int id = (new java.util.Random().nextBoolean()) ? 23 : 34;
                    for(int col = 0; col < tileMap.getMapHeight(); col += 32) {
                        for(int row = 0; row < tileMap.getMapWidth(); row += 32) {
                            if(tileMap.getTile(row, col) instanceof LavaTile) {
                                tileMap.setTile(GameResource.getTile(id), row / 32, col / 32);
                            }
                        }
                    }
                    count = -1;
                }
            }
            if(action == 1) {
                if(count > 5999) {
                    height = 24;
                    y -= 19;
                    count = -1;
                    for(int i = 0; i <= new java.util.Random().nextInt(3); i++) {
                        switch(new java.util.Random().nextInt(5)) {
                            case 0:
                                tileMap.addEnemy(new Snail(gl, tileMap, tileMap.hero.getX() - 50 + new java.util.Random().nextInt(100 + tileMap.hero.width), tileMap.hero.getY() - 100));
                                break;
                            case 1:
                                tileMap.addEnemy(new Bat(gl, tileMap, tileMap.hero.getX() - 50 + new java.util.Random().nextInt(100 + tileMap.hero.width), tileMap.hero.getY() - 100));
                                break;
                            case 2:
                                tileMap.addEnemy(new Squid(gl, tileMap, tileMap.hero.getX() - 50 + new java.util.Random().nextInt(100 + tileMap.hero.width), tileMap.hero.getY() - 100));
                                break;
                            case 3:
                                tileMap.addEnemy(new Skeleton(gl, tileMap, tileMap.hero.getX() - 50 + new java.util.Random().nextInt(100 + tileMap.hero.width), tileMap.hero.getY() - 100));
                                break;
                            case 4:
                                tileMap.addEnemy(new Slug(gl, tileMap, tileMap.hero.getX() - 50 + new java.util.Random().nextInt(100 + tileMap.hero.width), tileMap.hero.getY() - 100));
                                break;
                        }
                    }
                }
            }
            count++;
        }
    }
    
    public boolean intersectY(MapObject mo, int dir) {
        if(height == 5 || dir != 1) return true;
        JukeBox.play("switch");
        mo.losePlatform(this);
        y += height - 5;
        height = 5;
        switch(action) {
            case 0:
                JukeBox.play("climatechange");
                gl.flash(3000);
                count = 0;
                break;
            case 1:
                count = 0;
                break;
            case 2:
                tileMap.addObject(new BouncingBallPowerBox(gl, tileMap, getX() + width / 2 - 15, getY() - 500));
                break;
            case 3:
                count = 0;
                break;
            case 4:
                for(int i = 0; i < tileMap.platforms[area].size(); i++) {
                    if(tileMap.platforms[area].get(i) instanceof ColoredBlock) {
                        ColoredBlock c = (ColoredBlock) tileMap.platforms[area].get(i);
                        if(c instanceof GreenBlock) {
                            c.setSolid(true);
                        }
                        else {
                            c.setSolid(false);
                        }
                    }
                }
                break;
            case 5:
                for(int i = 0; i < tileMap.platforms[area].size(); i++) {
                    if(tileMap.platforms[area].get(i) instanceof ColoredBlock) {
                        ColoredBlock c = (ColoredBlock) tileMap.platforms[area].get(i);
                        if(c instanceof GreenBlock) {
                            c.setSolid(false);
                        }
                        else {
                            c.setSolid(true);
                        }
                    }
                }
                break;
            case 6:
                for(int i = 0; i < wl.length; i++) {
                    tileMap.getWallAt(wl[i][0], wl[i][1]).open();
                }
                break;
            case 7:
                if(tileMap.mobs[area].isEmpty()) break;
                tileMap.addObject(new SpikeBall(gl, tileMap, tileMap.mobs[area].get(0).getX() + tileMap.mobs[area].get(0).width / 2.0 - 32.0, tileMap.getY() - 64));
                tileMap.addPlatform(new InvisiblePlatform(gl, tileMap, tileMap.mobs[area].get(0).getX(), tileMap.mobs[area].get(0).getY() - 1, tileMap.mobs[area].get(0).width, 1));
                tileMap.removePlatform(this);
                tileMap.hero.setCanAction(false);
                tileMap.hero.setFlipped(tileMap.mobs[area].get(0).getX() < tileMap.hero.getX());
                break;
        }
        return true;
    }
}