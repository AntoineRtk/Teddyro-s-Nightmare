package osoroshi.teddyro.game.utils;

import java.util.ArrayList;

public class JukeBox {
    
    private static ArrayList<GameClip> gcs = new ArrayList<>();
    private static int count;
    
    public static void load(String name, String path) {
        GameClip gc = new GameClip();
        gc.load(name, path);
        gcs.add(gc);
        count++;
        GameResource.setPurcent(91.0 + Math.round(1.0 * count / 53.0 * 9.0));
    }
    
    public static void play(String name) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                gcs.get(i).play();
            }
        }
    }
    
    public static void dischargeAll() {
        count = 0;
        for(int i = 0; i < gcs.size(); i++) {
            gcs.get(i).stop();
            gcs.remove(i);
        }
    }
    
    public static void play(int number) {
        switch(number) {
            case -1:
                JukeBox.stopAll();
                break;
            case 1:
                //JukeBox.setLooping("lostvillage", 1293000, -1);
                JukeBox.setLooping("castle", 158455, -1);
                break;
            case 2:
                JukeBox.setLooping("desert", 35150, 3476500-3476500-1);
                break;
            case 3:
                JukeBox.setLooping("city", -1, -1);
                break;
            case 4:
                JukeBox.setLooping("swamp", -1, -1);
                break;
            case 5:
                JukeBox.setLooping("platform", 74000, -1);
                break;
            case 6:
                JukeBox.setLooping("dream", 44960, -1);
                break;
            case 7:
                JukeBox.setLooping("plains", -1, -1);
                break;
            case 8:
                JukeBox.setLooping("capsule", -1, -1, 815530);
                break;
            case 9:
                JukeBox.setLooping("winter", -1, -1);
                break;
            case 10:
                JukeBox.setLooping("boss", 106000, -1);
                break;
        }
    }
    
    public static void playNewInstance(String name) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                GameClip gc = gcs.get(i);
                gc.stop();
                gc.setPosition(0);
                gc.play();
                gc.setPosition(0);
            }
        }
    }
    
    public static boolean isRunning(String name) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                return gcs.get(i).isPlaying();
            }
        }
        return false;
    }
    
    public static void stop(String name) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                gcs.get(i).stop();
            }
        }
    }
    
    public static void stopAll() {
        for(int i = 0; i < gcs.size(); i++) {
            gcs.get(i).stop();
        }
    }
    
    public static void setLooping(String name, int start, int end) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                gcs.get(i).loop(start, end);
            }
        }
    }
    
    public static void setLooping(String name, int start, int end, int position) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                gcs.get(i).loop(start, end, position);
            }
        }
    }
    
    public static int getPosition(String name) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                return gcs.get(i).getPosition();
            }
        }
        return -1;
    }
    
    public static void setPosition(String name, int position) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                gcs.get(i).setPosition(position);
            }
        }
    }
    
    public static void mute() {
        for(int i = 0; i < gcs.size(); i++) {
            gcs.get(i).mute();
        }
    }
    
    public static void setMute(boolean b) {
        if(b) {
            for(int i = 0; i < gcs.size(); i++) {
                gcs.get(i).mute(true);
            }
        }
        else {
            for(int i = 0; i < gcs.size(); i++) {
                gcs.get(i).mute(false);
            }
        }
    }
    
    public static void stopSFX() {
        for(int i = 0; i < gcs.size(); i++) {
            gcs.get(i).stopSFX();
        }
    }
    
    public static void playWithFramePosition(String name, int position) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                gcs.get(i).playWithFramePosition(position);
            }
        }
    }
    
    public static void cancelLooping(String name) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                gcs.get(i).cancelLooping();
            }
        }
    }
    
    public static double clipLengthInSeconds(String name) {
        for(int i = 0; i < gcs.size(); i++) {
            if(gcs.get(i).getName().equals(name)) {
                return gcs.get(i).clipLengthInSeconds();
            }
        }
        return -1;
    }
}