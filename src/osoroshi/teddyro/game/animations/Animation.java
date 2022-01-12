package osoroshi.teddyro.game.animations;

import java.awt.image.BufferedImage;

public class Animation {
    
    private BufferedImage[] images;
    private int index, currentCount, count = -1;
    private boolean hasPlayedOnce = false, reverse = false, reversing = false;
    
    public void update() {
        if(count == -1) {
            return;
        }
        currentCount++;
        if(currentCount > count) {
            currentCount = 0;
            if(!reverse) {
                if(index + 1 >= images.length) {
                    index = 0;
                    hasPlayedOnce = true;
                }
                else {
                    index++;
                }
            }
            else {
                if(!reversing) {
                    index++;
                    if(index >= images.length) {
                        index = images.length - 2;
                        if(index < 0) index = 0;
                        reversing = true;
                    }
                }
                else {
                    index--;
                    if(index < 0) {
                        index = 1;
                        if(index >= images.length) index = images.length - 1;
                        reversing = false;
                        hasPlayedOnce = true;
                    }
                }
            }
        }
    }
    
    public void setDelay(int delay) {
        count = delay;
    }
    
    public BufferedImage getImage() {
        if(images == null) return null;
        return images[index];
    }
    
    public int getDelay() {
        return count;
    }
    
    public void setFrames(BufferedImage[] frames) {
        if(frames != images) {
            this.images = frames;
            this.count = -1;
            this.reverse = false;
            this.reversing = false;
            index = 0;
            currentCount = 0;
            hasPlayedOnce = false;
        }
    }
    
    public void setHasPlayedOnce(boolean hasPlayedOnce) {
        this.hasPlayedOnce = true;
    }

    public boolean hasPlayedOnce() {
        return hasPlayedOnce;
    }
    
    public BufferedImage[] getFrames() {
        return images;
    }
    
    public int getFrameIndex() {
        return index;
    }

    public void setFrame(int i) {
        this.index = i;
    }
    
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
    
    public boolean isReversing() {
        return this.reversing;
    }
}