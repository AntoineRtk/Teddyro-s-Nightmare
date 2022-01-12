package osoroshi.teddyro.game.objects;

import osoroshi.teddyro.game.animations.DustExplosion;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;

public class FallingRockGenerator extends MapElement {
    
    private int count = 150;
    private int range = 80 + new java.util.Random().nextInt(100);
    
    public FallingRockGenerator(GameLevel gl, TileMap tileMap, double x, double y) {
        super(gl, tileMap, x, y, null);
    }
    
    public void update() {
        super.update();
        count++;
        if((tileMap.hero.getX() > getX() - range && tileMap.hero.getX() < getX() + range) || (tileMap.hero.getX() + tileMap.hero.width > getX() - range && tileMap.hero.getX() + tileMap.hero.width < getX() - range)) {
            if(count > 150 && tileMap.hero.getY() > getY()) {
                count = 0;
                FallingRock f = new FallingRock(gl, this.tileMap, this.x + 16 - 15 / 2.0, this.y);
                tileMap.addObject(f);
            }
        }
    }
    
    public class FallingRock extends MapElement {
        
        public FallingRock(GameLevel gl, TileMap tileMap, double x, double y) {
            super(gl, tileMap, x, y, null);
            fallingSpeed = 1.23505;
            maxFallingSpeed = Double.MAX_VALUE;
            width = 15;
            height = 15;
            animation.setFrames(GameResource.getRockSprites().get(0));
            rotation = new java.util.Random().nextInt(361);
        }
        
        public void update() {
            super.update();
            super.checkCollisions();
            rotation += 3.5;
            if(intersects(tileMap.hero)) {
                tileMap.hero.hit(1);
                destroyRock(false);
            }
            if(dy == 0) {
                destroyRock(true);
            }
        }
        
        public void destroyRock(boolean tile) {
            for(int i = 0; i < 15; i += 5) {
                for(int i1 = 0; i1 < 15; i1 += 5) {
                    DustExplosion d = new DustExplosion(gl, tileMap, getImage().getSubimage(i, i1, 5, 5), getX() + i, getY() + i1, tile, 5, 5);
                    tileMap.addObject(d);
                }
            }
            tileMap.removeObject(FallingRock.this);
        }
    }
}