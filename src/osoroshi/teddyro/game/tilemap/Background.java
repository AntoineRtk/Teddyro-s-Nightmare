package osoroshi.teddyro.game.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.utils.GameResource;

public class Background {
    
    private TileMap tileMap;
    private BufferedImage image;
    private GameLevel gl;
    private boolean first = false, castle;
    private int option = 0;
    public static final int NORMAL = 0, LOOPING = 1;
    
    public Background(TileMap tileMap, GameLevel gl, boolean first) {
        this.tileMap = tileMap;
        this.gl = gl;
        this.first = first;
    }
    
    public void load(int number) {
        image = GameResource.getBackground(number);
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public void paint(Graphics2D g) {
        if(image == null) return;
        int rx = tileMap.getX() % image.getWidth(), ry = tileMap.getX() % image.getWidth();
        if(option == 0) {
            if(image.getWidth() == 3840) {
                g.drawImage(image, 0 - rx, 0, image.getWidth(), gl.getHeight(), null);
                return;
            }
            if(castle) {
                g.drawImage(image.getSubimage(0, 0, 128, 128), -tileMap.getX(), 0, null);
                g.drawImage(image.getSubimage(256, 0, 128, 128), tileMap.getMapWidth() - tileMap.getX() - 128, 0, null);
                double ratioy = ((gl.getHeight() - 256) / 128.0);
                g.drawImage(image.getSubimage(0, 128, 128, 128), -tileMap.getX(), 128, 128, (int) (128 * ratioy), null);
                g.drawImage(image.getSubimage(256, 128, 128, 128), tileMap.getMapWidth() - tileMap.getX() - 128, 128, 128, (int) (128 * ratioy), null);
                g.drawImage(image.getSubimage(0, 256, 128, 128), -tileMap.getX(), gl.getHeight() - 128, null);
                g.drawImage(image.getSubimage(256, 256, 128, 128), tileMap.getMapWidth() - tileMap.getX() - 128, gl.getHeight() - 128, null);
                int width = (int) ((tileMap.getMapWidth() - 256.0) / 128.0);
                int height = (int) ((gl.getHeight() - 256.0) / 128.0);
                double newwidth = Math.ceil((tileMap.getMapWidth() - 256.0) / width);
                double newheight = Math.ceil((gl.getHeight() - 256.0) / height);
                for(int i = 0; i < width; i++) {
                    g.drawImage(image.getSubimage(128, 0, 128, 128), 128 + (int) (newwidth * i) - tileMap.getX(), 0, (int) newwidth, 128, null);
                    g.drawImage(image.getSubimage(128, 256, 128, 128), 128 + (int) (newwidth * i) - tileMap.getX(), gl.getHeight() - 128, (int) newwidth, 128, null);
                    for(int i1 = 0; i1 < height; i1++) {
                        g.drawImage(image.getSubimage(128, 128, 128, 128), 128 + (int) (newwidth * i) - tileMap.getX(), 128 + (int) (newheight * i1), (int) newwidth, (int) newheight, null);
                    }
                }
            }
            else {
                if(tileMap.getMapWidth() >= gl.getWidth()) {
                    for(int i = 0; i < gl.getWidth() / image.getWidth() + 2; i++) {
                        if(first) {
                            g.drawImage(image, i * image.getWidth() - rx, 0, image.getWidth(), gl.getHeight(), null);
                        }
                        else {
                            g.drawImage(image, i * image.getWidth() - rx, gl.getHeight() - image.getHeight(), image.getWidth(), image.getHeight(), null);
                        }
                    }
                }
                else {
                    for(int i = 0; i < gl.getWidth() / image.getWidth() + 2; i++) {
                        if(first) {
                            g.drawImage(image, i * image.getWidth(), 0, image.getWidth(), gl.getHeight(), null);
                        }
                        else {
                            g.drawImage(image, i * image.getWidth(), gl.getHeight() - image.getHeight(), image.getWidth(), image.getHeight(), null);
                        }
                    }
                }
            }
        }
        else {
            for(int w = -200; w <= gl.getWidth() * 1.4; w += 200) {
                for(int h = -200; h <= gl.getHeight() * 1.4; h += 200) {
                    g.drawImage(image, w - (tileMap.getX() % 200), h - (tileMap.getY() % 200), 200, 200, null);
                }
            }
        }
    }
    
    public void setSize(int size) {
        this.option = size;
    }
    
    public void setCastle(boolean b) {
        this.castle = b;
    }
}