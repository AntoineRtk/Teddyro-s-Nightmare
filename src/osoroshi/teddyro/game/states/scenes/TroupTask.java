
package osoroshi.teddyro.game.states.scenes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.animations.Animation;
import osoroshi.teddyro.game.utils.GameResource;

public class TroupTask extends Task {
    
    private ArrayList<SceneMonster> sms = new ArrayList<>();
    
    public TroupTask(int info, String params) {
        super(info, params);
        sms.add(new SceneMonster(GameResource.getDinosaurSprites().get(0), 6));
        sms.add(new SceneMonster(GameResource.getSquidSprites().get(0), 3));
        sms.add(new SceneMonster(GameResource.getSkeletonSprites().get(1), 1));
        sms.add(new SceneMonster(GameResource.getSnailSprites().get(0), 1));
        sms.add(new SceneMonster(GameResource.getRedSquidSprites().get(0), 2));
        sms.add(new SceneMonster(GameResource.getSnadoSprites().get(0), 3));
        sms.add(new SceneMonster(GameResource.getBatSprites().get(0), 3));
        sms.get(sms.size() - 1).setBat(true);
    }
    
    public void update() {
        for(int i = 0; i < sms.size(); i++) {
            sms.get(i).update();
        }
    }
    
    public void paint(Graphics2D g) {
        for(int i = 0; i < sms.size(); i++) {
            sms.get(i).paint(g);
        }
    }
    
    public boolean isFinish() {
        for(int i = 0; i < sms.size(); i++) {
            if(!sms.get(i).isPlaced()) {
                return false;
            }
        }
        return true;
    }
    
    public void setGoingForward(boolean b) {
        for(int i = 0; i < sms.size(); i++) {
            sms.get(i).setGoingForward(false);
            if(sms.get(i).getMinWidth() == 330) {
                sms.get(i).setGoingForward(true);
                sms.get(i).setMinx(0);
            }
        }
    }
    
    private class SceneMonster {
        
        private double x;
        private int minx;
        private Animation animation;
        private boolean goingForward = true, isBat = false;
        
        public SceneMonster(BufferedImage[] animations, int delay) {
            animation = new Animation();
            animation.setFrames(animations);
            animation.setDelay(delay);
            x = 1000 + Math.random() * 2000;
            for(int i = 0; i < sms.size(); i++) {
                minx += sms.get(i).getMinWidth() + 5;
            }
        }
        
        public void setMinx(int minx) {
            this.minx = minx + 5;
        }
        
        public void update() {
            animation.update();
            if(this.goingForward) {
                x -= 4.3575;
                if(x < 180 + minx) {
                    x = 180 + minx;
                    if(!isBat) animation.setFrame(0);
                }
            }
            else {
                x += 4.3575;
            }
        }
        
        public void setGoingForward(boolean goingForward) {
            this.goingForward = goingForward;
        }
        
        public int getMinWidth() {
            return animation.getFrames()[0].getWidth();
        }
        
        public void paint(Graphics2D g) {
            if(!goingForward) {
                g.drawImage(animation.getImage(), (int) x + animation.getImage().getWidth(), 600 - animation.getFrames()[0].getHeight(), -animation.getFrames()[0].getWidth(), animation.getFrames()[0].getHeight(), null);
            }
            else {
                g.drawImage(animation.getImage(), (int) x, 600 - animation.getFrames()[0].getHeight(), animation.getFrames()[0].getWidth(), animation.getFrames()[0].getHeight(), null);
            }
        }
        
        public void setBat(boolean isBat) {
            this.isBat = isBat;
            animation.setReverse(true);
        }
        
        public boolean isPlaced() {
            return (goingForward) ? x == 180 + minx : x > 1000;
        }
    }
}