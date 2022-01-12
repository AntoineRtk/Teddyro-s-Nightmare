package osoroshi.teddyro.game.objects;

import osoroshi.teddyro.game.objects.camera.Camera;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.animations.ShootingStars;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Star extends MapElement {
    
    private boolean heroInStar = false, running = false, hudShown;
    private int count = 0, yg;
    private double sx, sy, rotationCoutdown;
    private long coutdown = -1;
    private ArrayList<ShootingStars> ss = new ArrayList<>();
    
    public Star(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        BufferedImage image = GameResource.getStarImage(70, 70);
        int rgb = Color.blue.getRGB();
        for(int h = 0; h < image.getHeight(); h++) {
            for(int w = 0; w < image.getWidth(); w++) {
                if(image.getRGB(w, h) == rgb) {
                    if(h % 4 == 0) {
                        image.setRGB(w, h, Color.red.getRGB());
                    }
                    else {
                        image.setRGB(w, h, Color.yellow.getRGB());
                    }
                }
            }
        }
        animation.setFrames(new BufferedImage[]{image});
        width = 70;
        height = 70;
        moveSpeed = 0.415;
        maxMoveSpeed = 40;
        stopSpeed = 0.8;
    }
    
    public void update() {
        yg++;
        for(int i = 0; i < ss.size(); i++) {
            ss.get(i).update();
            if(ss.get(i).shouldBeRemoved()) {
                ss.remove(i);
            }
        }
        super.update();
        super.checkCollisions();
        if(dx == 0 && heroInStar && getDirX() == 1) {
            heroInStar = false;
            tileMap.hero.kill();
            gl.setCamera(tileMap.hero);
            gl.setCameraAllignement(Camera.MID);
            gl.setShowHUD(hudShown);
        }
        rotation += 2;
        if(rotation >= 360) {
            rotation = 0;
        }
        if(Controler.isRight() && heroInStar) {
            maxMoveSpeed++;
            if(maxMoveSpeed > 85) {
                maxMoveSpeed = 85;
                for(int i = 0; i < 5 + new java.util.Random().nextInt(20); i++) {
                    ss.add(new ShootingStars(tileMap, getX(), getY() + new java.util.Random().nextDouble() * height, -1));
                }
            }
        }
        else if(heroInStar) {
            maxMoveSpeed--;
            if(maxMoveSpeed < 20) {
                maxMoveSpeed = 20;
            }
        }
        if(Controler.isUp() && heroInStar && System.currentTimeMillis() - coutdown > 5000) {
            setJumping(true);
            jumpStart = -3;
        }
        if(Controler.isDown() && heroInStar && System.currentTimeMillis() - coutdown > 5000) {
            setJumping(true);
            jumpStart = 3;
        }
        setDirX(0);
        if(coutdown != -1 && System.currentTimeMillis() - coutdown >= 1000) {
            int nbr = Integer.parseInt((""+(System.currentTimeMillis() - coutdown)).substring(0, 1));
            for(int i = 0; i < nbr; i++) {
                if(rotationCoutdown < (i + 1) * 360) {
                    rotationCoutdown += 36;
                    break;
                }
            }
        }
        if(heroInStar) {
            if(running) {
                tileMap.hero.x += sx;
                tileMap.hero.y += sy;
                count++;
                if(count >= 10) {
                    running = false;
                    coutdown = System.currentTimeMillis();
                    tileMap.hero.setX(getX());
                    tileMap.hero.setY(getY() + height / 2 - tileMap.hero.height / 2);
                }
            }
            else {
                if(System.currentTimeMillis() - coutdown > 5000) {
                    if(!JukeBox.isRunning("abovethesky")) {
                        JukeBox.stopAll();
                        JukeBox.setLooping("abovethesky", 177555, -1);
                    }
                    setDirX(1);
                    tileMap.hero.setX(getX());
                    tileMap.hero.setY(getY() + height / 2 - tileMap.hero.height / 2 - dy);
                }
            }
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(coutdown != -1) {
            g.setPaint(new GradientPaint(0, -yg, Color.red, 0, -yg + 30, Color.orange, true));
            g.setFont(new Font("Trajan Pro 3", Font.BOLD, 60));
            long nbr = (System.currentTimeMillis() - coutdown);
            String n = " ";
            if((nbr+"").length() == 4 && System.currentTimeMillis() - coutdown < 5000) {
                n = ""+(4 - (Integer.parseInt((nbr+"").substring(0, 1))));
                if(Integer.parseInt(n) <= 0) {
                    n = "LET'S GO !";
                }
            }
            TextLayout txt = new TextLayout(n, g.getFont(), g.getFontRenderContext());
            AffineTransform af = g.getTransform();
            g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rotationCoutdown), gl.getWidth() / 2, gl.getHeight() / 2));
            g.drawString(n, gl.getWidth() / 2 - (int) txt.getBounds().getWidth() / 2, gl.getHeight() / 2 + (int) txt.getBounds().getHeight() / 2);
            g.setTransform(af);
        }
        for(int i = 0; i < ss.size(); i++) {
            ss.get(i).paint(g);
        }
    }
    
    public void intersect(MapObject mo) {
        if(!(mo instanceof Hero)) return;
        if(!heroInStar) {
            hudShown = gl.isHUDShown();
            gl.setShowHUD(false);
            tileMap.hero.setFlipped(false);
            gl.setCamera(this);
            gl.setCameraAllignement(Camera.LEFT);
            heroInStar = true;
            tileMap.hero.setCanAction(false);
            tileMap.hero.fallingSpeed = 0;
            tileMap.hero.dx = 0;
            tileMap.hero.dy = 0;
            sx = getX() - tileMap.hero.getX();
            sy = (getY() + height / 2) - (tileMap.hero.getY() + tileMap.hero.height / 2);
            sx /= 10;
            sy /= 10;
            running = true;
        }
    }
}