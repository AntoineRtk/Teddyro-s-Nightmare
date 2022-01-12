package osoroshi.teddyro.game.tilemap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import osoroshi.teddyro.game.objects.Light;
import osoroshi.teddyro.game.states.GameState;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class MazeTileMap {
    
    private Tile[][] tiles;
    private int x, y, r = new java.util.Random().nextInt(256), g = new java.util.Random().nextInt(256), b = new java.util.Random().nextInt(256), flashCount;
    private boolean rr, gr, br, flashing;
    private double rotation = 0;
    private int luminosity = 0;
    private ArrayList<Light> lights = new ArrayList<>();
    private Color color = new Color(r, g, b);
    private BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    private GameState gs;
    private int flashLuminosityCount;
    
    public MazeTileMap(GameState gs) {
        this.gs = gs;
    }
    
    public void update() {
        for(int i = 0; i < lights.size(); i++) {
            lights.get(i).update();
        }
        flashCount++;
        int c = new java.util.Random().nextInt(3);
        switch(c) {
            case 0:
                if(rr) {
                    r += -15;
                }
                else {
                    r += 15;
                }
                break;
            case 1:
                if(gr) {
                    g += -15;
                }
                else {
                    g += 15;
                }
                break;
            case 2:
                if(br) {
                    b += -15;
                }
                else {
                    b += 15;
                }
                break;
        }
        if(r > 255) {
            r = 255;
            rr = true;
        }
        if(g > 255) {
            g = 255;
            gr = true;
        }
        if(b > 255) {
            b = 255;
            br = true;
        }
        if(r < 0) {
            r = 0;
            rr = false;
        }
        if(g < 0) {
            g = 0;
            gr = false;
        }
        if(b < 0) {
            b = 0;
            br = false;
        }
        color = new Color(r, g, b);
        if(flashCount >= 750 && luminosity == 255 && !flashing) {
            flashing = true;
            flashLuminosityCount = 0;
            JukeBox.play("applause"+new java.util.Random().nextInt(3));
        }
        if(flashing) {
            flashLuminosityCount++;
            if(flashLuminosityCount == (int) (25.0 * 5.0 / 4.0)) addLight();
            if(flashLuminosityCount > 25.0 * 5.0) {
                flashing = false;
                flashLuminosityCount = 0;
                flashCount = 0;
            }
        }
    }
    
    public void paint(Graphics2D g) {
        for(int col = 0; col < tiles.length; col++) {
            for(int row = 0; row < tiles[col].length; row++) {
                if(tiles[col][row].getImage() == image) continue;
                g.setColor(new Color(255, 255, 255, luminosity));
                if(tiles[col][row].isSolid()) {
                    g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), luminosity));
                }
                g.fillRect(row * 32 - x, col * 32 - y, 32, 32);
                if(tiles[col][row].getImage() != null && tiles[col][row].getImage().getWidth() == 45) {
                    g.drawImage(tiles[col][row].getImage(), row * 32 - x, col * 32 - y, 32, 32, null);
                }
                /*g.setColor(Color.blue);
                g.drawRect(row * 32 - x, col * 32 - y, 32, 32);*/
            }
        }
        for(int i = 0; i < lights.size(); i++) {
            lights.get(i).paint(g);
        }
        if(luminosity != 255) return;
        for(int col = 0; col < tiles.length; col++) {
            for(int row = 0; row < tiles[col].length; row++) {
                if(col == 0 && tiles[col][row].isSolid()) {
                    for(int i = 0; i < 4; i++) {
                        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 / 4 * 3 - 255 / 4 * 3 / 4 * i));
                        g.fillRect(row * 32 - x, col * 32 - 32 * (i + 1) - y, 32, 32);
                    }
                }
                if(col == tiles.length - 1 && tiles[col][row].isSolid()) {
                    for(int i = 0; i < 4; i++) {
                        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 / 4 * 3 - 255 / 4 * 3 / 4 * i));
                        g.fillRect(row * 32 - x, col * 32 + 32 * (i + 1) - y, 32, 32);
                    }
                }
                if(row == tiles[col].length - 1 && tiles[col][row].isSolid()) {
                    for(int i = 0; i < 4; i++) {
                        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 / 4 * 3 - 255 / 4 * 3 / 4 * i));
                        g.fillRect(row * 32 + 32 * (i + 1) - x, col * 32 - y, 32, 32);
                    }
                }
                if(row == 0 && tiles[col][row].isSolid()) {
                    for(int i = 0; i < 4; i++) {
                        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 / 4 * 3 - 255 / 4 * 3 / 4 * i));
                        g.fillRect(row * 32 - 32 * (i + 1) - x, col * 32 - y, 32, 32);
                    }
                }
                if(tiles[col][row].isSolid()) {
                    for(int i = 0; i < -1; i++) {
                        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 - 255 / 4 * 3 / 4 * i));
                        g.fillRect(row * 32 - 32 * (i + 1) - x, col * 32 - y, 32, 32);
                        g.fillRect(row * 32 + 32 * (i + 1) - x, col * 32 - y, 32, 32);
                        g.fillRect(row * 32 - x, col * 32 + 32 * (i + 1) - y, 32, 32);
                        g.fillRect(row * 32 - x, col * 32 - 32 * (i + 1) - y, 32, 32);
                    }
                }
            }
        }
        if(flashing) {
            int alpha = 255;
            if(flashLuminosityCount < 25.0 * 5.0 / 4.0 || flashLuminosityCount > 93.75) {
                alpha = (int) (255.0 / (25.0 * 5.0 / 4.0) * flashLuminosityCount);
                if(flashLuminosityCount > 93.75) {
                    alpha = 255 - (int) (255.0 / (25.0 * 5.0 / 4.0) * (flashLuminosityCount - 93.75));
                }
            }
            g.setColor(new Color(255, 255, 255, alpha));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    public void setPosition(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
        if(this.x < 32 * -4) {
            this.x = 32 * -4;
        }
        if(this.y < 32 * -4) {
            this.y = 32 * -4;
        }
        if(this.x + gs.getWidth() > getWidth() + 32 * 4) {
            this.x = getWidth() + 32 * 4 - gs.getWidth();
        }
        if(this.y + gs.getHeight() > getHeight() + 32 * 4) {
            this.y = getHeight() + 32 * 4 - gs.getHeight();
        }
        /*if(this.x + 350 > tiles[0].length * 32 + 32 * 4 - 350) {
            this.x = tiles[0].length * 32 + 32 * 4 - 350;
        }
        if(this.y + 350 > tiles.length * 32 + 32 * 4 - 350) {
            this.x = tiles.length * 32 + 32 * 4 - 350;
        }*/
    }
    
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    
    public boolean isSolid(double x, double y) {
        try {
            return tiles[(int) y / 32][(int) x / 32].isSolid();
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) { return true; }
    }
    
    public boolean isFlower(double x, double y) {
        return tiles[(int) y / 32][(int) x / 32].getImage() != null && tiles[(int) y / 32][(int) x / 32].getImage().getWidth() == 45;
    }
    
    public void load(String path) {
        try {
            lights.clear();
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("osoroshi/teddyro/resources/maps/"+path+".mta")));
            int w = Integer.parseInt(br.readLine());
            int h = Integer.parseInt(br.readLine());
            tiles = new Tile[h][w];
            for(int hm = 0; hm < h; hm++) {
                String[] line = br.readLine().split(" ");
                for(int wm = 0; wm < w; wm++) {
                    if(Integer.parseInt(line[wm]) == 0) {
                        tiles[hm][wm] = new Tile(null, false);
                    }
                    else if(Integer.parseInt(line[wm]) == 1) {
                        tiles[hm][wm] = new Tile(null, true);
                    }
                    else if(Integer.parseInt(line[wm]) == 2) {
                        tiles[hm][wm] = new Tile(image, true);
                    }
                    else {
                        tiles[hm][wm] = new Tile(GameResource.getFlowerSprites().get(0)[0], false);
                    }
                }
            }
            br.close();
            addLight();
        } catch (IOException ex) {
            System.exit(ex.hashCode());
        }
    }
    
    public void addLight() {
        lights.clear();
        for(int i = 0; i < Math.round(4.0 / 100 * (getWidth() / 32.0 * (getHeight() / 32.0))); i++) {
            lights.add(new Light(this, new java.util.Random().nextInt(getWidth()), new java.util.Random().nextInt(getHeight()), new java.util.Random().nextInt(getWidth()), new java.util.Random().nextInt(getHeight()), i % 2));
        }
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return tiles[0].length * 32;
    }
    
    public int getHeight() {
        return tiles.length * 32;
    }
    
    public void setLuminosity(int luminosity) {
        this.luminosity = luminosity;
    }
    
    public double getRotation() {
        return rotation * Math.PI / 180;
    }
    
    public void stopFlashing() {
        flashCount = 0;
        flashing = false;
    }
    
    public Color getColor() {
        return new Color(r, g, b);
    }
}