package osoroshi.teddyro.game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.objects.Dog;
import osoroshi.teddyro.game.tilemap.MazeTileMap;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.JukeBox;
import osoroshi.teddyro.game.utils.SaveManager;

public class Maze {
    
    private GameState gs;
    private MazeTileMap mTileMap;
    private Dog dog;
    private long time = System.currentTimeMillis();
    private int pauseCount = 0, luminosity = 0, fontSize = 0, endCount, l, wo;
    private boolean finish = false;
    private ArrayList<ImagePiece> pieces = new ArrayList<>();
    
    public Maze(GameState gs, int l) {
        this.gs = gs;
        this.l = l;
        mTileMap = new MazeTileMap(this.gs);
        dog = new Dog(this.gs, this, mTileMap);
        dog.setPosition(32, 32);
    }
    
    public void start() {
        JukeBox.stopAll();
        dog.setCanMove(false);
        time = System.currentTimeMillis();
        JukeBox.setLooping("bonusroom", 597555, -1);
        luminosity = 0;
        endCount = 0;
    }
    
    public void update() {
        mTileMap.update();
        dog.update();
        if(System.currentTimeMillis() - time > 13750) {
            if(luminosity != 1) {
                wo = 255;
                luminosity = 1;
                mTileMap.setLuminosity(255);
                JukeBox.play("lightswitch");
                dog.setCanMove(true);
            }
        }
        else {
            luminosity = (int) (127.5 / 10000 * (System.currentTimeMillis() - time));
            mTileMap.setLuminosity(luminosity);
        }
        if(Controler.isEscape() && System.currentTimeMillis() - time > 13750 && pauseCount > 30 && !finish) {
            ((PauseState) gs.gsm.getState(5)).setState(gs.getStateIndex());
            gs.setState(5);
            pauseCount = 0;
        }
        pauseCount++;
        if(finish) {
            dog.setCanMove(false);
            double r = Math.abs(mTileMap.getRotation()) * 180.0 / Math.PI % 360;
            if(r != 360 || r != 0) {
                if(r < 180) {
                    dog.addRotation((mTileMap.getRotation() > 0) ? -1 : 1);
                }
                else {
                    dog.addRotation((mTileMap.getRotation() > 0) ? 1 : -1);
                }
            }
        }
        for(int i = 0; i < pieces.size(); i++) {
            pieces.get(i).update();
        }
    }
    
    public void paint(Graphics2D g) {
        BufferedImage maze = new BufferedImage(gs.getWidth(), gs.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = maze.createGraphics();
        mTileMap.paint(g2);
        dog.paint(g2);
        AffineTransform af = g.getTransform();
        g.setTransform(AffineTransform.getRotateInstance(-mTileMap.getRotation(), gs.getWidth() / 2, gs.getHeight() / 2));
        g.drawImage(maze, 0, 0, null);
        g.setTransform(af);
        g.setFont(new Font("Gabriola", Font.ITALIC, 50));
        if(System.currentTimeMillis() - time > 13750 && System.currentTimeMillis() - time < 20000) {
            TextLayout welcome = new TextLayout("Welcome to the Maze !", g.getFont(), g.getFontRenderContext());
            int red = mTileMap.getColor().getRed() - 50, green = mTileMap.getColor().getGreen() - 50, blue = mTileMap.getColor().getBlue() - 50;
            if(red < 0) red = 0;
            if(green < 0) green = 0;
            if(blue < 0) blue = 0;
            g.setColor(new Color(red, green, blue, wo));
            g.drawString("Welcome to the Maze !", gs.getWidth() / 2 - (int) welcome.getBounds().getWidth() / 2, 100);
            if(System.currentTimeMillis() - time > 17000) {
                wo = 255 - (int) (255.0 / 3000.0 * (System.currentTimeMillis() - time - 17000));
            }
        }
        if(!pieces.isEmpty()) {
            g.setColor(Color.black);
            g.fillRect(0, 0, gs.getWidth(), gs.getHeight());
            for(int i = 0; i < pieces.size(); i++) {
                pieces.get(i).paint(g);
            }
            fontSize += 5;
            if(fontSize > 180) fontSize = 180;
            g.setFont(new Font("Gabriola", Font.BOLD, fontSize));
            TextLayout txt = new TextLayout("Terminé !", g.getFont(), g.getFontRenderContext());
            g.setColor(mTileMap.getColor());
            g.fill(txt.getOutline(AffineTransform.getTranslateInstance(gs.getWidth() / 2.0 - txt.getBounds().getWidth() / 2.0, gs.getHeight() / 2.0)));
            g.setColor(Color.white);
            g.draw(txt.getOutline(AffineTransform.getTranslateInstance(gs.getWidth() / 2.0 - txt.getBounds().getWidth() / 2.0, gs.getHeight() / 2.0)));
            endCount++;
            if(endCount == 125) {
                SaveManager.finish(l);
                gs.setState(2);
            }
        }
        if(Math.abs(mTileMap.getRotation()) * 180.0 / Math.PI % 360 == 0 && pieces.isEmpty() && finish) {
            BufferedImage screen = gs.getScreen();
            pieces.add(new ImagePiece(screen.getSubimage(0, 0, gs.getWidth() / 2, gs.getHeight() / 2), 0, 0, -1, -1));
            pieces.add(new ImagePiece(screen.getSubimage(gs.getWidth() / 2, 0, gs.getWidth() / 2, gs.getHeight() / 2), gs.getWidth() / 2.0, 0, 1, -1));
            pieces.add(new ImagePiece(screen.getSubimage(0, gs.getHeight() / 2, gs.getWidth() / 2, gs.getHeight() / 2), 0, gs.getHeight() / 2.0, -1, 1));
            pieces.add(new ImagePiece(screen.getSubimage(gs.getWidth() / 2, gs.getHeight() / 2, gs.getWidth() / 2, gs.getHeight() / 2), gs.getWidth() / 2.0, gs.getHeight()/ 2.0, 1, 1));
            JukeBox.stop("bonusroom");
            JukeBox.play("endbonusroom");
            fontSize = 0;
        }
    }
    
    public void load(String path) {
        mTileMap.load(path);
    }
    
    public void finish() {
        finish = true;
        mTileMap.stopFlashing();
    }
    
    private class ImagePiece {
        
        private BufferedImage image;
        private double x, y;
        private int dirx, diry;
        
        public ImagePiece(BufferedImage image, double x, double y, int dirx, int diry) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.dirx = dirx;
            this.diry = diry;
        }
        
        public void update() {
            double distx = gs.getWidth(), disty = gs.getHeight();
            x += (gs.getWidth() / Math.max(distx, disty)) * (dirx * 4);
            y += (gs.getHeight() / Math.max(distx, disty)) * (diry * 4);
        }
        
        public void paint(Graphics2D g) {
            g.drawImage(image, (int) x, (int) y, null);
        }
    }
}