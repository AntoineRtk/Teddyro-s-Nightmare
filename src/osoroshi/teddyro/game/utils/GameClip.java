package osoroshi.teddyro.game.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameClip implements Cloneable {
    
    private String name;
    private Clip clip;
    private AudioInputStream ais;
    private boolean running = false;
    private FloatControl fc;
    
    public GameClip() {
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(GameClip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public GameClip clone() {
        try {
            return (GameClip) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(GameClip.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void load(String name, String path) {
        try {
            this.name = name;
            ais = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource("osoroshi/teddyro/resources/musics/"+path+".wav"));
            clip.open(ais);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            clip.addLineListener(new LineListener() {
                public void update(LineEvent le) {
                    if(le.getType() == LineEvent.Type.STOP) stop();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(GameClip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void play() {
        if(isPlaying()) return;
        stop();
        running = true;
        clip.setFramePosition(0);
        clip.start();
    }
    
    public void playWithFramePosition(int frames) {
        if(isPlaying()) return;
        stop();
        running = true;
        clip.setFramePosition(frames);
        clip.start();
    }
    
    public void stop() {
        clip.setFramePosition(0);
        clip.stop();
        running = false;
    }
    
    public String getName() {
        return name;
    }
    
    public void loop(int start, int end) {
        if(isPlaying()) return;
        running = true;
        clip.setFramePosition(0);
        int s = start, e = end;
        if(start == -1) {
            s = 0;
        }
        clip.loop(-1);
        clip.setLoopPoints(s, e);
    }
    
    public void setVolume(float volume) {
        fc.setValue(volume);
    }
    
    public boolean isPlaying() {
        return running;
    }
    
    public void mute() {
        if(fc.getValue() == 0) {
            fc.setValue(fc.getMinimum());
        }
        else {
            fc.setValue(0);
        }
    }
    
    public void mute(boolean mute) {
        if(mute) {
            fc.setValue(fc.getMinimum());
        }
        else {
            fc.setValue(0);
        }
    }
    
    public void stopSFX() {
        if((clip.getMicrosecondLength()/1000000.0) < 10) {
            stop();
        }
    }
    
    public void setPosition(int position) {
        this.clip.setFramePosition(position);
    }
    
    public int getPosition() {
        return clip.getFramePosition();
    }
    
    public void cancelLooping() {
        int pos = clip.getFramePosition();
        clip.loop(0);
        clip.setFramePosition(pos);
    }
    
    public void loop(int start, int end, int position) {
        if(isPlaying()) return;
        running = true;
        clip.setFramePosition(position);
        int s = start, e = end;
        if(start == -1) {
            s = 0;
        }
        clip.loop(-1);
        clip.setLoopPoints(s, e);
    }
    
    public double clipLengthInSeconds() {
        return clip.getMicrosecondLength() / 1000000.0;
    }
}