package osoroshi.teddyro.game.world;

import osoroshi.teddyro.game.states.GameStateManager;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;
import osoroshi.teddyro.game.utils.SaveManager;

public class LevelSelector2 extends LevelSelector {
    
    public LevelSelector2(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        super.init();
        bg = GameResource.getWorld2Background();
        int[][][] levels = {
            {{141, 433, 15}, {}, {}, {-2, 1, 1, 2, -1, 5}},
            {{513, 135, 16}, {0}, {0}, {-1, 0, 2, 0}},
            {{513, 301, 17}, {0}, {0}, {-1, 0, 2, 3}},
            {{513, 466, 18}, {2}, {2}, {-2, 2, -1, 4}},
            {{141, 135, 19}, {3}, {3}, {1, 3, 2, 3, -2, 6}},
            {{-60, 433, -1}, {0}, {}, {}},
            {{getWidth() / 2 - 30, -60, -1}, {4}, {4}, {}}
        };
        for(int i = 0; i < levels.length; i++) {
            addLevel(levels[i]);
        }
        setBonus(3);
        setBoss(4);
        setAccessToWorld(5, 0);
        setAccessToWorld(6, 2);
        for(int i = 0; i < 5; i++) {
            gls.get(i).setFinish(SaveManager.isFinish(i + 6));
        }
        JukeBox.play(4);
    }
    
    public int getWorld() {
        return 1;
    }
}