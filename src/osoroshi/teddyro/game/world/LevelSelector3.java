package osoroshi.teddyro.game.world;

import osoroshi.teddyro.game.states.GameStateManager;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;
import osoroshi.teddyro.game.utils.SaveManager;

public class LevelSelector3 extends LevelSelector {
    
    private long time = System.currentTimeMillis(), eruptTime;
    
    public LevelSelector3(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        super.init();
        bg = GameResource.getWorld3Background();
        /*gls.add(new GameLevelSelector(0, getHeight() - 60, 6));
        gls.add(new GameLevelSelector(60, 0, 7));
        gls.add(new GameLevelSelector(240, getHeight() - 60, 8));
        gls.add(new GameLevelSelector(480, 0, 9));
        gls.add(new GameLevelSelector(getWidth()-60*2, getHeight() - 60, 10));
        gls.add(new Warp(getWidth()-60, getHeight() / 2 - 30, 10));*/
        int[][][] levels = {
            {{60, 60, 20}, {}, {}, {1, 1, 2, 1, -1, 6}},
            {{240, getHeight() - 120, 21}, {0}, {0}, {-1, 0, 1, 2}},
            {{600, 60, 22}, {1}, {1}, {-1, 1, 2, 4, 1, 3}},
            {{getWidth() - 75, getHeight() / 2 - 30, 23}, {2}, {2}, {-2, 2, -1, 5}},
            {{((240 + (getWidth() - 120))) / 2 - 10, getHeight() - 120, 24}, {2}, {2}, {-2, 2}},
            {{getWidth() / 2 - 30, getHeight() / 2 - 30, 25}, {3}, {3}, {1, 3}},
            {{-60, 60, -1}, {0}, {}, {}}
        };
        for(int i = 0; i < levels.length; i++) {
            addLevel(levels[i]);
        }
        setBonus(4);
        setFinalAccess(5);
        setAccessToWorld(6, 1);
        for(int i = 0; i < 5; i++) {
            gls.get(i).setFinish(SaveManager.isFinish(i + 11));
        }
        JukeBox.setLooping("volcano", 5000, -1);
        eruptTime = 7500 + new java.util.Random().nextInt(2250);
        time = System.currentTimeMillis();
    }
    
    public void update() {
        super.update();
        if(System.currentTimeMillis() - time > eruptTime) {
            JukeBox.play("erupt"+new java.util.Random().nextInt(3));
            time = System.currentTimeMillis();
            eruptTime = 10000 + new java.util.Random().nextInt(5000);
        }
    }
    
    public int getWorld() {
        return 2;
    }
}