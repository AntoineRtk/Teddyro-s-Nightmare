package osoroshi.teddyro.game.world;

import osoroshi.teddyro.game.states.GameStateManager;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;
import osoroshi.teddyro.game.utils.SaveManager;

public class LevelSelector1 extends LevelSelector {
    
    public LevelSelector1(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        super.init();
        bg = GameResource.getWorld1Background();
        int[][][] levels = {
            {{30, 100, 9}, {}, {}, {1, 1, 2, 1}},
            {{50, 400, 10}, {0}, {0}, {-1, 0, -2, 0, 1, 2, 2, 3}},
            {{300, 280, 11}, {1}, {1}, {-1, 1, 1, 4, -2, 4}},
            {{600, 520, 12}, {1}, {1}, {-1, 1, -2, 4, 1, 4}},
            {{600, 30, 13}, {2, 3}, {2, 3}, {-1, 2, 2, 3, 1, 5}},
            {{getWidth(), 10, -1}, {4}, {4}, {}},
        };
        for(int i = 0; i < levels.length; i++) {
            addLevel(levels[i]);
        }
        setBonus(3);
        setBoss(4);
        setAccessToWorld(5, 1);
        for(int i = 0; i < 5; i++) {
            gls.get(i).setFinish(SaveManager.isFinish(i + 1));
        }
        JukeBox.setLooping("world1", 75000, -1);
    }
    
    public int getWorld() {
        return 0;
    }
}