package osoroshi.teddyro.game.objects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import osoroshi.teddyro.game.animations.Firework;
import osoroshi.teddyro.game.objects.mobs.Comet;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.JukeBox;

public class FireworkGenerator extends MapElement {
    
    private Comet comet;
    private ArrayList<Firework> fireworks = new ArrayList<>();
    private long cometSpawnTime, currentTime, timer, cometTime;
    
    public FireworkGenerator(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        currentTime = System.currentTimeMillis();
        comet = new Comet(tileMap);
    }
    
    public void update() {
        if(System.currentTimeMillis() - currentTime > timer) {
            timer = 500 + new java.util.Random().nextInt(3000) + 1;
            currentTime = System.currentTimeMillis();
            fireworks.add(new Firework(tileMap, tileMap.getX() + new java.util.Random().nextInt(700), 1000));
            if(!JukeBox.isRunning("firework")) JukeBox.playNewInstance("firework");
        }
        for(int i = 0; i < fireworks.size(); i++) {
            fireworks.get(i).update();
            if(fireworks.get(i).shouldBeRemoved()) {
                fireworks.remove(i);
            }
        }
        if(System.currentTimeMillis() - cometTime > cometSpawnTime) {
            comet.spawn();
            cometSpawnTime = 30000 + new java.util.Random().nextInt(30001);
            cometTime = System.currentTimeMillis();
        }
        comet.update();
    }
    
    public void paint(Graphics2D g) {
        for(int i = 0; i < fireworks.size(); i++) {
            fireworks.get(i).paint(g);
        }
        comet.paint(g);
    }
}