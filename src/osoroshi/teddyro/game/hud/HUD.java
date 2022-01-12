package osoroshi.teddyro.game.hud;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.objects.BouncingBall;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.utils.GameResource;

public class HUD {
    
    private GameLevel gl;
    private Hero hero;
    private int lifeY = 50;
    private boolean reverse = false, reverseO = false;
    private BufferedImage image, ballImage;
    private double rotation, o;
    private int[][] placements;
    
    public HUD(GameLevel gl, Hero hero) {
        this.gl = gl;
        this.hero = hero;
        placements = new int[30][30];
        this.image = GameResource.getStarImage(30, 30);
        for(int w = 0; w < image.getWidth(); w++) {
            for(int h = 0; h < image.getHeight(); h++) {
                if(image.getRGB(w, h) != 0) {
                    image.setRGB(w, h, Color.yellow.getRGB());
                    placements[h][w] = 1;
                }
            }
        }
        ballImage = new BouncingBall(null, null, 0, 0).getImage();
    }
    
    public void update() {
        rotation += 5;
        if(!reverse) {
            lifeY--;
            if(lifeY <= 30) {
                reverse = true;
            }
        }
        else {
            lifeY++;
            if(lifeY >= 50) {
                reverse = false;
            }
        }
        if(rotation == 360) {
            rotation = 0;
        }
        if(!reverseO) o++;
        else o--;
        if(o == -1) {
            o = 1;
            reverseO = false;
        }
        if(o == 151) {
            o = 149;
            reverseO = true;
        }
    }
    
    public void paint(Graphics2D g) {
        Composite c = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g.setPaint(new GradientPaint(0, 0, Color.green, 20, lifeY, new Color(0, 130, 0), true));
        g.fillRect(10, gl.getHeight() - 40, (int) (300 / 5.0 * hero.getLife()), 30);
        Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(3f));
        g.setColor(Color.black);
        g.drawRect(10, gl.getHeight() - 40, 300, 30);
        g.setStroke(stroke);
        g.setComposite(c);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawImage(image, 313, gl.getHeight() - 40, null);
        g.drawString(hero.getStarsLeft()+"/5", 313 + image.getWidth(), gl.getHeight() - 17);
        g.setColor(new Color(255, 128, 0));
        g.setColor(new Color(255, 255, 255, (int) (191.25 / 150.0 * o)));
        for(int h = 0; h < placements.length; h++) {
            for(int w = 0; w < placements[h].length; w++) {
                if(placements[h][w] == 1) {
                    g.fillRect(313 + w, gl.getHeight() - 40 + h, 1, 1);
                }
            }
        }
        if(hero.getPower().getType() != null) {
            switch(hero.getPower().getType().getClass().getName()) {
                case "BouncingBallPower":
                    AffineTransform af = g.getTransform();
                    g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rotation), 310 + image.getWidth() * 3 + 15, gl.getHeight() - 25));
                    g.drawImage(ballImage, 313 + image.getWidth() * 3, gl.getHeight() - 40, 30, 30, null);
                    g.setTransform(af);
                    break;
            }
        }
    }
}